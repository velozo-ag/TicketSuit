package views.administrador.funcion;

import java.sql.Date;
import java.time.LocalDate;

import controllers.FuncionController;
import controllers.PeliculaController;
import controllers.TipoFuncionController;
import entities.Funcion;
import entities.Pelicula;
import entities.Perfil;
import entities.TipoFuncion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class AltaFuncionController {

    @FXML
    private Button bCerrar;

    @FXML
    private ComboBox<Pelicula> cPelicula;

    @FXML
    private ComboBox<TipoFuncion> cTipoFuncion;

    @FXML
    private DatePicker dFechaIngreso;

    @FXML
    private DatePicker dFechaFinal;

    private PeliculaController peliculaController = new PeliculaController();
    private TipoFuncionController tipoFuncionController = new TipoFuncionController();
    private FuncionController funcionController = new FuncionController();

    @FXML
    public void initialize() {
        cargarPeliculas();
        cargarTipoFuncion();
        //setupDatePickers(dFechaIngreso, dFechaFinal);
        configurarDatePickers();
    }

    @FXML
    void altaFuncion(ActionEvent event) {
        if (verificarCampos()) {
            Funcion funcion = new Funcion();
            funcion.setId_pelicula(cPelicula.getValue().getIdPelicula());
            funcion.setId_tipoFuncion(cTipoFuncion.getValue().getIdTipoFuncion());
            funcion.setFechaIngreso(Date.valueOf(dFechaIngreso.getValue()));
            funcion.setFechaFinal(Date.valueOf(dFechaFinal.getValue()));

            funcionController.createFuncion(funcion);

            mensajeExito("Funcion creada con exito");
            cerrarFormulario(event);
        }
    }

    @FXML
    void cerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarPeliculas() {
        ObservableList<Pelicula> peliculas = FXCollections.observableArrayList(peliculaController.findAll());
        cPelicula.setItems(peliculas);
    }

    private void cargarTipoFuncion() {
        ObservableList<TipoFuncion> tiposFuncion = FXCollections.observableArrayList(tipoFuncionController.findAll());
        cTipoFuncion.setItems(tiposFuncion);
    }

    private void configurarDatePickers() {
        LocalDate hoy = LocalDate.now();

        dFechaIngreso.setValue(hoy);

        // Restricciones en dFechaIngreso
        dFechaIngreso.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(hoy));
            }
        });

        // Listener para dFechaIngreso
        dFechaIngreso.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dFechaFinal.setValue(newValue.plusDays(3)); // Configurar un valor inicial
                dFechaFinal.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newValue.plusDays(3)));
                    }
                });
            }
        });

        // Restricciones en dFechaFinal
        dFechaFinal.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(dFechaIngreso.getValue() == null ? hoy : dFechaIngreso.getValue().plusDays(3)));
            }
        });
    }

    private boolean verificarCampos() {
        if (dFechaIngreso.getValue() == null || dFechaFinal.getValue() == null || cPelicula.getValue() == null || cTipoFuncion.getValue() == null) {
            mensajeError("Debe rellenar todos los campos.");
            return false;
        }

        LocalDate fechaIngreso = dFechaIngreso.getValue();
        LocalDate fechaFinal = dFechaFinal.getValue();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaIngresoMas3 = fechaIngreso.plusDays(3);

        if (fechaIngreso.isBefore(fechaActual) || fechaFinal.isBefore(fechaActual)) {
            mensajeError("No se puede ingresar una fecha anterior a la actual.");
            return false;
        }

        if (fechaFinal.isBefore(fechaIngreso)) {
            mensajeError("La fecha final de la función no puede ser anterior a su fecha de ingreso.");
            return false;
        }

        if (fechaFinal.isBefore(fechaIngresoMas3)) {
            mensajeError("El período de una función no puede ser menor a 3 días.");
            return false;
        }

        return true;
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
