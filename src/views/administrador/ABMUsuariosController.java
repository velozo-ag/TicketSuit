package views.administrador;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javafx.scene.control.TableCell;

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

    DatabaseConnection dbController = new DatabaseConnection();

    private MainController mainController = new MainController();
    private UsuarioController usuarioController = new UsuarioController(dbController.connect());

    @FXML
    public void initialize() {
        System.out.println("Método initialize() ejecutado.");

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPerfil.setCellValueFactory(new PropertyValueFactory<>("idPerfil"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        asignarBotones();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(usuarioController.findAll());
        tablaUsuarios.setItems(usuarios);
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
    void logout(ActionEvent event) {

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
    }

    @FXML
    void ABMFunciones(ActionEvent event) {
    }

    @FXML
    void ABMPeliculas(ActionEvent event) {
    }

    void asignarBotones() {
        colModificar.setCellValueFactory(new PropertyValueFactory<>("")); // Esto debe ser vacío
        colModificar.setCellFactory(new Callback<TableColumn<Usuario, Void>, TableCell<Usuario, Void>>() {
            @Override
            public TableCell<Usuario, Void> call(TableColumn<Usuario, Void> param) {
                return new TableCell<Usuario, Void>() {
                    private final Button btnModificar = new Button("Modificar");

                    public HBox createButtonContainer() {
                        HBox hBox = new HBox();
                        hBox.getChildren().add(btnModificar);
                        HBox.setHgrow(btnModificar, Priority.ALWAYS); // Esto asegura que el botón ocupe todo el ancho
                        btnModificar.setMaxWidth(Double.MAX_VALUE); // Asegura que el botón use todo el ancho disponible
                        return hBox;
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item != null) {
                            setGraphic(null);
                        } else {
                            btnModificar.setOnAction(event -> {
                                Usuario usuario = getTableView().getItems().get(getIndex());
                                // modificarUsuario(usuario);
                            });
                            setGraphic(createButtonContainer()); // Usar el contenedor de botones
                        }
                    }
                };
            }
        });

        colDesactivar.setCellValueFactory(new PropertyValueFactory<>(""));
        colDesactivar.setCellFactory(new Callback<TableColumn<Usuario, Void>, TableCell<Usuario, Void>>() {
            @Override
            public TableCell<Usuario, Void> call(TableColumn<Usuario, Void> param) {
                return new TableCell<Usuario, Void>() {
                    private final Button btnDesactivar = new Button("Desactivar");

                    public HBox createButtonContainer() {
                        HBox hBox = new HBox();
                        hBox.getChildren().add(btnDesactivar);
                        HBox.setHgrow(btnDesactivar, Priority.ALWAYS); // Esto asegura que el botón ocupe todo el ancho
                        btnDesactivar.setMaxWidth(Double.MAX_VALUE); // Asegura que el botón use todo el ancho
                                                                     // disponible
                        return hBox;
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item != null) {
                            setGraphic(null);
                        } else {
                            btnDesactivar.setOnAction(event -> {
                                Usuario usuario = getTableView().getItems().get(getIndex());
                                // desactivarUsuario(usuario);
                            });
                            setGraphic(createButtonContainer()); // Usar el contenedor de botones
                        }
                    }
                };
            }
        });
    }
}
