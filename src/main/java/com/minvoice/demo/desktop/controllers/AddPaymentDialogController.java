package com.minvoice.demo.desktop.controllers;

import com.minvoice.demo.application.services.dto.PaymentDto;
import com.minvoice.demo.application.services.interfaces.IGeneralStatusService;
import com.minvoice.demo.application.services.interfaces.IPaymentService;
import com.minvoice.demo.domain.model.GeneralStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Component
@CommonsLog
@Getter
@RequiredArgsConstructor
public class AddPaymentDialogController {
    private final IGeneralStatusService statusService;
    private final IPaymentService paymentService;
    @Setter private int invoiceId;

    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cbTipoPago;
    @FXML private DatePicker dpFecha;
    @FXML private TextArea txtObservacion;
    @FXML private Label lblError;

    private double montoResult;
    private LocalDate fechaResult;
    private String tipoPagoResult;
    private String observacionResult;


    @FXML
    private void initialize() {
        dpFecha.setValue(LocalDate.now());

        Pattern decimalPattern = Pattern.compile("^$|^[+-]?\\d*(?:[\\.,]\\d{0,2})?$");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return decimalPattern.matcher(newText).matches() ? change : null;
        };
        txtMonto.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, filter));

        var statusTypes = statusService.findByGroup("payment");
        List<String> types = new ArrayList<>();
        for (GeneralStatus type : statusTypes) {
            types.add(type.getName() + " - " + type.getCode());
        }
        cbTipoPago.getItems().setAll(types);
        if (!types.isEmpty()) {
            cbTipoPago.setValue(types.get(0));
        }

    }

    private boolean validateAndCapture() {
        lblError.setVisible(false);

        String raw = txtMonto.getText();
        if (raw == null || raw.isBlank()) {
            return fail("Ingresa un monto.");
        }
        try {
            String normalized = raw.replace(',', '.');
            double value = Double.parseDouble(normalized);
            if (value <= 0) return fail("El monto debe ser mayor que 0.");
            this.montoResult = value;
        } catch (NumberFormatException ex) {
            return fail("Formato de monto inválido.");
        }

        String tipo = cbTipoPago.getValue();
        if (tipo == null || tipo.isBlank()) {
            return fail("Selecciona un tipo de pago.");
        }
        this.tipoPagoResult = tipo;

        LocalDate fecha = dpFecha.getValue();
        if (fecha == null) return fail("Selecciona una fecha.");
        this.fechaResult = fecha;

        this.observacionResult = txtObservacion.getText() == null ? "" : txtObservacion.getText().trim();

        return true;
    }

    private boolean fail(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
        return false;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onSave(ActionEvent actionEvent) {
        if (validateAndCapture()) {
            String status = tipoPagoResult.split("-")[1].trim();
            PaymentDto paymentDto = new PaymentDto(
                    montoResult,
                    observacionResult,
                    status,
                    fechaResult,
                    invoiceId
            );
            paymentService.addPayment(paymentDto);
            showAlert(Alert.AlertType.INFORMATION,
                    "Pago registrado correctamente",
                    "El pago con un monto de: " + montoResult + "$ guardado correctamente",
                    null);
            actionEvent.consume();
            onCancel(actionEvent);
            return;
        }
        showAlert(Alert.AlertType.WARNING,
                "Falta informacion por rellenar",
                "No se pudo registrar el pago porque falta informacion por rellenar",
                null);

    }
}
