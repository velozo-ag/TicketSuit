package views.administrador.sala;

import java.util.ArrayList;
import java.util.List;

import controllers.AsientoController;
import controllers.SalaController;
import entities.Asiento;
import entities.Sala;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AltaSalaController {

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

    private SalaController salaController = new SalaController();
    private AsientoController asientoController = new AsientoController();

    public void initialize() {
        tFilas.setText("1");
        ;
        tColumnas.setText("1");
        ;
        tCapacidad.setText("1");
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

        tColumnas.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {

                tColumnas.setText(newValue.replaceAll("[^\\d]", ""));

            } else if (!newValue.isEmpty()) {
                int col = Integer.parseInt(newValue);

                if (col > 18) {
                    tColumnas.setText("18");
                    mensajeError("La cantidad de columnas no puede ser mayor a 18.");
                }
                if (col <= 0) {
                    tColumnas.setText("1");
                }

                tCapacidad.setText(String.valueOf(col * Integer.parseInt(tFilas.getText())));
                mostrarAsientos();
            }
        });

    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void altaSala(ActionEvent event) {
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

    private void mostrarAsientos() {
        panelAsientos.getChildren().clear(); // Limpia panel antes de añadir nuevos asientos

        int asientoWidth = 14;
        int asientoHeight = 10;
        int anchoPanel = 346;
        int altoPanel = 186;

        int filas = Integer.parseInt(tFilas.getText());
        int columnas = Integer.parseInt(tColumnas.getText());

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

    private boolean verificarCampos() {
        // Verificar que el nombre no esté vacío
        if (tNombre.getText().isEmpty()) {
            mensajeError("El nombre de la sala no puede estar vacío.");
            return false;
        }
    
        // Verificar que el nombre tenga al menos 5 caracteres
        if (tNombre.getText().length() < 5) {
            mensajeError("El nombre de la sala debe tener al menos 5 caracteres.");
            return false;
        }
    
        // Verificar si el nombre ya existe en la base de datos
        if (salaController.findByNombre(tNombre.getText()) != null) {
            mensajeError("El nombre de la sala ya está en uso.");
            return false;
        }
    
        // Verificar que el valor de filas sea válido
        if (tFilas.getText().isEmpty() || Integer.parseInt(tFilas.getText()) <= 0) {
            mensajeError("La cantidad de filas debe ser mayor a 0.");
            return false;
        }
    
        if (Integer.parseInt(tFilas.getText()) > 20) {
            mensajeError("La cantidad de filas no puede ser mayor a 16.");
            return false;
        }
    
        // Verificar que el valor de columnas sea válido
        if (tColumnas.getText().isEmpty() || Integer.parseInt(tColumnas.getText()) <= 0) {
            mensajeError("La cantidad de columnas debe ser mayor a 0.");
            return false;
        }
    
        if (Integer.parseInt(tColumnas.getText()) > 20) {
            mensajeError("La cantidad de columnas no puede ser mayor a 18.");
            return false;
        }
    
        // Si pasa todas las verificaciones
        return true;
    }

}
