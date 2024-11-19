package views;

import java.io.IOException;

import entities.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.App;

public class MainController {

    private Stage stage;
    private Scene scene;

    @FXML
    public void toLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.setScene(root, stage);
        
        UserSession.getInstance().cerrarSession();
    }

    @FXML
    public void toPAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/administrador/ABMUsuarios.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.setScene(root, stage);
    }

    @FXML
    public void toPGerente(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/gerente/PanelGerente.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.setScene(root, stage);
    }

    @FXML
    public void toPVendedor(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/vendedor/Cartelera.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.setScene(root, stage);
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

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        } catch (ClassCastException e) {
            System.out.println("No es un boton");
            try {
                stage = (Stage) ((Pane) event.getSource()).getScene().getWindow();
            } catch (ClassCastException ex) {
                System.out.println("No es panel");
            }
        }
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        // stage.setMaximized(true);

        centerPane(pane, scene);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            centerPane(pane, scene);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            centerPane(pane, scene);
        });
    }

}
