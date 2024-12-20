package views.administrador.usuario;

import java.sql.Connection;
import java.sql.SQLException;

import controllers.PerfilController;
import controllers.UsuarioController;
import database.DatabaseConnection;
import entities.Perfil;
import entities.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ALTAUsuarioController {

    @FXML
    private Button bCerrar;

    @FXML
    private ComboBox<Boolean> cEstado;

    @FXML
    private ComboBox<Perfil> cPerfil;

    @FXML
    private Pane formPanel;

    @FXML
    private Pane mainButton;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private TextField tNombre;

    @FXML
    private TextField tDni;

    @FXML
    private PasswordField tPassword;

    UsuarioController usuarioController = new UsuarioController();

    @FXML
    public void initialize() {
        PerfilController perfilController = new PerfilController();

        ObservableList<Boolean> estados = FXCollections.observableArrayList();
        estados.add(true);
        estados.add(false);
        cEstado.setItems(estados);

        ObservableList<Perfil> perfiles = FXCollections.observableArrayList(perfilController.findAll());
        cPerfil.setItems(perfiles);

        tDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tDni.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void altaUsuario(ActionEvent event) {

        if (verificarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setNombre(tNombre.getText());
            usuario.setPassword(tPassword.getText());
            usuario.setIdPerfil(cPerfil.getValue().getIdPerfil());
            usuario.setEstado(cEstado.getValue());
            usuario.setIdCine(1);
            usuario.setDni(Integer.parseInt(tDni.getText()));

            usuarioController.createUsuario(usuario);

            mensajeExito("Usuario creado con exito!");
            CerrarFormulario(event);
        }
    }

    private boolean verificarCampos() {
        if (tNombre == null || tNombre.getText().length() < 5) {
            mensajeError("El nombre debe tener al menos 5 caracteres.");
            return false;
        }

        if (tDni == null || tDni.getText().length() != 8) {
            mensajeError("El DNI debe tener 8 digitos.");
            return false;
        }

        if (usuarioController.findByDni(Integer.parseInt(tDni.getText())) != null) {
            mensajeError("El DNI ya existe.");
            return false;
        }

        if (usuarioController.findByUser(tNombre.getText()) != null) {
            mensajeError("El nombre ya esta en uso.");
            return false;
        }

        // Verificar que la contraseña tenga al menos 6 caracteres
        if (tPassword == null || tPassword.getText().length() < 6) {
            mensajeError("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        // Verificar que el perfil seleccionado no sea nulo
        if (cPerfil.getValue() == null) {
            mensajeError("Debes seleccionar un perfil.");
            return false;
        }

        // Verificar que el estado sea booleano (true o false)
        if (cEstado.getValue() == null) {
            mensajeError("El estado debe ser un valor booleano (true/false).");
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
