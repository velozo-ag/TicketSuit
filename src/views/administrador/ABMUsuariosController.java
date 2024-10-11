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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private AdministracionController administracionController = new AdministracionController();
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
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(usuarioController.findAll());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/AltaUsuario.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/ModificarUsuario.fxml"));
            Parent root = loader.load();

            MODIFICARUsuarioController controller = loader.getController();
            controller.setUsuario(usuarioSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> cargarUsuarios());
            stage.showAndWait();

            usuarioSeleccionado = usuarioController.findById(usuarioSeleccionado.getIdUsuario());
            updateDataPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        modificarEstado();
        bActivar.setDisable(true);
        bDesactivar.setDisable(false);
    }
    
    @FXML
    void desactivarUsuario(ActionEvent event) throws SQLException {
        modificarEstado();
        bActivar.setDisable(false);
        bDesactivar.setDisable(true);
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

    @FXML
    void ABMSalas(ActionEvent event) {
        administracionController.ABMSalas(event);
    }

    @FXML
    void ABMFunciones(ActionEvent event) {
        administracionController.ABMFunciones(event);
    }

    @FXML
    void ABMPeliculas(ActionEvent event) {
        administracionController.ABMPeliculas(event);
    }

    // void asignarBotones() {

    // colModificar.setCellValueFactory(new PropertyValueFactory<>(""));
    // colModificar.setCellFactory(new Callback<TableColumn<Usuario, Void>,
    // TableCell<Usuario, Void>>() {
    // @Override
    // public TableCell<Usuario, Void> call(TableColumn<Usuario, Void> param) {
    // return new TableCell<Usuario, Void>() {

    // private final Button btnModificar = new Button("Modificar");

    // public HBox createButtonContainer() {
    // HBox hBox = new HBox();
    // hBox.getChildren().add(btnModificar);
    // HBox.setHgrow(btnModificar, Priority.ALWAYS);
    // btnModificar.setMaxWidth(Double.MAX_VALUE);
    // return hBox;
    // }

    // @Override
    // protected void updateItem(Void item, boolean empty) {
    // super.updateItem(item, empty);
    // if (empty || item != null) {
    // setGraphic(null);
    // } else {
    // Usuario usuario = getTableView().getItems().get(getIndex());
    // if (usuario.getIdPerfil() == 1) {
    // setGraphic(null);
    // } else {
    // btnModificar.setOnAction(event -> {
    // modificarUsuario(usuario);
    // });
    // setGraphic(createButtonContainer());
    // }
    // }
    // }
    // };
    // }
    // });

    // colDesactivar.setCellValueFactory(new PropertyValueFactory<>(""));
    // colDesactivar.setCellFactory(new Callback<TableColumn<Usuario, Void>,
    // TableCell<Usuario, Void>>() {
    // @Override
    // public TableCell<Usuario, Void> call(TableColumn<Usuario, Void> param) {
    // return new TableCell<Usuario, Void>() {
    // private final Button btnDesactivar = new Button("AltEstado");

    // public HBox createButtonContainer() {
    // HBox hBox = new HBox();
    // hBox.getChildren().add(btnDesactivar);
    // HBox.setHgrow(btnDesactivar, Priority.ALWAYS);
    // btnDesactivar.setMaxWidth(Double.MAX_VALUE);
    // return hBox;
    // }

    // @Override
    // protected void updateItem(Void item, boolean empty) {
    // super.updateItem(item, empty);
    // if (empty || item != null) {
    // setGraphic(null);
    // } else {
    // Usuario usuario = getTableView().getItems().get(getIndex());
    // if (usuario.getIdPerfil() == 1) {
    // setGraphic(null);
    // } else {
    // btnDesactivar.setOnAction(event -> {
    // desactivarUsuario(usuario);
    // });
    // setGraphic(createButtonContainer());
    // }
    // }
    // }
    // };
    // }
    // });
    // }

}
