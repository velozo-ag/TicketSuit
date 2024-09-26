package views.administrador;

import java.sql.Connection;

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
    private PasswordField tPassword;

    DatabaseConnection dbController = new DatabaseConnection();
    Connection connection = dbController.connect();
    UsuarioController usuarioController = new UsuarioController(connection);

    @FXML
    public void initialize() {
        PerfilController perfilController = new PerfilController(connection);

        ObservableList<Boolean> estados = FXCollections.observableArrayList();
        estados.add(true);
        estados.add(false);
        cEstado.setItems(estados);

        ObservableList<Perfil> perfiles = FXCollections.observableArrayList(perfilController.findAll());
        cPerfil.setItems(perfiles);
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

            usuarioController.createUsuario(usuario);

            CerrarFormulario(event);
        }
    }

    private void mostrarEstados() {
        Boolean estado = cEstado.getValue();

        if (estado != null) {
            System.out.println(estado);
        } else {
            System.out.println("Estado incorrecto");
        }
    }

    private void mostrarPerfiles() {
        Perfil perfil = cPerfil.getValue();

        if (perfil != null) {
            System.out.println(perfil.getNombre());
        } else {
            System.out.println("Perfil incorrecto");
        }
    }

    private boolean verificarCampos() {
        if (tNombre == null || tNombre.getText().length() < 5) {
            mostrarMensajeError("El nombre debe tener al menos 5 caracteres.");
            return false;
        }

        // Verificar que la contraseña tenga al menos 6 caracteres
        if (tPassword == null || tPassword.getText().length() < 6) {
            mostrarMensajeError("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        // Verificar que el perfil seleccionado no sea nulo
        if (cPerfil.getValue() == null) {
            mostrarMensajeError("Debes seleccionar un perfil.");
            return false;
        }

        // Verificar que el estado sea booleano (true o false)
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
}
