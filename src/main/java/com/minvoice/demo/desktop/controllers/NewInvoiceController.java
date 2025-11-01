package com.minvoice.demo.desktop.controllers;

import com.minvoice.demo.application.services.dto.InvoiceDto;
import com.minvoice.demo.application.services.dto.InvoiceFileDto;
import com.minvoice.demo.application.services.interfaces.IGeneralStatusService;
import com.minvoice.demo.application.services.interfaces.IInvoiceService;
import com.minvoice.demo.application.services.interfaces.IItemService;
import com.minvoice.demo.application.services.interfaces.IXmlInvoiceReader;
import com.minvoice.demo.domain.model.enums.TypeInvoice;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@CommonsLog
public class NewInvoiceController {
    private final ApplicationContext context;
    private final IXmlInvoiceReader reader;
    private final IItemService itemService;
    private final IGeneralStatusService statusService;
    private final IInvoiceService invoiceService;

    private InvoiceFileDto invoiceDto;
    private File selectedPdfFile;
    private File selectedXmlFile;

    @FXML private StackPane dropZone;
    @FXML private StackPane dropZoneXml;
    @FXML private Label lblDropZone;
    @FXML private Label lblDropZoneExaminar;
    @FXML private Label lblDropZoneXml;
    @FXML private Label lblDropZoneXmlExaminar;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtMonto;
    @FXML private ComboBox cbEstado;
    @FXML private ComboBox cbTipo;
    @FXML private Button btnGuardar;
    @FXML private TextField txtObservacion;
    @FXML private TextField txtDescripcion;


    @FXML
    public void saveInvoice(ActionEvent actionEvent) {
        if(selectedPdfFile == null || selectedXmlFile == null || txtMonto.getText().isEmpty() || txtMonto.getText().isBlank() ||
        dpFecha.getValue() == null || txtDescripcion.getText().isBlank() || txtDescripcion.getText().isEmpty() || invoiceDto.itemsCode().isEmpty()) {
            showMessage("No se pudo crear la factura, faltan datos por llenar", Alert.AlertType.ERROR);
            return;
        }
        TypeInvoice type = TypeInvoice.valueOf(cbTipo.getValue().toString());
        String status = cbEstado.getValue().toString().split("-")[1].trim();
        InvoiceDto invoice = new InvoiceDto(
                invoiceDto.noInvoice(),
                type,
                status,
                txtDescripcion.getText(),
                txtObservacion.getText(),
                invoiceDto.issueDate(),
                invoiceDto.itemsCode(),
                selectedPdfFile,
                selectedXmlFile

        );
        invoiceService.save(invoice);
        showMessage("Factura creada correctamente", Alert.AlertType.INFORMATION);


    }

    private void loadInvoiceInfo(String filePath) {
        invoiceDto = reader.read(filePath);
        var itemPrice = itemService.findTotalPricesByCodes(invoiceDto.itemsCode());
        if(itemPrice.isEmpty()) {
            showMessage("No se puedo encontrar los items de la factura ", Alert.AlertType.ERROR);
            selectedPdfFile = null;
            dropZoneXml.setStyle("-fx-border-color: none; -fx-border-width: none;");
            lblDropZoneXml.setText("Arrastra y suelta aquí el XML");
            lblDropZoneXmlExaminar.setText("o haz click aquí para buscar manualmente");
            return;
        }
        dpFecha.setValue(invoiceDto.issueDate().toLocalDate());
        String totalItemsPrice = String.format("%.2f", itemPrice.get());
        txtMonto.setText(totalItemsPrice + "$");
    }

    private void configureDragAndDrop() {
        dropZone.setOnDragOver(this::onDragOver);
        dropZone.setOnDragDropped(this::onDragDropped);

        dropZoneXml.setOnDragDropped(this::onDragDropped);
        dropZoneXml.setOnDragDropped(this::onDragDroppedXml);
    }

    private void showMessage(String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        configureDragAndDrop();
        var statusList = statusService.findByGroup("invoice");
        if(statusList.isEmpty()) {
            showMessage("No se puedo encontrar estados de factura", Alert.AlertType.ERROR);
            btnGuardar.setDisable(true);
            return;
        }
        btnGuardar.setDisable(false);
        List<String> statusName = statusList
                .stream()
                .map(status -> status.getName() + " - " + status.getCode())
                .toList();
        Optional<String> statusDefault = statusList
                .stream()
                .filter(status -> status.getCode().trim().equalsIgnoreCase("pp"))
                .findFirst()
                .map(status -> status.getName() + " - " + status.getCode());

        cbEstado.getItems().setAll(statusName);
        statusDefault.ifPresent(status -> cbEstado.setValue(status));
        cbEstado.setValue(statusName.stream().findFirst().get());

        List<String> typeInvoices = Arrays.stream(TypeInvoice.values())
                .map(Enum::name)
                .toList();

        cbTipo.getItems().setAll(typeInvoices);
        cbTipo.setValue(cbTipo.getItems().get(0));


    }

    @FXML
    public void onDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            if (file.getName().toLowerCase().endsWith(".pdf")) {
                selectedPdfFile = file;
                dropZone.setStyle("-fx-border-color: green; -fx-border-width: 2;");
                showMessage("Archivo cargado: " + file.getName(), Alert.AlertType.INFORMATION);
                lblDropZone.setText("Factura cargada: " + file.getName());
                lblDropZoneExaminar.setText(null);
                success = true;
            } else {
                showMessage("Solo se permiten archivos PDF.", Alert.AlertType.WARNING);
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    @FXML
    public void onDragOver(DragEvent dragEvent) {
        var db = dragEvent.getDragboard();
        if (db.hasFiles() && db.getFiles().get(0).getName().toLowerCase().endsWith(".pdf")) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }
    @FXML
    public void onDragDroppedXml(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            if (file.getName().toLowerCase().endsWith(".xml")) {
                selectedXmlFile = file;
                dropZoneXml.setStyle("-fx-border-color: green; -fx-border-width: 2;");
                showMessage("Archivo cargado: " + file.getName(), Alert.AlertType.INFORMATION);
                lblDropZoneXml.setText("XML cargado: " + file.getName());
                lblDropZoneXmlExaminar.setText(null);
                success = true;
                loadInvoiceInfo(file.getPath());
            } else {
                showMessage("Solo se permiten archivos XML.", Alert.AlertType.WARNING);
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    @FXML
    public void onDragOverXml(DragEvent dragEvent) {
        var db = dragEvent.getDragboard();
        if (db.hasFiles() && db.getFiles().get(0).getName().toLowerCase().endsWith(".xml")) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }


    public String chooseFile(Event event, String description, String format) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, format));
        File file = chooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            selectedPdfFile = file;
            dropZone.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            showMessage("Archivo seleccionado: " + file.getName(), Alert.AlertType.INFORMATION);
            return file.getName();
        }
        return null;
    }

    @FXML
    public void closeDialog(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClicXml(MouseEvent mouseEvent) {
        String fileName = chooseFile(mouseEvent, "ARCHIVOS XML", "*.xml");
        if(fileName != null) {
            dropZoneXml.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            lblDropZoneXml.setText("XML cargado: " + fileName);
            lblDropZoneXmlExaminar.setText(null);
        }
    }

    @FXML
    public void onClickPdf(MouseEvent mouseEvent) {
        String fileName = chooseFile(mouseEvent, "ARCHIVOS PDF", "*.pdf");
        if(fileName != null) {
            dropZone.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            lblDropZone.setText("Factura cargada: " + fileName);
            lblDropZoneExaminar.setText(null);
        }
    }

}
