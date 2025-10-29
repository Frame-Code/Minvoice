package com.minvoice.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@CommonsLog
public class MainViewController {
    private final ApplicationContext context;

    @FXML
    private Button addNewInvoice;

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
            log.error("No se pudo abrir la ventana, error msg: " + e.getMessage() + "stackTrace: " + Arrays.toString(e.getStackTrace()));
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
