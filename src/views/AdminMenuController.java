package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class AdminMenuController {

    MainController mainController = new MainController();

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
    void ABMSalas(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/administrador/ABMSalas.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
