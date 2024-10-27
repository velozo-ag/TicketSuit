package views.administrador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TableCell;
import controllers.PerfilController;
import controllers.UsuarioController;
import database.DatabaseConnection;
import entities.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import views.MainController;
import views.SceneManager;
import views.administrador.usuario.MODIFICARUsuarioController;
import database.DatabaseConnection;

public class ABMUsuariosController {

    // PANEL PRINCIPAL ABMUSUARIOS
    @FXML
    private Pane mainPanel;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, Boolean> colEstado;

    @FXML
    private TableColumn<Usuario, Integer> colIdUsuario;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, Integer> colPerfil;

    @FXML
    private TableColumn<Usuario, Void> colModificar;

    @FXML
    private TableColumn<Usuario, Void> colDesactivar;

    @FXML
    private Pane dataPanel;

    @FXML
    private Label labelSeleccionar;

    @FXML
    private Label labId;

    @FXML
    private Label labNombre;

    @FXML
    private Label labPerfil;

    @FXML
    private Label labEstado;

    @FXML
    private Button bActivar;

    @FXML
    private Button bDesactivar;

    @FXML
    private TextField tFiltro;

    private MainController mainController = new MainController();
    private UsuarioController usuarioController = new UsuarioController();
    private PerfilController perfilController = new PerfilController();

    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPerfil.setCellValueFactory(new PropertyValueFactory<>("nombrePerfil"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateUsuarioSeleccionado(newValue);
                updateDataPanel();
            }
        });

        dataPanel.setVisible(false);
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> usuarios = null;

        if (!tFiltro.getText().isEmpty()) {
            usuarios = FXCollections.observableArrayList(usuarioController.findByContain(tFiltro.getText()));
            System.out.println(tFiltro.getText());
        } else {
            usuarios = FXCollections.observableArrayList(usuarioController.findAll());
        }
        tablaUsuarios.setItems(usuarios);
    }

    private void updateUsuarioSeleccionado(Usuario usuario) {
        usuarioSeleccionado = usuario;
        System.out.println(
                "Usuario: " + usuarioSeleccionado.getNombre() + " - Perfil: " + usuarioSeleccionado.getNombrePerfil());
    }

    private void updateDataPanel() {
        labelSeleccionar.setVisible(false);
        dataPanel.setVisible(true);

        labNombre.setText(usuarioSeleccionado.getNombre().toUpperCase());
        labId.setText("#" + Integer.toString(usuarioSeleccionado.getIdUsuario()));
        labPerfil.setText(perfilController.findById(usuarioSeleccionado.getIdPerfil()).getNombre());
        labEstado.setText(usuarioSeleccionado.getEstado() ? "ACTIVO" : "INACTIVO");

        if (usuarioSeleccionado.getIdPerfil() == 1) {
            bDesactivar.setDisable(true);
            bActivar.setDisable(true);
            return;
        }

        if (usuarioSeleccionado.getEstado()) {
            bDesactivar.setDisable(false);
            bActivar.setDisable(true);
        } else {
            bDesactivar.setDisable(true);
            bActivar.setDisable(false);
        }
    }

    @FXML
    void formularioUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/usuario/AltaUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> cargarUsuarios());

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/administrador/usuario/ModificarUsuario.fxml"));
            Parent root = loader.load();

            MODIFICARUsuarioController modificarUsuarioController = loader.getController();
            modificarUsuarioController.setUsuario(usuarioSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> cargarUsuarios());
            stage.showAndWait();

            usuarioSeleccionado = usuarioController.findById(usuarioSeleccionado.getIdUsuario());
            updateDataPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void filtrarUsuarios(ActionEvent event) {
        cargarUsuarios();
    }

    void modificarEstado() throws SQLException {
        usuarioSeleccionado.setEstado(!usuarioSeleccionado.getEstado());
        usuarioController.updateUsuario(usuarioSeleccionado);
        usuarioSeleccionado = usuarioController.findById(usuarioSeleccionado.getIdUsuario());
        updateDataPanel();
        cargarUsuarios();
    }

    @FXML
    void activarUsuario(ActionEvent event) throws SQLException {
        boolean res = mensajeConfirmacion("Desea activar al usuario " + usuarioSeleccionado.getNombre() + "?");

        if (res) {
            modificarEstado();
            bActivar.setDisable(true);
            bDesactivar.setDisable(false);
        }
    }

    @FXML
    void desactivarUsuario(ActionEvent event) throws SQLException {
        boolean res = mensajeConfirmacion("Desea desactivar al usuario " + usuarioSeleccionado.getNombre() + "?");

        if (res) {
            modificarEstado();
            bActivar.setDisable(false);
            bDesactivar.setDisable(true);
        }
    }

    @FXML
    void logout(ActionEvent event) {
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
