package views.administrador;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.PerfilController;
import controllers.UsuarioController;
import database.DatabaseConnection;
import entities.Perfil;
import entities.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.App;
import views.MainController;
import database.DatabaseConnection;
import javafx.stage.FileChooser;

public class CrearBackupController {

    @FXML
    private Button bCerrar;

    private MainController mainController = new MainController();

    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @FXML
    private void backupDatabase() throws URISyntaxException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar dirección donde guardar el Backup");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String backupPath = selectedDirectory.getAbsolutePath() + "\\TicketSuitBackup.bak";
            String backupQuery = "BACKUP DATABASE TicketSuit TO DISK = '" + backupPath + "'";

            try (Connection connection = databaseConnection.connect();
                    Statement statement = connection.createStatement()) {

                statement.executeUpdate(backupQuery);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Backup Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("Archivo .bak creado en: " + backupPath + " \n A continuacion, se reiniciara la aplicacion.");
                alert.showAndWait();

                App.restartApplication();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Backup Fallido");
                alert.setHeaderText("Ocurrió un error durante la creación del Backup");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void restoreDatabase() {
        String restorePath = "C:\\Backups\\TicketSuitBackup.bak";
        String restoreQuery = "RESTORE DATABASE TicketSuit FROM DISK = '" + restorePath + "'";

        try (Connection connection = databaseConnection.connect()) {
            String useMasterQuery = "USE master";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(useMasterQuery);
            }

            String setSingleUserQuery = "ALTER DATABASE TicketSuit SET SINGLE_USER WITH ROLLBACK IMMEDIATE";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(setSingleUserQuery);
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(restoreQuery);
            }

            String setMultiUserQuery = "ALTER DATABASE TicketSuit SET MULTI_USER";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(setMultiUserQuery);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Restauración Exitosa");
            alert.setHeaderText(null);
            alert.setContentText("La base de datos ha sido restaurada con éxito.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en la Restauración");
            alert.setHeaderText("Ocurrió un error al restaurar la base de datos");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }
}
