package views.administrador.sala;

import controllers.AsientoController;
import controllers.SalaController;
import entities.Sala;
import entities.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ModificarSalaController {

    @FXML
    private Button bCerrar;

    @FXML
    private Pane formPanel;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private Pane panelAsientos;

    @FXML
    private TextField tCapacidad;

    @FXML
    private TextField tColumnas;

    @FXML
    private TextField tFilas;

    @FXML
    private TextField tNombre;

    public Sala sala;
    private SalaController salaController = new SalaController();
    private AsientoController asientoController = new AsientoController();

    public void initialize() {
        mostrarAsientos();

        tFilas.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tFilas.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!newValue.isEmpty()) {

                int row = Integer.parseInt(newValue);

                if (row > 16) {
                    tFilas.setText("16");
                    mensajeError("La cantidad de filas no puede ser mayor a 16.");
                }

                if (row <= 0) {
                    tFilas.setText("1");
                }

                tCapacidad.setText(String.valueOf(row * Integer.parseInt(tColumnas.getText())));
                mostrarAsientos();
            }
        });
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {

    }

    @FXML
    void modificarSala(ActionEvent event) {
        boolean res = mensajeConfirmacion("Desea modificar sala " + sala.getNombre() + "?");

        if (res) {
            if (verificarCampos()) {
                Sala sala = new Sala();
                sala.setNombre(tNombre.getText());
                sala.setCantColumnas(Integer.parseInt(tColumnas.getText()));
                sala.setCantFilas(Integer.parseInt(tFilas.getText()));
                sala.setCapacidad(sala.getCantColumnas() * sala.getCantFilas());
                sala.setIdCine(1);

                salaController.createSala(sala);
                asientoController.createAllAsientos(Integer.parseInt(tFilas.getText()),
                        Integer.parseInt(tColumnas.getText()),
                        salaController.findByNombre(tNombre.getText()).getIdSala());

                mensajeExito("Sala creada con exito!");
                CerrarFormulario(event);
            }
        }
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    private boolean verificarCampos() {

        if (tNombre.getText().isEmpty()) {
            mensajeError("El nombre de la sala no puede estar vacio.");
            return false;
        }

        if (tNombre.getText().length() < 5) {
            mensajeError("El nombre de la sala debe tener al menos 5 caracteres.");
            return false;
        }

        if (salaController.findByNombre(tNombre.getText()) != null) {
            mensajeError("El nombre de la sala ya esta en uso!");
            return false;
        }

        if (tFilas.getText().isEmpty()) {
            mensajeError("Al menos debe haber una (1) fila.");
            return false;
        }

        if (tColumnas.getText().isEmpty()) {
            mensajeError("Al menos debe haber una (1) columna.");
            return false;
        }

        return true;
    }

    private void mostrarAsientos() {
        panelAsientos.getChildren().clear(); // Limpia panel antes de añadir nuevos asientos

        int asientoWidth = 18;
        int asientoHeight = 14;
        int anchoPanel = 470;
        int altoPanel = 275;

        int filas = sala.getCantFilas();
        int columnas = sala.getCantColumnas();

        Image asientoImage = new Image("@../../Resources/Asiento.png");

        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPrefSize(anchoPanel, altoPanel);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                ImageView imgAsiento = new ImageView(asientoImage);
                imgAsiento.setFitWidth(asientoWidth);
                imgAsiento.setFitHeight(asientoHeight);
                imgAsiento.setPreserveRatio(true);

                grid.add(imgAsiento, j, i);
            }
        }

        panelAsientos.getChildren().add(grid);
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

    private boolean mensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType buttonAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAceptar, buttonCancelar);

        alert.showAndWait();

        if (alert.getResult() == buttonAceptar) {
            return true;
        }

        return false;
    }

}
