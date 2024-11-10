package views.vendedor;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.swing.Action;

import controllers.PeliculaController;
import entities.Pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.MainController;
import views.SceneManager;

public class CarteleraController {

    @FXML
    private Button bFiltrar;

    @FXML
    private Label lCartelera;

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

    @FXML
    void filtrarPeliculas(ActionEvent event) {
        if (dFecha.getValue() == null || tPelicula.getText() == "") {
            mostrarMensajeError("Debe llenar ambos campos para filtrar");
        } else {
            cargarPeliculas();
        }
    }

    @FXML
    void limpiarFiltro(ActionEvent event) {
        tPelicula.setText("");
        dFecha.setValue(null);
        cargarPeliculas();
    }

    void cargarPeliculas() {
        List<Pelicula> peliculas;
        spPeliculas.getChildren().clear();

        if (!tPelicula.getText().isEmpty() && dFecha.getValue() != null) {
            peliculas = peliculaController.findByNombreFecha(tPelicula.getText(), Date.valueOf(dFecha.getValue()));
            if (peliculas.size() == 0) {
                mostrarMensajeError("No hay peliculas con esas especificaciones");
                peliculas = peliculaController.findAll();
            }
        } else {
            peliculas = peliculaController.findAll();
        }
        

        int totalPeliculas = peliculas.size();
        double panelWidth = 120;
        double panelHeight = 177;
        double gap = 20;
        int panelsPerRow = 3;
        int i = 0;

        int rows = (int) Math.ceil((double) totalPeliculas / panelsPerRow);
        double totalHeight = gap + rows * (panelHeight + gap);

        spPeliculas.setPrefHeight(Math.max(totalHeight, 539));

        for (Pelicula pelicula : peliculas) {
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);

            // Panel de imagen
            Pane panel = new Pane();
            panel.setPrefWidth(panelWidth);
            panel.setPrefHeight(panelHeight);
            panel.getStyleClass().add("cartelera");

            ImageView thumbnail = new ImageView();
            thumbnail.setFitWidth(panelWidth);
            thumbnail.setFitHeight(panelHeight - 2);
            thumbnail.setPreserveRatio(true);
            thumbnail.setImage(new Image(pelicula.getImagen()));
            thumbnail.setLayoutX(2);
            thumbnail.setLayoutY(1);

            panel.getChildren().add(thumbnail);

            VBox vboxInfo = new VBox(5);
            vboxInfo.setAlignment(Pos.TOP_LEFT);

            Label lNombre = new Label(pelicula.getNombre());
            lNombre.getStyleClass().add("nombre-pelicula");
            Label lSinopsis = new Label(pelicula.getSinopsis());
            lSinopsis.setWrapText(true);
            lSinopsis.setMaxWidth(200);

            vboxInfo.getChildren().addAll(lNombre, lSinopsis);

            panel.setOnMouseClicked(event -> seleccionPelicula(pelicula, event));

            hbox.getChildren().addAll(panel, vboxInfo);

            int column = i % panelsPerRow;
            int row = i / panelsPerRow;

            double positionX = gap + column * (panelWidth + 250);
            double positionY = gap + row * (panelHeight + gap);

            AnchorPane.setLeftAnchor(hbox, positionX);
            AnchorPane.setTopAnchor(hbox, positionY);

            spPeliculas.getChildren().add(hbox);
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

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
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
