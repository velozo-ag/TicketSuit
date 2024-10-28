package views.administrador;

import java.io.IOException;
import java.sql.Date;

import controllers.FuncionController;
import entities.Funcion;
import entities.Pelicula;
import entities.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import views.MainController;

public class ABMFunciones {

    @FXML
    private TableView<Funcion> tablaFunciones;

    @FXML
    private TableView<Sala> tablaSalas;
    
    @FXML
    private TableColumn<Sala, String> colNombreSala;

    @FXML
    private TableColumn<Funcion, Integer> colIdFuncion;

    @FXML
    private TableColumn<Funcion, String> colInicio;

    @FXML
    private TableColumn<Funcion, String> colFinal;

    @FXML
    private TableColumn<Funcion, Pelicula> colPelicula;

    @FXML
    private TableColumn<Funcion, Integer> colTipoFuncion;

    @FXML
    private Pane mainPanel;

    private AdministracionController administracionController = new AdministracionController();
    private MainController mainController = new MainController();
    private FuncionController funcionController = new FuncionController();

    public void initialize() {
        colIdFuncion.setCellValueFactory(new PropertyValueFactory<>("idFuncion"));
        colPelicula.setCellValueFactory(new PropertyValueFactory<>("nombrePelicula"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("final"));
        colTipoFuncion.setCellValueFactory(new PropertyValueFactory<>("tipoFuncion"));

        cargarFunciones();
    }

    private void cargarFunciones() {
        ObservableList<Funcion> funciones = FXCollections.observableArrayList(funcionController.findAll());
        tablaFunciones.setItems(funciones);
    }

    @FXML
    void ABMPeliculas(ActionEvent event) {
        administracionController.ABMPeliculas(event);
    }

    @FXML
    void ABMUsuarios(ActionEvent event) {
        administracionController.ABMUsuarios(event);
    }

    @FXML
    void ABMSalas(ActionEvent event) {
        administracionController.ABMSalas(event);
    }

    @FXML
    void formularioFuncion(ActionEvent event) {
        mostrarMensajeError("Proximamente");
    }

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
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

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("-");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
