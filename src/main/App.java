package main;

import java.io.IOException;

import controllers.SalaController;
import entities.Sala;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/gerente/PanelGerente.fxml"));
        scene = new Scene(root);

        root.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Ticket Suit");
        primaryStage.setScene(scene);
        primaryStage.show();

        // SalaController salaController = new SalaController();
        // Sala sala = new Sala();
        // sala.setIdCine(1);
        // sala.setNombre("SALA 1");
        // sala.setCantFilas(7);
        // sala.setCantColumnas(8);
        // sala.setCapacidad(sala.getCantColumnas() * sala.getCantFilas());
        // salaController.createSala(sala);

        // sala.setIdCine(1);
        // sala.setNombre("SALA 2");
        // sala.setCantFilas(8);
        // sala.setCantColumnas(10);
        // sala.setCapacidad(sala.getCantColumnas() * sala.getCantFilas());
        // salaController.createSala(sala);
        
        // sala.setIdCine(1);
        // sala.setNombre("SALA 3");
        // sala.setCantFilas(8);
        // sala.setCantColumnas(12);
        // sala.setCapacidad(sala.getCantColumnas() * sala.getCantFilas());
        // salaController.createSala(sala);
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
