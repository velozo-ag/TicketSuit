package views.administrador;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import controllers.AsientoController;
import controllers.FuncionController;
import controllers.SalaController;
import controllers.Sala_FuncionController;
import controllers.TicketController;
import entities.Asiento;
import entities.Funcion;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.MainController;

public class ABMFunciones {

    @FXML
    private TableView<Sala_Funcion> tablaFunciones;

    @FXML
    private TableView<Sala> tablaSalas;

    @FXML
    private TableColumn<Sala, String> colNombreSala;

    @FXML
    private TableColumn<Sala_Funcion, Integer> colIdFuncion;

    @FXML
    private TableColumn<Sala_Funcion, String> colInicio;

    @FXML
    private TableColumn<Sala_Funcion, String> colFinal;

    @FXML
    private TableColumn<Sala_Funcion, Pelicula> colPelicula;

    @FXML
    private TableColumn<Sala_Funcion, Integer> colTipoFuncion;

    @FXML
    private Pane mainPanel;

    @FXML
    private Pane panelFuncion;

    @FXML
    private Pane panelDatos;

    @FXML
    private Pane panelAsientos;

    @FXML
    private Label tAsientosLibres;

    @FXML
    private Label tAsientosOcupados;

    private MainController mainController = new MainController();
    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();
    private SalaController salaController = new SalaController();
    private AsientoController asientoController = new AsientoController();
    private TicketController ticketController = new TicketController();

    private Sala salaSeleccionada;
    private Sala_Funcion funcionSeleccionada;

    public void initialize() {
        colIdFuncion.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPelicula.setCellValueFactory(new PropertyValueFactory<>("pelicula"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("horaFinal"));
        colTipoFuncion.setCellValueFactory(new PropertyValueFactory<>("tipoFuncion"));

        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaSalas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSalaSeleccionada(newValue);
            }
        });

        tablaFunciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFuncionSeleccionada(newValue);
            }
        });

        panelDatos.setVisible(false);
        cargarSalas();
    }

    private void cargarSalas() {
        ObservableList<Sala> salas = FXCollections.observableArrayList(salaController.findAll());
        tablaSalas.setItems(salas);
    }

    private void updateSalaSeleccionada(Sala sala) {
        this.salaSeleccionada = sala;
        updateTablaFunciones();
    }

    private void updateFuncionSeleccionada(Sala_Funcion funcion) {
        this.funcionSeleccionada = funcion;
        updatePanelFuncion();
    }

    private void updatePanelFuncion() {
        tAsientosOcupados.setText(ticketController.cantVendidoPorFuncion(funcionSeleccionada) + "");
        tAsientosLibres.setText(salaSeleccionada.getCapacidad() - ticketController.cantVendidoPorFuncion(funcionSeleccionada) + "");
        panelDatos.setVisible(true);
        mostrarAsientos();
    }

    private void updateTablaFunciones() {
        ObservableList<Sala_Funcion> funciones = FXCollections
                .observableArrayList(sala_FuncionController.findByIdSala(salaSeleccionada.getIdSala()));
        tablaFunciones.setItems(funciones);
    }

    @FXML
    void formularioFuncion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/funcion/AltaFuncion.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> cargarSalas());

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void formularioSalaFuncion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/administrador/funcion/AltaSalaFuncion.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> cargarSalas());

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPAdmin(ActionEvent event) {
        try {
            mainController.toPAdmin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAsientos() {
        panelAsientos.getChildren().clear();

        List<CheckBox> bAsientosSeleccionados = new ArrayList<>();
        List<Asiento> asientos = asientoController.findBySala(funcionSeleccionada.getId_sala());

        Sala sala = salaController.findById(funcionSeleccionada.getId_sala());
        int asientoWidth = 22;
        int asientoHeight = 18;
        int anchoPanel = 302;
        int altoPanel = 246;

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

            if (asientoController.asientoDesocupado(asiento.getIdAsiento(), funcionSeleccionada)) {
                boton.setStyle(setAsientoStyle("Asiento"));
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

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("-");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
