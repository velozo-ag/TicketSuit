package views.vendedor;

import java.io.IOException;
import java.util.List;

import javax.swing.Action;

import controllers.PeliculaController;
import entities.Pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import views.MainController;

public class CarteleraController {

    @FXML
    private Button bFiltrar;

    @FXML
    private DatePicker dFecha;

    @FXML
    private Pane mainPanel;

    @FXML
    private AnchorPane spPeliculas;

    @FXML
    private TextField tPelicula;

    private MainController mainController = new MainController();
    private PeliculaController peliculaController = new PeliculaController();

    @FXML
    public void initialize() {
        cargarPeliculas();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toTickets(ActionEvent event) {
        // Parent root;
        // try {
        // root =
        // FXMLLoader.load(getClass().getResource("/views/vendedor/HistorialTickets.fxml"));
        // Scene scene = new Scene(root);
        // Pane panel = (Pane) scene.lookup("#mainPanel");
        // mainController.setUpScene(event, panel, scene);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        mostrarMensajeError("Proximamente");
    }

    void cargarPeliculas() {
        List<Pelicula> peliculas = peliculaController.findAll();
        int totalPeliculas = peliculas.size();

        double panelWidth = 280;
        double panelHeight = 414;
        double gap = 14.8;
        int panelsPerRow = 4;
        int i = 0;

        int rows = (int) Math.ceil((double) totalPeliculas / panelsPerRow);
        double totalHeight = gap + rows * (panelHeight + gap);

        if (totalHeight < 539) {
            totalHeight = 539;
        }

        spPeliculas.setPrefHeight(totalHeight);

        for (Pelicula pelicula : peliculas) {
            Pane panel = new Pane();
            panel.setPrefWidth(panelWidth);
            panel.setPrefHeight(panelHeight);
            panel.getStyleClass().add("cartelera");

            ImageView thumbnail = new ImageView();
            thumbnail.setFitWidth(panelWidth); 
            thumbnail.setFitHeight(panelHeight - 1);
            thumbnail.setPreserveRatio(true); 
            thumbnail.setImage(new Image(pelicula.getImagen())); 
            thumbnail.setLayoutX(2);
            thumbnail.setLayoutY(1);

            panel.getChildren().add(thumbnail);

            int column = i % panelsPerRow;
            int row = i / panelsPerRow;

            double positionX = gap + column * (panelWidth + gap);
            double positionY = gap + row * (panelHeight + gap);

            AnchorPane.setLeftAnchor(panel, positionX);
            AnchorPane.setTopAnchor(panel, positionY);

            panel.setOnMouseClicked(event -> {
                seleccionPelicula(pelicula, event);
            });

            spPeliculas.getChildren().add(panel);
            i++;
        }
    }

    void seleccionPelicula(Pelicula pelicula, MouseEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/PeliculaSeleccionada.fxml"));
            root = loader.load();

            PeliculaSeleccionadaController peliculaSeleccionadaController = loader.getController();
            peliculaSeleccionadaController.setPelicula(pelicula);

            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(new ActionEvent(event.getSource(), event.getTarget()), panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("-");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
