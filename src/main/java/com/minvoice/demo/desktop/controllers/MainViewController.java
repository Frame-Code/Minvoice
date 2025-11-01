package com.minvoice.demo.desktop.controllers;

import com.minvoice.demo.application.services.dto.InvoiceDto;
import com.minvoice.demo.application.services.dto.InvoiceTableDto;
import com.minvoice.demo.application.services.interfaces.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@CommonsLog
public class MainViewController {
    private final ApplicationContext context;
    private final IInfoInvoiceService infoInvoiceService;
    private final IInvoiceService invoiceService;
    private final IGeneralStatusService statusService;

    @FXML private Button addNewInvoice;
    @FXML private Label lblTotalFacturado;
    @FXML private Label lblTotalPagado;
    @FXML private Label lblTotalPendiente;
    @FXML private Label lblCantidadFacturas;

    @FXML private TableView<InvoiceTableDto> tblTodas;
    @FXML private TableColumn<InvoiceTableDto, String> colEstadoTodas;
    @FXML private TableColumn<InvoiceTableDto, String> colDescripcionTodas;
    @FXML private TableColumn<InvoiceTableDto, Double> colMontoTodas;
    @FXML private TableColumn<InvoiceTableDto, LocalDateTime> colFechaTodas;
    @FXML private TableColumn<InvoiceTableDto, String> colArchivoTodas;
    @FXML private TableColumn<InvoiceTableDto, Double> colPorPagarTodas;

    @FXML private TableView<InvoiceTableDto> tblPendientes;
    @FXML private TableColumn<InvoiceTableDto, String> colEstadoPendiente;
    @FXML private TableColumn<InvoiceTableDto, String> colDescripcionPendiente;
    @FXML private TableColumn<InvoiceTableDto, Double> colMontoPendiente;
    @FXML private TableColumn<InvoiceTableDto, LocalDateTime> colFechaPendiente;
    @FXML private TableColumn<InvoiceTableDto, String> colArchivoPendiente;

    @FXML private TableView<InvoiceTableDto> tblPagadas;
    @FXML private TableColumn<InvoiceTableDto, String> colEstadoPagada;
    @FXML private TableColumn<InvoiceTableDto, String> colDescripcionPagada;
    @FXML private TableColumn<InvoiceTableDto, Double> colMontoPagada;
    @FXML private TableColumn<InvoiceTableDto, LocalDateTime> colFechaPagada;
    @FXML private TableColumn<InvoiceTableDto, String> colArchivoPagada;

    @FXML
    private void initialize() {
        String totalBilled = String.format("%.2f", infoInvoiceService.getTotalBilled());
        String totalPaid = String.format("%.2f", infoInvoiceService.getTotalPaid());
        String totalPaymentDue = String.format("%.2f", infoInvoiceService.getPaymentDue());
        String invoiceCount = String.valueOf(infoInvoiceService.getInvoiceCount());

        lblTotalFacturado.setText(totalBilled);
        lblTotalPagado.setText(totalPaid);
        lblTotalPendiente.setText(totalPaymentDue);
        lblCantidadFacturas.setText(invoiceCount);

        initTable(tblTodas, colEstadoTodas, colDescripcionTodas, colMontoTodas, colPorPagarTodas, colFechaTodas, colArchivoTodas);
    }

    private void initTable(TableView<InvoiceTableDto> table,
                           TableColumn<InvoiceTableDto, String> colEstado,
                           TableColumn<InvoiceTableDto, String> colDescripcion,
                           TableColumn<InvoiceTableDto, Double> colMonto,
                           TableColumn<InvoiceTableDto, Double> colPorPagar,
                           TableColumn<InvoiceTableDto, LocalDateTime> colFecha,
                           TableColumn<InvoiceTableDto, String> colArchivo) {
        initCombo(colEstado);
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("mount"));
        colPorPagar.setCellValueFactory(new PropertyValueFactory<>("porPagar"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("date"));
        colArchivo.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        colMonto.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("$ %.2f", value));
            }
        });

        colPorPagar.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("$ %.2f", value));
            }
        });

        colFecha.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : value.toLocalDate() + " " + value.toLocalTime());
            }
        });

        var invoices = invoiceService.FindAllToTable();
        ObservableList<InvoiceTableDto> data = FXCollections.observableArrayList(invoices);
        table.setItems(data);
    }

    private void initCombo(TableColumn<InvoiceTableDto, String> colEstado) {
        var statusList = statusService.findByGroup("invoice");
        ObservableList<String> status = FXCollections.observableArrayList(
                statusList.stream()
                        .map(s -> s.getName() + " - " + s.getCode())
                        .toList());

        colEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEstado.setCellFactory(column -> new TableCell<InvoiceTableDto, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>(status);
            {
                comboBox.setMinWidth(120);
                comboBox.setOnAction(event -> {
                    InvoiceTableDto factura = getTableView().getItems().get(getIndex());
                    if (factura != null) {
                        factura.setStatus(comboBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(String valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(valor);
                    setGraphic(comboBox);
                }
            }
        });
    }

    @FXML
    public void onAddInvoice(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NewInvoiceView.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage dialog = new Stage();
            dialog.setTitle("New invoice");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(addNewInvoice.getScene().getWindow());
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.show();
        } catch (IOException e) {
            log.error("No se pudo abrir la ventana, error msg: " + e.getCause() + "\n stackTrace: \n" + Arrays.toString(e.getStackTrace()));
            showAlert(Alert.AlertType.ERROR,
                    "No se pudo abrir la ventana",
                    "Error: no se pudo abrir la ventana para generar nueva factura",
                    null);
        }

    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
