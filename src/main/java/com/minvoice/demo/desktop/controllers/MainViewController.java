package com.minvoice.demo.desktop.controllers;

import com.minvoice.demo.application.services.dto.InvoiceTableDto;
import com.minvoice.demo.application.services.interfaces.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
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
    private final IFileService fileService;

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
    @FXML private TableColumn<InvoiceTableDto, Void> colOpcionesTodas;

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
        initActionsColumn(tblTodas, colOpcionesTodas);
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
        colArchivo.setCellFactory(col -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink();

            {
                link.setTextFill(Color.web("#007bff"));
                link.setUnderline(true);

                link.setOnAction(e -> {
                    InvoiceTableDto row = getTableView().getItems().get(getIndex());
                    if (row == null) return;

                    File file = resolveFileFor(row);
                    var response = fileService.openInFileExplorer(file);
                    if(!response.isSuccessfully()) {
                        showAlert(Alert.AlertType.ERROR, "Error al abrir carpeta", response.message(), null);
                    }
                });
            }

            @Override
            protected void updateItem(String fileName, boolean empty) {
                super.updateItem(fileName, empty);
                if (empty || fileName == null || fileName.isBlank()) {
                    setGraphic(null);
                    setText(null);
                } else {
                    link.setText(fileName);
                    setGraphic(link);
                    setText(null);
                }
            }
        });

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

    private void initActionsColumn(TableView<InvoiceTableDto> table,
                                   TableColumn<InvoiceTableDto, Void> colOpciones) {

        colOpciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnAddPay = new Button("Pay 💵");
            private final Button btnDelete = new Button("Del ⛔");
            private final HBox buttonsBox = new HBox(8, btnAddPay, btnDelete);

            {
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.setPadding(new Insets(3));
                buttonsBox.setFillHeight(true);
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                // ===== Estilos generales =====
                String baseStyle = """
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                -fx-background-radius: 4;
                -fx-cursor: hand;
                -fx-padding: 3 10 3 10;
            """;

                // ===== Botón "Pagar" (verde) =====
                btnAddPay.setStyle(baseStyle + "-fx-background-color: #28a745;");
                btnAddPay.setTooltip(new Tooltip("Registrar pago de esta factura"));
                btnAddPay.setOnMouseEntered(e -> btnAddPay.setStyle(baseStyle + "-fx-background-color: #218838;"));
                btnAddPay.setOnMouseExited(e -> btnAddPay.setStyle(baseStyle + "-fx-background-color: #28a745;"));

                // ===== Botón "Eliminar" (rojo) =====
                btnDelete.setStyle(baseStyle + "-fx-background-color: #dc3545;");
                btnDelete.setTooltip(new Tooltip("Eliminar factura"));
                btnDelete.setOnMouseEntered(e -> btnDelete.setStyle(baseStyle + "-fx-background-color: #c82333;"));
                btnDelete.setOnMouseExited(e -> btnDelete.setStyle(baseStyle + "-fx-background-color: #dc3545;"));

                // ===== Acciones =====
                btnAddPay.setOnAction(e -> {
                    InvoiceTableDto factura = getTableView().getItems().get(getIndex());
                    if (factura != null) {
                        onAddPayment(factura);
                    }
                });

                btnDelete.setOnAction(e -> {
                    InvoiceTableDto factura = getTableView().getItems().get(getIndex());
                    if (factura == null) return;

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Seguro que deseas eliminar esta factura?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.setHeaderText(null);
                    confirm.setTitle("Confirmación");
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                invoiceService.deleteById(factura.getIdInvoice());
                                getTableView().getItems().remove(factura);
                                refreshTotals();
                            } catch (Exception ex) {
                                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar", ex.getMessage());
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        colOpciones.setMinWidth(160);
        colOpciones.setPrefWidth(180);
    }



    private void onAddPayment(InvoiceTableDto row) {
        showAlert(Alert.AlertType.INFORMATION, "Pago", null, "Abrir modal de pago para factura ID: " + row.getIdInvoice());
    }

    private void refreshTotals() {
        lblTotalFacturado.setText(String.format("%.2f", infoInvoiceService.getTotalBilled()));
        lblTotalPagado.setText(String.format("%.2f", infoInvoiceService.getTotalPaid()));
        lblTotalPendiente.setText(String.format("%.2f", infoInvoiceService.getPaymentDue()));
        lblCantidadFacturas.setText(String.valueOf(infoInvoiceService.getInvoiceCount()));
    }

    private File resolveFileFor(InvoiceTableDto row) {
        return new File(row.getFilePath());
    }

}
