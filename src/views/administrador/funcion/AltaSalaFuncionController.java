package views.administrador.funcion;

import controllers.FuncionController;
import controllers.HorarioController;
import controllers.PeliculaController;
import controllers.SalaController;
import controllers.Sala_FuncionController;
import entities.Funcion;
import entities.Horario;
import entities.Pelicula;
import entities.Sala;
import entities.Sala_Funcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AltaSalaFuncionController {

    @FXML
    private Button bCerrar;

    @FXML
    private ComboBox<Funcion> cFuncion;

    @FXML
    private ComboBox<Horario> cHorario;

    @FXML
    private ComboBox<Sala> cSala;

    @FXML
    private DatePicker dFecha;

    @FXML
    private TextField tFinal;

    private SalaController salaController = new SalaController();
    private HorarioController horarioController = new HorarioController();
    private FuncionController funcionController = new FuncionController();
    private PeliculaController peliculaController = new PeliculaController();

    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();

    Timestamp inicioFuncion;
    Timestamp finalFuncion;

    @FXML
    private void initialize() {

        // COMO HAGO ESTE LISTENER
        cSala.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cFuncion.getValue() != null) {
                cargarHorarios();
                cHorario.setDisable(false);
            }
        });

        cFuncion.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cSala.getValue() != null) {
                cargarHorarios();
                cHorario.setDisable(false);
            }
        });

        cHorario.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                calcularHorarioFinal();
            }
        });

        cHorario.setDisable(true);
        cargarFunciones();
        cargarSalas();
    }

    @FXML
    void altaSalaFuncion(ActionEvent event) {
        
        if (verificarCampos()) {
            Sala_Funcion sala_Funcion = new Sala_Funcion();
            sala_Funcion.setId_funcion(cFuncion.getValue().getId_funcion());
            sala_Funcion.setId_sala(cSala.getValue().getIdSala());
            sala_Funcion.setInicioFuncion(inicioFuncion);
            sala_Funcion.setFinalFuncion(finalFuncion);

            sala_FuncionController.createSalaFuncion(sala_Funcion);

            mensajeExito("Funcion asignada a Sala con exito");
            cerrarFormulario(event);
        }
    }

    private void cargarFunciones() {
        ObservableList<Funcion> funciones = FXCollections.observableArrayList(funcionController.findAll());
        cFuncion.setItems(funciones);
    }

    private void cargarSalas() {
        ObservableList<Sala> salas = FXCollections.observableArrayList(salaController.findAll());
        cSala.setItems(salas);
    }

    private void cargarHorarios() {
        int duracion = peliculaController.findById(cFuncion.getValue().getId_pelicula()).getDuracion();

        ObservableList<Horario> horarios = FXCollections
                .observableArrayList(horarioController.findBySalaDuracion(cSala.getValue().getIdSala(), duracion, Date.valueOf(dFecha.getValue())));
        cHorario.setItems(horarios);
    }

    private void calcularHorarioFinal() {
        int duracion = peliculaController.findById(cFuncion.getValue().getId_pelicula()).getDuracion();
        LocalTime horarioInicio = cHorario.getValue().getHorario().toLocalTime();

        LocalTime horarioFinal = horarioInicio.plusMinutes(duracion);
        tFinal.setText(horarioFinal + ":00");
    }

    @FXML
    void cerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    public boolean verificarCampos() {

        if (cFuncion.getValue() == null || cSala.getValue() == null || dFecha.getValue() == null
                || cHorario.getValue() == null) {
            mensajeError("Debe rellenar todos los campos.");
            return false;
        }

        Funcion funcion = cFuncion.getValue();
        Sala sala = cSala.getValue();
        Date fecha = Date.valueOf(dFecha.getValue());

        if (fecha.before(Date.valueOf(LocalDate.now().plusDays(1)))) {
            mensajeError("Debe ingresar una fecha posterior a hoy.");
            dFecha.setValue(LocalDate.now().plusDays(1));
            return false;
        }

        // Settea el inicio de la funcion (dia y hora)
        inicioFuncion = Timestamp.valueOf(fecha.toLocalDate().format(DateTimeFormatter.ISO_DATE).toString() + " "
                + cHorario.getValue().getHorario());

        // Verifica si comienza hoy y termina ma;ana
        if (verificaDiferenciaDias()) {
            finalFuncion = Timestamp
                    .valueOf(fecha.toLocalDate().plusDays(1).format(DateTimeFormatter.ISO_DATE).toString()
                            + " " + tFinal.getText());

        } else {
            finalFuncion = Timestamp
                    .valueOf(fecha.toLocalDate().format(DateTimeFormatter.ISO_DATE).toString()
                            + " " + tFinal.getText());
        }

        return true;
    }

    public boolean verificaDiferenciaDias() {
        boolean iniciaAntes = cHorario.getValue().getHorario().after(Time.valueOf("13:00:00"))
                && cHorario.getValue().getHorario().before(Time.valueOf("23:59:00"));
        boolean terminaDespues = Time.valueOf(tFinal.getText()).after(Time.valueOf("00:00:00"))
                && Time.valueOf(tFinal.getText()).before(Time.valueOf("02:00:00"));

        return iniciaAntes && terminaDespues;
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
