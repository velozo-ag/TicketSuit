package views.administrador;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.PerfilController;
import controllers.UsuarioController;
import database.DatabaseConnection;
import entities.Perfil;
import entities.Usuario;
import javafx.application.Platform;
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
import views.MainController;
import database.DatabaseConnection;
import javafx.stage.FileChooser;

public class CrearBackupController {

    @FXML
    private Button bCerrar;

    private MainController mainController = new MainController();

    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @FXML
    private void backupDatabase() {
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
                alert.setContentText("Archivo .bak creado en: " + backupPath);
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Backup Fallido");
                alert.setHeaderText("Ocurrió un error durante la creación del Backup");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void restoreDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Backup", "*.bak"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String backupPath = selectedFile.getAbsolutePath();
            String setSingleUserQuery = "ALTER DATABASE TicketSuit SET SINGLE_USER WITH ROLLBACK IMMEDIATE;";
            String restoreQuery = "RESTORE DATABASE TicketSuit FROM DISK = '" + backupPath + "' WITH REPLACE;";
            String setMultiUserQuery = "ALTER DATABASE TicketSuit SET MULTI_USER;";

            try (Connection connection = databaseConnection.connect();
                Statement statement = connection.createStatement()) {

                // Cambiar a la base de datos master
                connection.setCatalog("master");

                // Cerrar conexiones activas
                statement.executeUpdate(setSingleUserQuery);

                // Restaurar la base de datos
                statement.executeUpdate(restoreQuery);

                // Volver a modo multiusuario
                statement.executeUpdate(setMultiUserQuery);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Restauración Exitosa");
                alert.setHeaderText("La restauración se realizo exitosamente. Reinicie la app para finalizar.");
                alert.setContentText("La base de datos ha sido restaurada desde: " + backupPath);
                alert.showAndWait();
                Platform.exit();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Restauración Fallida");
                alert.setHeaderText("Ocurrió un error durante la restauración de la base de datos");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }
}
