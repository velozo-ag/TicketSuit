package views;

import java.io.IOException;

import controllers.UsuarioController;
import entities.UserSession;
import entities.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button bLogin;

    @FXML
    private Button bCerrar;

    @FXML
    private AnchorPane pLogin;

    @FXML
    private PasswordField tPassword;

    @FXML
    private TextField tUsername;

    private MainController mainController = new MainController();
    private UsuarioController usuarioController = new UsuarioController();

    @FXML
    void authAccount(ActionEvent event) {

        Usuario usuario = usuarioController.findByUser(tUsername.getText());
        if (usuario == null) {
            usuario = usuarioController.findByDni(Integer.parseInt(tUsername.getText()));
        }

        System.out.println(usuario.getNombre());

        if (verificarCampos(usuario)) {
            UserSession.getInstance().setUsuario(usuario);

            try {
                switch (usuario.getIdPerfil()) {
                    case 1:
                        mainController.toPAdmin(event);
                        break;
                    case 2:
                        mainController.toPVendedor(event);
                        break;
                    case 3:
                        mainController.toPGerente(event);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    private boolean verificarCampos(Usuario usuario) {

        String nombre;
        String password;

        if (usuario != null) {
            nombre = usuario.getNombre();
            password = usuario.getPassword();
        } else {
            mostrarMensajeError("Usuario invalido");
            return false;
        }

        if (nombre == null) {
            mostrarMensajeError("Usuario incorrecto.");
            return false;
        }
        if (!tPassword.getText().equals(password)) {
            mostrarMensajeError("Contraseña incorrecta.");
            return false;
        }

        if (!usuario.getEstado()) {
            mostrarMensajeError("Usuario no habilitado");
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
}
