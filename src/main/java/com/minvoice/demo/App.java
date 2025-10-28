package com.minvoice.demo;

import com.gluonhq.charm.glisten.control.Avatar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.JarURLConnection;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class App extends Application {
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        context = new SpringApplicationBuilder(SpringApplication.class)
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        var glistenCss = Objects.requireNonNull(
                Avatar.class.getResource("/com/gluonhq/charm/glisten/control/glisten.css")
        ).toExternalForm();
        scene.getStylesheets().add(glistenCss);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/views/modern-table.css")).toExternalForm()
        );


        stage.setScene(scene);
        stage.setTitle("Management invoices");
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
    }
}
