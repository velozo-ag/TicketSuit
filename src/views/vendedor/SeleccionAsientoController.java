package views.vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import controllers.SalaController;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import views.MainController;

public class SeleccionAsientoController {

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Pane panelAsientos;

    @FXML
    private Label tNombrePelicula;

    MainController mainController = new MainController();
    SalaController salaController = new SalaController();

    public Pelicula pelicula;
    public Sala_Funcion funcion;
    public int cantidadAsientos;

    @FXML
    void toPVendedor(ActionEvent event) {
        try {
            mainController.toPVendedor(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValores(Pelicula pelicula, Sala_Funcion funcion, int cantidadTickets) {
        this.pelicula = pelicula;
        this.funcion = funcion;
        this.cantidadAsientos = cantidadTickets;
        mostrarAsientos();
    }

    private void mostrarAsientos() {
        panelAsientos.getChildren().clear(); // Limpia panel antes de añadir nuevos asientos

        int maxSeleccionados = cantidadAsientos; // Máximo de asientos que se pueden seleccionar
        List<CheckBox> asientosSeleccionados = new ArrayList<>(); // Lista para almacenar asientos seleccionados

        Sala sala = salaController.findById(funcion.getId_sala());
        int asientoWidth = 18;
        int asientoHeight = 14;
        int anchoPanel = 887;
        int altoPanel = 400;

        int filas = sala.getCantFilas();
        int columnas = sala.getCantColumnas();

        System.out.println(filas + " " + columnas);

        Image asientoImage = new Image("@../../Resources/Asiento.png");

        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPrefSize(anchoPanel, altoPanel);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                CheckBox boton = new CheckBox();
                boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                        "-fx-background-size: cover;-fx-background-color: none;\r\n" + //
                        "    -fx-padding: 0;");
                boton.getStyleClass().add("asiento-button");
                boton.setPrefWidth(asientoWidth);
                boton.setPrefHeight(asientoHeight);

                boton.setOnMouseEntered(
                        event -> {
                            if (!boton.isSelected()) {
                                boton.setStyle("-fx-background-image: url('@../../Resources/AsientoHover.png'); " +
                                        "-fx-background-size: cover;-fx-background-color: none;\r\n" + //
                                        "    -fx-padding: 0;");
                            }
                        });

                boton.setOnMouseExited(event -> {
                    if (!boton.isSelected()) {
                        boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                                "-fx-background-size: cover;-fx-background-color: none;\r\n" + //
                                "    -fx-padding: 0;");
                    }
                });

                boton.setOnAction(event -> {
                    if (boton.isSelected()) {
                        if (asientosSeleccionados.size() < maxSeleccionados) {
                            boton.setStyle("-fx-background-image: url('@../../Resources/AsientoSeleccionado.png'); " +
                                    "-fx-background-size: cover;-fx-background-color: none;\r\n" + //
                                    "    -fx-padding: 0;");
                            asientosSeleccionados.add(boton);
                        } else {
                            boton.setSelected(false);
                        }
                    } else {
                        boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                                "-fx-background-size: cover;");
                        asientosSeleccionados.remove(boton);
                    }
                });

                grid.add(boton, j, i);
            }
        }

        panelAsientos.getChildren().add(grid);
    }

}
