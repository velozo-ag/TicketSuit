package views;

import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.util.Set;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

    private Stage stage;
    private Scene scene;

    @FXML
    public void toLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        scene = new Scene(root);
        Pane panel = (Pane) scene.lookup("#mainPanel");

        setUpScene(event, panel, scene);
    }

    @FXML
    public void toPAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/administrador/PanelAdministrador.fxml"));
        scene = new Scene(root);
        Pane panel = (Pane) scene.lookup("#mainPanel");

        setUpScene(event, panel, scene);
    }

    @FXML
    public void toPGerente(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/gerente/PanelGerente.fxml"));
        scene = new Scene(root);
        Pane panel = (Pane) scene.lookup("#mainPanel");

        setUpScene(event, panel, scene);
    }

    @FXML
    public void toPVendedor(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/vendedor/Cartelera.fxml"));
        scene = new Scene(root);
        Pane panel = (Pane) scene.lookup("#mainPanel");

        setUpScene(event, panel, scene);
    }

    @FXML
    private void centerPane(Pane pane, Scene scene) {
        double stageWidth = scene.getWidth();
        double stageHeight = scene.getHeight();

        AnchorPane.setLeftAnchor(pane, (stageWidth - pane.getPrefWidth()) / 2);
        AnchorPane.setRightAnchor(pane, (stageWidth - pane.getPrefWidth()) / 2);
        AnchorPane.setTopAnchor(pane, (stageHeight - pane.getPrefHeight()) / 2);
        AnchorPane.setBottomAnchor(pane, (stageHeight - pane.getPrefHeight()) / 2);
    }

    @FXML 
    public void setUpScene(ActionEvent event, Pane pane, Scene scene) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.setScene(scene);
        // stage.setMaximized(true);

        centerPane(pane, scene);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            centerPane(pane, scene);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            centerPane(pane, scene);
        });

        // Pane mainButton = (Pane) scene.lookup("#mainButton");
        // configureButtonHoverEffect(mainButton);
    }

    public void configureButtonHoverEffect(Pane button) {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setNode(button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.05); // Escalar 5% más grande en X
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.05); // Escalar 5% más grande en Y
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        scaleTransition.setDuration(Duration.millis(200)); // Duración de la transición

        ScaleTransition scaleTransitionReverse = new ScaleTransition();
        scaleTransitionReverse.setNode(button);
        scaleTransitionReverse.setFromX(1.05); // Escalar 5% más grande en X
        scaleTransitionReverse.setToX(1.0); // Volver a tamaño original en X
        scaleTransitionReverse.setFromY(1.05); // Escalar 5% más grande en Y
        scaleTransitionReverse.setToY(1.0); // Volver a tamaño original en Y
        scaleTransitionReverse.setCycleCount(1);
        scaleTransitionReverse.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        scaleTransitionReverse.setDuration(Duration.millis(200)); // Duración de la transición

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransitionReverse.play();
        });
    }

}
