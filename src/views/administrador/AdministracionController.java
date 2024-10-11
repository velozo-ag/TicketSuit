package views.administrador;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.MainController;
import views.SceneManager;

public class AdministracionController {

    @FXML
    private Pane mainPanel;

    private MainController mainController = new MainController();

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ABMPeliculas(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/administrador/ABMPeliculas.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ABMUsuarios(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/administrador/ABMUsuarios.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ABMFunciones(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/administrador/ABMFunciones.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ABMSalas(ActionEvent event) {
        // Parent root;
        // try {
        // root =
        // FXMLLoader.load(getClass().getResource("/views/administrador/ABMSalas.fxml"));
        // Scene scene = new Scene(root);
        // Pane panel = (Pane) scene.lookup("#mainPanel");
        // mainController.setUpScene(event, panel, scene);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        mostrarMensajeError("Proximamente");
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("-");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
