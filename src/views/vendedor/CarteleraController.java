package views.vendedor;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.Action;

import controllers.PeliculaController;
import controllers.Sala_FuncionController;
import entities.Pelicula;
import entities.Sala_Funcion;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
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
    private Pane panelFechas;

    @FXML
    private AnchorPane spPeliculas;

    @FXML
    private TextField tPelicula;

    private MainController mainController = new MainController();
    private PeliculaController peliculaController = new PeliculaController();
    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();

    private int posicionFecha = 0;

    @FXML
    public void initialize() {
        cargarPeliculasPorDia(Date.valueOf(LocalDate.now()));
        cargarDias(posicionFecha);
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
    void adelantarFechas(ActionEvent event) {
        List<Date> dias = sala_FuncionController.findDias();
        if (dias.size() > 5) {
            posicionFecha += 5;
            cargarDias(posicionFecha);
        }

        if (dias.size() < posicionFecha) {
            posicionFecha = 0;
            cargarDias(posicionFecha);
        }
    }

    @FXML
    void atrasarFechas(ActionEvent event) {
        // List<Date> dias = sala_FuncionController.findDias();

        if (posicionFecha <= 0) {
            posicionFecha = 0;
            cargarDias(posicionFecha);
        } else {
            posicionFecha -= 5;
            cargarDias(posicionFecha);
        }
    }

    void cargarPeliculasPorDia(Date fecha) {
        List<Pelicula> peliculas;
        spPeliculas.getChildren().clear();
        peliculas = peliculaController.findByFecha(fecha);

        int totalPeliculas = peliculas.size();
        double panelWidth = 120;
        double panelHeight = 177;
        double gap = 20;
        int panelsPerRow = 3;
        int i = 0;

        int rows = (int) Math.ceil((double) totalPeliculas / panelsPerRow);
        double totalHeight = gap + rows * (panelHeight + gap);

        spPeliculas.setPrefHeight(Math.max(totalHeight, 479));

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

            panel.setOnMouseClicked(event -> seleccionPelicula(pelicula, fecha, event));

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

    void cargarDias(int inicio) {
        List<Date> dias = sala_FuncionController.findDias();
        int gap = 10;
        int buttonWidth = 77;
        int buttonHeight = 75;
        int initial_posX = 97;
        int initial_posY = 8;
        int cant = 0;

        ToggleGroup toggleGroup = new ToggleGroup();

        if (dias.size() == 0) {
            System.out.println("No hay funciones programdas");
            return;
        }

        panelFechas.getChildren().clear();

        if (dias.size() < 5 || dias.size() - inicio < 5) {
            for (int i = inicio; i < dias.size(); i++) {
                ToggleButton boton = new ToggleButton(diaToString(dias.get(i)).toUpperCase());
                boton.setPrefWidth(buttonWidth);
                boton.getStyleClass().add("botonFecha");
                boton.setWrapText(true);
                boton.setTextAlignment(TextAlignment.CENTER);
                boton.setPrefHeight(buttonHeight);
                boton.setLayoutX(initial_posX + cant * (buttonWidth + gap));
                boton.setLayoutY(initial_posY);

                boton.setToggleGroup(toggleGroup);

                Date fecha = dias.get(i);
                boton.onActionProperty().set(c -> cargarPeliculasPorDia(fecha));
                panelFechas.getChildren().add(boton);

                cant++;
            }
        } else if (dias.size() > 5) {
            for (int i = inicio; i < inicio + 5; i++) {
                ToggleButton boton = new ToggleButton(diaToString(dias.get(i)).toUpperCase());
                boton.setPrefWidth(buttonWidth);
                boton.getStyleClass().add("botonFecha");
                boton.setWrapText(true);
                boton.setTextAlignment(TextAlignment.CENTER);
                boton.setPrefHeight(buttonHeight);
                boton.setLayoutX(initial_posX + cant * (buttonWidth + gap));
                boton.setLayoutY(initial_posY);

                boton.setToggleGroup(toggleGroup);

                Date fecha = dias.get(i);
                boton.onActionProperty().set(c -> cargarPeliculasPorDia(fecha));
                panelFechas.getChildren().add(boton);

                cant++;
            }
        }

    }

    String diaToString(Date dia) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE dd", new Locale("es", "ES"));
        return formatter.format(dia);
    }

    void seleccionPelicula(Pelicula pelicula, Date fecha, MouseEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/PeliculaSeleccionada.fxml"));
            root = loader.load();

            PeliculaSeleccionadaController peliculaSeleccionadaController = loader.getController();
            peliculaSeleccionadaController.setPelicula(pelicula);
            peliculaSeleccionadaController.setFecha(fecha);

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
