package com.minvoice.demo.desktop.controllers;

import com.minvoice.demo.application.services.interfaces.IInfoInvoiceService;
import com.minvoice.demo.application.services.interfaces.IXmlInvoiceReader;
import com.minvoice.demo.application.services.interfaces.IXmlReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@CommonsLog
public class MainViewController {
    private final ApplicationContext context;
    private final IInfoInvoiceService infoInvoiceService;
    private final IXmlInvoiceReader service;

    @FXML
    private Button addNewInvoice;
    @FXML
    private Label lblTotalFacturado;
    @FXML
    private Label lblTotalPagado;
    @FXML
    private Label lblTotalPendiente;
    @FXML
    private Label lblCantidadFacturas;

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
