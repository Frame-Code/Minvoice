package com.minvoice.demo.desktop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@CommonsLog
public class NewInvoiceController {
    private final ApplicationContext context;
    @FXML
    private StackPane dropZone;
    @FXML
    private Label lblDropZone;
    @FXML
    private Label lblDropZoneExaminar;
    @FXML
    private Label lblArchivo;
    @FXML
    private Button btnCancelar;

    private File selectedFile;

    private void configureDragAndDrop() {
        dropZone.setOnDragOver(this::onDragOver);
        dropZone.setOnDragDropped(this::onDragDropped);
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
    }

    @FXML
    public void onDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            if (file.getName().toLowerCase().endsWith(".pdf")) {
                selectedFile = file;
                dropZone.setStyle("-fx-border-color: green; -fx-border-width: 2;");
                showMessage("Archivo cargado: " + file.getName(), Alert.AlertType.INFORMATION);
                lblDropZone.setText("Archivo cargado: " + file.getName());
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
        if(dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    @FXML
    public void chooseFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File file = chooser.showOpenDialog(((Button) actionEvent.getSource()).getScene().getWindow());

        if (file != null) {
            selectedFile = file;
            dropZone.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            showMessage("Archivo seleccionado: " + file.getName(), Alert.AlertType.INFORMATION);
            lblArchivo.setText("Archivo seleccionado: " + file.getName());
        }
    }

    @FXML
    public void closeDialog(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void saveInvoice(ActionEvent actionEvent) {
        showMessage("Se va guardar la factura", Alert.AlertType.INFORMATION);
    }
}
