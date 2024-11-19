package views.vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Action;

import controllers.AsientoController;
import controllers.SalaController;
import entities.Asiento;
import entities.Compra;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import entities.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    public int maxSeleccionados;

    private Compra compra = new Compra();

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
        if (asientosSelec.size() != maxSeleccionados) {
            mensajeError("Debe seleccionar todos los Asientos antes de continuar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/ConfirmarVenta.fxml"));
            Parent root = loader.load();
            ConfirmacionVentaController confirmacionVentaController = loader.getController();
            confirmacionVentaController.setValores(compra, pelicula, funcion, asientosSelec);

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                if (confirmacionVentaController.getCompra()) {
                    mostrarResumenCompra(event);
                } else {
                    irACartelera(event);
                }
            });

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void irACartelera(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/Cartelera.fxml"));
            root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarResumenCompra(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/ResumenVenta.fxml"));
            root = loader.load();
            ResumenVentaController resumenVentaController = loader.getController();
            resumenVentaController.setValores(compra, pelicula, funcion, asientosSelec);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValores(Compra compra, Pelicula pelicula, Sala_Funcion funcion, int cantidadTickets) {
        this.pelicula = pelicula;
        this.funcion = funcion;
        this.cantidadAsientos = cantidadTickets;
        this.compra = compra;

        mostrarAsientos();
    }

    private void mostrarAsientos() {
        panelAsientos.getChildren().clear(); 

        maxSeleccionados = cantidadAsientos;
        List<CheckBox> bAsientosSeleccionados = new ArrayList<>();
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
            // Para salto de fila
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
                boton.setStyle(setAsientoStyle("Asiento"));
                boton.setOnMouseEntered(event -> {
                    if (!boton.isSelected()) {
                        boton.setStyle(setAsientoStyle("AsientoHover"));
                    }
                });
                boton.setOnMouseExited(event -> {
                    if (!boton.isSelected()) {
                        boton.setStyle(setAsientoStyle("Asiento"));
                    }
                });
                boton.setOnAction(event -> {
                    if (boton.isSelected()) {
                        if (bAsientosSeleccionados.size() < maxSeleccionados) {
                            boton.setStyle(setAsientoStyle("AsientoSeleccionado"));
                            boton.setGraphic(null);
                            bAsientosSeleccionados.add(boton);
                            asientosSelec.add(asiento);
                        } else {
                            boton.setSelected(false);
                        }
                    } else {
                        boton.setStyle(setAsientoStyle("Asiento"));
                        bAsientosSeleccionados.remove(boton);
                        asientosSelec.remove(asiento);
                    }
                });

            } else {
                boton.setStyle(setAsientoStyle("AsientoOcupado"));
                boton.setDisable(true);
            }

            grid.add(boton, asiento.getNumeroColumna(), fila);
        }

        panelAsientos.getChildren().add(grid);
    }

    private String setAsientoStyle(String asiento) {
        return "-fx-background-image: url('@../../Resources/" + asiento
                + ".png'); -fx-background-size: cover; -fx-padding: 0;";
    }

    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
