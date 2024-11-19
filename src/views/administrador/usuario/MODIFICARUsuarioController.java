package views.administrador.usuario;

import java.sql.Connection;
import java.util.List;

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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MODIFICARUsuarioController {

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

    private Usuario usuario;
    private UsuarioController usuarioController = new UsuarioController();
    private PerfilController perfilController = new PerfilController();

    @FXML
    public void initialize() {
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
    void modificarUsuario(ActionEvent event) {
        boolean res = mensajeConfirmacion("Desea modificar usuario " + usuario.getNombre() + "?");

        if (res) {
            if (verificarCampos()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(this.usuario.getIdUsuario());
                usuario.setNombre(tNombre.getText());
                usuario.setPassword(tPassword.getText());
                usuario.setIdPerfil(cPerfil.getValue().getIdPerfil());
                usuario.setEstado(cEstado.getValue());
                usuario.setDni(Integer.parseInt(tDni.getText()));
                usuario.setIdCine(1);

                usuarioController.updateUsuario(usuario);

                mensajeExito("Usuario modificado con exito!");
                CerrarFormulario(event);
            }
        }
    }

    private boolean verificarCampos() {
        if (tNombre == null || tNombre.getText().length() < 5) {
            mensajeError("El nombre debe tener al menos 5 caracteres.");
            return false;
        }

        if (usuarioController.findByUser(tNombre.getText()).getIdUsuario() != usuario.getIdUsuario()) {
            mensajeError("El nombre ya esta en uso.");
            return false;
        }

        if (tDni == null || tDni.getText().length() != 8) {
            mensajeError("El DNI debe tener 8 digitos.");
            return false;
        }

        if (usuarioController.findByDni(Integer.parseInt(tDni.getText())) != null) {
            mensajeError("El DNI se encuentra en uso.");
            return false;
        }

        if (tPassword == null || tPassword.getText().length() < 6) {
            mensajeError("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        if (cPerfil.getValue() == null) {
            mensajeError("Debes seleccionar un perfil.");
            return false;
        }

        if (cEstado.getValue() == null) {
            mensajeError("El estado debe ser un valor booleano (true/false).");
            return false;
        }

        return true;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        List<Perfil> perfiles = perfilController.findAll();

        ObservableList<Boolean> estados = FXCollections.observableArrayList();
        estados.add(false);
        estados.add(true);
        cEstado.setItems(estados);

        if (usuario.getIdPerfil() == 1) {
            cEstado.setDisable(true);
            cPerfil.setDisable(true);
        } else {
            cEstado.setDisable(false);
            cPerfil.setDisable(false);
        }

        tNombre.setText(usuario.getNombre());
        tPassword.setText(usuario.getPassword());
        cEstado.setValue(usuario.getEstado());
        System.out.println(perfiles.get(0));
        System.out.println(perfiles.get(1));
        System.out.println(perfiles.get(2));
        cPerfil.setValue(perfiles.get(usuario.getIdPerfil() - 1));
        tDni.setText(String.valueOf(usuario.getDni()));
    }

    private void mensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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
