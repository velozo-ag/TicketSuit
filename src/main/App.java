package main;
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

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        scene = new Scene(root);
        
        root.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Ticket Suit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Esto solamente cambia el contenido del root - Recibe la ruta del archivo FXML
    public static void setRoot(String route) throws IOException {
        Parent newRoot = FXMLLoader.load(App.class.getResource(route));
        scene.setRoot(newRoot);
        System.out.println(scene.getStylesheets());
    }

    public static void main(String[] args) {
        launch(args);
    }

}