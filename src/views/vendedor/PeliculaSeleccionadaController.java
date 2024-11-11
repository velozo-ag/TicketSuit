package views.vendedor;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import controllers.FuncionController;
import controllers.Sala_FuncionController;
import controllers.TipoFuncionController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Funcion;
import entities.Pelicula;
import entities.Sala_Funcion;
import entities.TipoFuncion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.MainController;
import views.SceneManager;

public class PeliculaSeleccionadaController {

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Pane panelImagen;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tSinopsis;

    @FXML
    private Label lPrecio;

    @FXML
    private TextField tCantidad;

    private FuncionController funcionController = new FuncionController();
    private TipoFuncionController tipoFuncionController = new TipoFuncionController();
    private MainController mainController = new MainController();
    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();
    public Pelicula pelicula;
    public Date fecha;
    public Sala_Funcion funcionSeleccionada = null;
    public List<Sala_Funcion> funciones;

    @FXML
    void initialize() {
        lPrecio.setText("0");
        tCantidad.setText("1");

        tCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tCantidad.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!newValue.isEmpty()) {

                int cantidad = Integer.parseInt(newValue);

                if (cantidad > 16) {
                    tCantidad.setText("16");
                    mensajeError("No hay tantos asientos disponibles");
                }

                if (cantidad <= 0) {
                    tCantidad.setText("1");
                }

                if (funcionSeleccionada == null) {
                    tCantidad.setText("1");
                    mensajeError("Seleccione una funcion");
                }

            }
        });
    }

    @FXML
    void continuarCompra(ActionEvent event) {
        if (funcionSeleccionada == null) {
            mensajeError("Seleccione un horario para continuar");
            return;
        }

        if (tCantidad.getText().isEmpty()) {
            mensajeError("Ingrese una cantidad de tickets valida para continuar");
            return;
        }

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/SeleccionAsiento.fxml"));
            root = loader.load();

            SeleccionAsientoController seleccionAsientoController = loader.getController();
            seleccionAsientoController.setValores(pelicula, funcionSeleccionada, Integer.parseInt(tCantidad.getText()));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPVendedor(ActionEvent event) {
        try {
            mainController.toPVendedor(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        tNombrePelicula.setText(pelicula.getNombre());

        ImageView thumbnail = new ImageView();
        thumbnail.setFitWidth(panelImagen.getPrefWidth());
        thumbnail.setFitHeight(panelImagen.getPrefHeight() - 2);
        thumbnail.setPreserveRatio(true);
        thumbnail.setImage(new Image(pelicula.getImagen()));
        thumbnail.setLayoutX(1);
        thumbnail.setLayoutY(1);

        panelImagen.getChildren().add(thumbnail);

        tSinopsis.setText(pelicula.getSinopsis());
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
        cargarFunciones();
    }

    private void cargarFunciones() {
        double PosY = 337;
        double ColPos2D = 546;
        double ColPos3D = 705;
        double ColPosIMAX = 873;

        GridPane gridPane2D = new GridPane();
        GridPane gridPane3D = new GridPane();
        GridPane gridPaneIMAX = new GridPane();

        gridPane2D.setLayoutX(ColPos2D);
        gridPane2D.setLayoutY(PosY);
        gridPane3D.setLayoutX(ColPos3D);
        gridPane3D.setLayoutY(PosY);
        gridPaneIMAX.setLayoutX(ColPosIMAX);
        gridPaneIMAX.setLayoutY(PosY);

        ToggleGroup toggleGroup = new ToggleGroup();
        funciones = sala_FuncionController.findByPeliculaFecha(pelicula.getIdPelicula(), fecha);

        for (Sala_Funcion funcion : funciones) {
            TipoFuncion tipoFuncion = tipoFuncionController
                    .findById(funcionController.findById(funcion.getId_funcion()).getId_tipoFuncion());

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            ToggleButton boton = new ToggleButton(formatter.format(funcion.getInicioFuncion()));
            boton.getStyleClass().add("horarioPelicula");
            boton.setToggleGroup(toggleGroup);
            gridPane3D.setMargin(boton, new Insets(2));

            boton.onActionProperty().set(c -> seleccionFuncion(funcion));

            switch (tipoFuncion.getIdTipoFuncion()) {
                case 1:
                    gridPane2D.add(boton, 0, gridPane2D.getRowCount());
                    break;
                case 2:
                    gridPane3D.add(boton, 0, gridPane3D.getRowCount());
                    break;
                case 3:
                    gridPaneIMAX.add(boton, 0, gridPaneIMAX.getRowCount());
                    break;

            }
        }

        mainPanel.getChildren().addAll(gridPane2D, gridPane3D, gridPaneIMAX);

    }

    void seleccionFuncion(Sala_Funcion funcion) {
        this.funcionSeleccionada = funcion;
        lPrecio.setText(Integer.parseInt(tCantidad.getText())
                * funcionController.findById(funcion.getId_funcion()).getTipoFuncion().getPrecio() + "");
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
