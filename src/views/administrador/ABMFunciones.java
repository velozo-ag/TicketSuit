package views.administrador;

import java.io.IOException;
import java.sql.Date;

import controllers.FuncionController;
import controllers.SalaController;
import controllers.Sala_FuncionController;
import entities.Funcion;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private AdministracionController administracionController = new AdministracionController();
    private MainController mainController = new MainController();
    private FuncionController funcionController = new FuncionController();
    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();
    private SalaController salaController = new SalaController();

    private Sala salaSeleccionada;

    public void initialize() {
        // NO HAY IDFUNCION PORQUE ES SALAFUNCION Y TIENE ID COMPUESTO
        colIdFuncion.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPelicula.setCellValueFactory(new PropertyValueFactory<>("pelicula"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("horaFinal"));
        colTipoFuncion.setCellValueFactory(new PropertyValueFactory<>("tipoFuncion"));

        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaSalas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSalaSeleccionada(newValue);
                updateTablaFunciones();
            }
        });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/funcion/AltaSalaFuncion.fxml"));
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

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("-");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
