package views;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class RestoreController {

    private DatabaseConnection databaseConnection;

    @FXML
    private PasswordField passwordField;

    public RestoreController() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public void restoreDatabaseR() {
        String password = passwordField.getText();
        if (!password.equals("restore123")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Contraseña Incorrecta");
            alert.setHeaderText("La contraseña no es correcta");
            alert.setContentText("Por favor, ingresa la contraseña correcta.");
            alert.showAndWait();
            return;
        }
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Backup", "*.bak"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
    
        if (selectedFile != null) {
            String backupPath = selectedFile.getAbsolutePath();
    
            String createDatabaseQuery = "CREATE DATABASE TicketSuit;";
            String setSingleUserQuery = "ALTER DATABASE TicketSuit SET SINGLE_USER WITH ROLLBACK IMMEDIATE;";
            String restoreQuery = "RESTORE DATABASE TicketSuit FROM DISK = '" + backupPath + "' WITH REPLACE;";
            String setMultiUserQuery = "ALTER DATABASE TicketSuit SET MULTI_USER;";
    
            // intentar conectarse y verificar si la base de datos existe
            try (Connection connection = databaseConnection.connect();
                 Statement statement = connection.createStatement()) {
    
                // switch a master
                connection.setCatalog("master");
    
                // verificar si existe la bd ts
                String checkDatabaseQuery = "SELECT database_id FROM sys.databases WHERE name = 'TicketSuit';";
                ResultSet resultSet = statement.executeQuery(checkDatabaseQuery);
    
                if (!resultSet.next()) {
                    System.out.println("La base de datos 'TicketSuit' no existe. Creando la base de datos...");
                    statement.executeUpdate(createDatabaseQuery);
                } else {
                    System.out.println("La base de datos 'TicketSuit' ya existe.");
                }
    
                // restaurar
                System.out.println("Restaurando la base de datos 'TicketSuit'...");
                statement.executeUpdate(setSingleUserQuery);
                statement.executeUpdate(restoreQuery);
                statement.executeUpdate(setMultiUserQuery);
    
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Restauración Exitosa");
                alert.setHeaderText("La restauración se realizo exitosamente. Reinicie la app para finalizar.");
                alert.setContentText("La base de datos ha sido restaurada correctamente desde: " + backupPath);
                alert.showAndWait();
                Platform.exit();
    
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al Restaurar");
                alert.setContentText("Ocurrió un error al intentar restaurar la base de datos: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
    