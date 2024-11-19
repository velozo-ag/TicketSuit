package main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
    
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        String initialView;
    
        try {
            if (!dbConnection.checkDatabaseExists()) {
                System.out.println("Base de datos no encontrada. Cargando ventana de restauración...");
                initialView = "/views/Restore.fxml";
            } else {
                if (!dbConnection.isConnected()) {
                    dbConnection.connect(); 
                }
                if (!dbConnection.hasAdminUser()) {
                    System.out.println("No hay usuarios administradores. Cargando ventana de restauración...");
                    initialView = "/views/Restore.fxml";
                } else {
                    System.out.println("Base de datos lista. Cargando login...");
                    initialView = "/views/Login.fxml";
                }
            }

            Parent root = FXMLLoader.load(getClass().getResource(initialView));
            scene = new Scene(root);
            root.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
       
        } catch (Throwable e) {
            e.printStackTrace();
        }
    
    
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Ticket Suit");
        primaryStage.getIcons().add(new Image("/Resources/LogoThumbnail.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void restartApplication() throws URISyntaxException {
        primaryStage.close();

        try {
            String javaBin = System.getProperty("java.home") + "/bin/java"; 
            File currentJar = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            if (currentJar.getName().endsWith(".jar")) {
                ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", currentJar.getPath());
                builder.start();
            } else {
                ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", System.getProperty("java.class.path"), App.class.getName());
                builder.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}