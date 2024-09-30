import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.AsientoManager;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Ticket Suit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // AsientoManager.insertarAsientos(); // Podes desactivar si ya pusiste los asientos
        launch(args);
    }

    /*
     * 
     * public static void insertarAsientos() {
     * DatabaseConnection dbController = new DatabaseConnection();
     * Connection connection = dbController.connect();
     * 
     * String insertSQL =
     * "INSERT INTO asiento (id_asiento, letra_fila, numero_columna, id_sala) VALUES (?, ?, ?, ?)"
     * ;
     * int totalAsientos = 56;
     * int columnas = 8;
     * int filas = totalAsientos / columnas;
     * 
     * char[] letrasFilas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
     * 
     * try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
     * 
     * connection.setAutoCommit(false);
     * 
     * int idSala = 1;
     * int idAsiento = 1;
     * int count = 0;
     * 
     * for (int i = 1; i <= filas; i++) {
     * char fila = letrasFilas[i-1];
     * 
     * for (int columna = 1; columna <= columnas; columna++) {
     * pstmt.setInt(1, idAsiento);
     * pstmt.setString(2, String.valueOf(fila));
     * pstmt.setInt(3, columna);
     * pstmt.setInt(4, idSala);
     * pstmt.addBatch();
     * count++;
     * idAsiento++;
     * 
     * if (count == totalAsientos) {
     * break;
     * }
     * }
     * if (count == totalAsientos) {
     * break;
     * }
     * }
     * 
     * pstmt.executeBatch();
     * connection.commit(); // Hacer commit de la transacción
     * System.out.println("Asientos insertados exitosamente!");
     * 
     * } catch (SQLException e) {
     * e.printStackTrace();
     * }
     * 
     * }
     */

    // Pane panel = (Pane) scene.lookup("#mainPanel");

    // scene.widthProperty().addListener((observable, oldValue, newValue) -> {
    // double stageWidth = newValue.doubleValue();
    // AnchorPane.setLeftAnchor(panel, (stageWidth - panel.getPrefWidth()) / 2);
    // AnchorPane.setRightAnchor(panel, (stageWidth - panel.getPrefWidth()) / 2);
    // });

    // scene.heightProperty().addListener((observable, oldValue, newValue) -> {
    // double stageHeight = newValue.doubleValue();
    // AnchorPane.setTopAnchor(panel, 20.0);
    // AnchorPane.setTopAnchor(panel, (stageHeight - panel.getPrefHeight()) / 2);
    // });

}
