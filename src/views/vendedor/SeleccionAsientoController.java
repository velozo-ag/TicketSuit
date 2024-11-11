package views.vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import controllers.AsientoController;
import controllers.SalaController;
import entities.Asiento;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.MainController;
import views.SceneManager;

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
    AsientoController asientoController = new AsientoController();

    public Pelicula pelicula;
    public Sala_Funcion funcion;
    public int cantidadAsientos;
    public List<Asiento> asientosSelec = new ArrayList<>();

    @FXML
    void volverPelicula(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/PeliculaSeleccionada.fxml"));
            root = loader.load();

            PeliculaSeleccionadaController peliculaSeleccionadaController = loader.getController();
            peliculaSeleccionadaController.setPelicula(pelicula);
            peliculaSeleccionadaController.setFecha(funcion.getFecha());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void confirmarAsientos(ActionEvent event) {
        System.out.println("confirmar");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/ConfirmarVenta.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            // stage.setOnHidden(e -> cargarSalas());

            stage.showAndWait();

        } catch (Exception e) {
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

        int maxSeleccionados = cantidadAsientos;
        List<CheckBox> asientosSeleccionados = new ArrayList<>();
        List<Asiento> asientos = asientoController.findBySala(funcion.getId_sala());

        Sala sala = salaController.findById(funcion.getId_sala());
        int asientoWidth = 30;
        int asientoHeight = 23;
        int anchoPanel = 887;
        int altoPanel = 400;

        int fila = 0;
        String filaAnterior = "";

        int filas = sala.getCantFilas();
        int columnas = sala.getCantColumnas();

        System.out.println(filas + " " + columnas);

        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPrefSize(anchoPanel, altoPanel);
        grid.setAlignment(Pos.CENTER);

        for (Asiento asiento : asientos) {
            if (!asiento.getLetraFila().equals(filaAnterior) && !filaAnterior.equals("")) {
                fila++;
                filaAnterior = asiento.getLetraFila();
            } else {
                filaAnterior = asiento.getLetraFila();
            }

            CheckBox boton = new CheckBox();
            boton.getStyleClass().add("asiento-button");
            boton.setPrefWidth(asientoWidth);
            boton.setPrefHeight(asientoHeight);
            
            if (asientoController.asientoDesocupado(asiento.getIdAsiento(), funcion)) {
                boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                        "-fx-background-size: cover;" +
                        "-fx-padding: 0;");

                boton.setOnMouseEntered(
                        event -> {
                            if (!boton.isSelected()) {
                                boton.setStyle("-fx-background-image: url('@../../Resources/AsientoHover.png'); " +
                                        "-fx-background-size: cover;" +
                                        "-fx-padding: 0;");
                            }
                        });

                boton.setOnMouseExited(event -> {
                    if (!boton.isSelected()) {
                        boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                                "-fx-background-size: cover;" +
                                "-fx-padding: 0;");
                    }
                });

                boton.setOnAction(event -> {
                    if (boton.isSelected()) {
                        if (asientosSeleccionados.size() < maxSeleccionados) {
                            boton.setStyle("-fx-background-image: url('@../../Resources/AsientoSeleccionado.png'); " +
                                    "-fx-background-size: cover;" +
                                    "-fx-z-index: 2;" +
                                    "-fx-padding: 0;");
                            boton.setGraphic(null);
                            asientosSeleccionados.add(boton);
                        } else {
                            boton.setSelected(false);
                        }
                    } else {
                        boton.setStyle("-fx-background-image: url('@../../Resources/Asiento.png'); " +
                                "-fx-background-size: cover;" +
                                "-fx-padding: 0;");
                        asientosSeleccionados.remove(boton);
                    }
                });
            }else{
                boton.setStyle("-fx-background-image: url('@../../Resources/AsientoOcupado.png'); " +
                        "-fx-background-size: cover;" +
                        "-fx-padding: 0;");
                boton.setDisable(true);
            }

            grid.add(boton, asiento.getNumeroColumna(), fila);
        }

        panelAsientos.getChildren().add(grid);
    }

}
