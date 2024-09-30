package views.administrador;

import java.io.IOException;
import java.sql.Date;

import controllers.FuncionController;
import entities.Funcion;
import entities.Pelicula;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import views.MainController;

public class ABMFunciones {

    @FXML
    private TableView<Funcion> tablaFunciones;

    @FXML
    private TableColumn<Funcion, Date> colFecha;
    
    @FXML
    private TableColumn<Funcion, String> colHoraFinal;
    
    @FXML
    private TableColumn<Funcion, String> colHoraInicio;
    
    @FXML
    private TableColumn<Funcion, Integer> colIdFuncion;
    
    @FXML
    private TableColumn<Funcion, Void> colDesactivar;

    @FXML
    private TableColumn<Funcion, Void> colModificar;

    @FXML
    private TableColumn<Funcion, Pelicula> colPelicula;

    @FXML
    private TableColumn<Funcion, ?> colSala;

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
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
        colHoraFinal.setCellValueFactory(new PropertyValueFactory<>("hora_final"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTipoFuncion.setCellValueFactory(new PropertyValueFactory<>("tipoFuncion"));
        // colSala.setCellValueFactory(new PropertyValueFactory<>("sala"));

        // asignarBotones();
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

}
