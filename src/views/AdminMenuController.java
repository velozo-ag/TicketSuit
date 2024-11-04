package views;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminMenuController {

    MainController mainController = new MainController();

    @FXML
    void toBackUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/CrearBackup.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
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
