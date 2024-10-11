package views.administrador;

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
    private PasswordField tPassword;

    private Usuario usuario;
    private UsuarioController usuarioController = new UsuarioController();
    private PerfilController perfilController = new PerfilController();

    @FXML
    public void initialize() {
        ObservableList<Perfil> perfiles = FXCollections.observableArrayList(perfilController.findAll());
        cPerfil.setItems(perfiles);
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void modificarUsuario(ActionEvent event) {
        if (verificarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(this.usuario.getIdUsuario());
            usuario.setNombre(tNombre.getText());
            usuario.setPassword(tPassword.getText());
            usuario.setIdPerfil(cPerfil.getValue().getIdPerfil());
            usuario.setEstado(cEstado.getValue());
            usuario.setIdCine(1);

            usuarioController.updateUsuario(usuario);

            CerrarFormulario(event);
        }
    }

    private boolean verificarCampos() {
        if (tNombre == null || tNombre.getText().length() < 5) {
            mostrarMensajeError("El nombre debe tener al menos 5 caracteres.");
            return false;
        }

        if (usuarioController.findByUser(tNombre.getText()) != null) {
            mostrarMensajeError("El nombre ya esta en uso.");
            return false;
        }

        if (tPassword == null || tPassword.getText().length() < 6) {
            mostrarMensajeError("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        if (cPerfil.getValue() == null) {
            mostrarMensajeError("Debes seleccionar un perfil.");
            return false;
        }

        if (cEstado.getValue() == null) {
            mostrarMensajeError("El estado debe ser un valor booleano (true/false).");
            return false;
        }

        return true;
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        List<Perfil> perfiles = perfilController.findAll();

        ObservableList<Boolean> estados = FXCollections.observableArrayList();
        estados.add(false);
        estados.add(true);
        cEstado.setItems(estados);

        if(usuario.getIdPerfil() == 1){
            cEstado.setDisable(true);
            cPerfil.setDisable(true);
        }else{
            cEstado.setDisable(false);
            cPerfil.setDisable(false);
        }

        tNombre.setText(usuario.getNombre());
        tPassword.setText(usuario.getPassword());
        cEstado.setValue(usuario.getEstado());
        cPerfil.setValue(perfiles.get(usuario.getIdPerfil() - 1));
    }

}
