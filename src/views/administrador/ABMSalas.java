package views.administrador;

import java.io.IOException;
import java.util.List;
import controllers.AsientoController;
import controllers.SalaController;
import entities.Asiento;
import entities.Pelicula;
import entities.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.MainController;
import views.administrador.sala.ModificarSalaController;
import views.administrador.usuario.MODIFICARUsuarioController;

public class ABMSalas {

    @FXML
    private Button bActivar;

    @FXML
    private Button bDesactivar;
    
    @FXML
    private Button bModificar;

    @FXML
    private TableView<Sala> tablaSalas;

    @FXML
    private TableColumn<Sala, String> colNombreSala;

    @FXML
    private TableColumn<Sala, String> colCapacidad;

    @FXML
    private TableColumn<Sala, Integer> colSalaID;

    @FXML
    private TableColumn<Sala, Integer> colEstado;

    @FXML
    private Pane asientosPanel;

    @FXML
    private Pane dataPanel;

    @FXML
    private Pane dataPanel2;

    @FXML
    private Label labCapacidad;

    @FXML
    private Label labDimensiones;

    @FXML
    private Label labId;

    @FXML
    private Label labNombre;

    @FXML
    private Label labelSeleccionar;

    @FXML
    private Label labelSeleccionar2;

    @FXML
    private Pane mainPanel;

    private Sala salaSeleccionada;
    private MainController mainController = new MainController();
    private SalaController salaController = new SalaController();
    private AsientoController asientoController = new AsientoController();

    public void initialize() {
        colSalaID.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("dimensiones"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNombre"));

        tablaSalas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSalaSeleccionada(newValue);
                updateDataPanel();
            }
        });

        dataPanel.setVisible(false);
        dataPanel2.setVisible(false);
        bModificar.setVisible(false);
        cargarSalas();
    }

    @FXML
    void formularioSala(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/sala/AltaSala.fxml"));
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
    void modificarAsientos(ActionEvent event) {

    }

    @FXML
    void modificarSala(ActionEvent event) {
        // try {
        //     FXMLLoader loader = new FXMLLoader(
        //             getClass().getResource("/views/administrador/sala/ModificarSala.fxml"));
        //     Parent root = loader.load();

        //     ModificarSalaController modificarSalaController = loader.getController();
        //     modificarSalaController.setSala(salaSeleccionada);

        //     Stage stage = new Stage();
        //     stage.setTitle("Modificar sala");
        //     stage.initModality(Modality.APPLICATION_MODAL);
        //     stage.setScene(new Scene(root));

        //     stage.setOnHidden(e -> cargarSalas());
        //     stage.showAndWait();

        //     salaSeleccionada = salaController.findById(salaSeleccionada.getIdSala());
        //     updateDataPanel();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

    }

    @FXML
    void activarSala(ActionEvent event) {
        boolean res = mensajeConfirmacion("Desea activar la sala: " + salaSeleccionada.getNombre() + "?");

        if (res) {
            bActivar.setDisable(true);
            bDesactivar.setDisable(false);

            salaSeleccionada.setEstado(1);
            salaController.updateSala(salaSeleccionada);
            cargarSalas();

            
            mostrarMensajeExito("Sala activada con éxito",
                            "La sala " + salaSeleccionada.getNombre() + " ha sido activada correctamente.");
        }
    }

    @FXML
    void desactivarSala(ActionEvent event) {
    // Advertencia con detalles
        String advertencia = "Al desactivar esta sala, se eliminarán todas las funciones asociadas "
            + "que no tengan tickets vendidos. ¿Desea continuar?";
        
        boolean confirmacion = mensajeConfirmacion(advertencia);

        if (confirmacion) {
            // Desactivar la sala
            salaSeleccionada.setEstado(0);
            salaController.updateSala(salaSeleccionada);

            // Eliminar funciones sin tickets
            int funcionesEliminadas = salaController.eliminarFuncionesSinTickets(salaSeleccionada.getIdSala());

            // Mostrar resultado
            String mensajeFinal = "La sala se desactivó con éxito.\n"
                + "Se eliminaron " + funcionesEliminadas + " funciones que no tenían tickets vendidos.";
            mostrarMensaje("Operación exitosa", mensajeFinal);

            // Actualizar la vista
            cargarSalas();

            // Habilitar botones de acción
            bActivar.setDisable(false);
            bDesactivar.setDisable(true);
        } else {
            mostrarMensaje("Operación cancelada", "No se realizó ninguna acción.");
        }
    }


    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void updateDataPanel() {
        labelSeleccionar.setVisible(false);
        labelSeleccionar2.setVisible(false);
        dataPanel.setVisible(true);
        dataPanel2.setVisible(true);

        labId.setText(salaSeleccionada.getIdSala() + "");
        labNombre.setText(salaSeleccionada.getNombre());
        labCapacidad.setText(salaSeleccionada.getCapacidad() + "");
        labDimensiones.setText(salaSeleccionada.getCantFilas() + " x " + salaSeleccionada.getCantColumnas());

        mostrarAsientos();
    }

    private void cargarSalas() {
        ObservableList<Sala> salas = FXCollections.observableArrayList(salaController.findAll());
        tablaSalas.setItems(salas);
    }

    private void updateSalaSeleccionada(Sala sala) {
        salaSeleccionada = sala;

        if (sala.getEstado() == 1) {
            bActivar.setDisable(true);
            bDesactivar.setDisable(false);
        } else {
            bActivar.setDisable(false);
            bDesactivar.setDisable(true);
        }
    }

    private void mostrarMensajeExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAsientos() {
        asientosPanel.getChildren().clear(); // Limpia panel antes de añadir nuevos asientos

        int asientoWidth = 18;
        int asientoHeight = 14;
        int anchoPanel = 470;
        int altoPanel = 275;

        int filas = salaSeleccionada.getCantFilas();
        int columnas = salaSeleccionada.getCantColumnas();

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

        asientosPanel.getChildren().add(grid);
    }

    @FXML
    void toPAdmin(ActionEvent event) {
        try {
            mainController.toPAdmin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
