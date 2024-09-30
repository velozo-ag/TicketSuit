package views.administrador;

import java.io.IOException;

import controllers.PeliculaController;
import entities.Director;
import entities.Genero;
import entities.Pelicula;
import entities.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import views.MainController;

public class ABMPeliculas {

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private TableColumn<Pelicula, Integer> colClasificacion;

    @FXML
    private TableColumn<Pelicula, Director> colDirector;

    @FXML
    private TableColumn<Pelicula, Integer> colDuracion;

    @FXML
    private TableColumn<Pelicula, Genero> colGeneros;

    @FXML
    private TableColumn<Pelicula, String> colNombre;

    @FXML
    private TableColumn<Pelicula, String> colSinopsis;

    @FXML
    private TableColumn<Pelicula, Void> colModificar;

    @FXML
    private TableColumn<Pelicula, Void> colDesactivar;

    MainController mainController = new MainController();
    AdministracionController administracionController = new AdministracionController();
    PeliculaController peliculaController = new PeliculaController();

    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("nombreClasificacion"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("nombreDirector"));
        colGeneros.setCellValueFactory(new PropertyValueFactory<>("generos"));
        colSinopsis.setCellValueFactory(new PropertyValueFactory<>("Sinopsis"));
        colSinopsis.setCellFactory(new Callback<TableColumn<Pelicula, String>, TableCell<Pelicula, String>>() {
            @Override
            public TableCell<Pelicula, String> call(TableColumn<Pelicula, String> param) {
                return new TableCell<Pelicula, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setTooltip(null);
                        } else {
                            setText(item);
                            Tooltip tooltip = new Tooltip(item);
                            tooltip.setWrapText(true);
                            tooltip.setMaxWidth(500);
                            setTooltip(tooltip);
                        }
                    }
                };
            }
        });

        asignarBotones();
        cargarPeliculas();
    }

    private void cargarPeliculas() {
        ObservableList<Pelicula> peliculas = FXCollections.observableArrayList(peliculaController.findAll());
        tablaPeliculas.setItems(peliculas);
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
    void formularioPelicula(ActionEvent event) {
        // try {
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/AltaPelicula.fxml"));
        //     Parent root = loader.load();

        //     Stage stage = new Stage();
        //     stage.setTitle("Formulario");
        //     stage.initModality(Modality.APPLICATION_MODAL);
        //     stage.initStyle(StageStyle.UNDECORATED);
        //     stage.setScene(new Scene(root));
        //     stage.showAndWait();

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
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
    void ABMUsuarios(ActionEvent event) {
        administracionController.ABMUsuarios(event);
    }

    void asignarBotones() {
        colModificar.setCellValueFactory(new PropertyValueFactory<>(""));
        colModificar.setCellFactory(new Callback<TableColumn<Pelicula, Void>, TableCell<Pelicula, Void>>() {
            @Override
            public TableCell<Pelicula, Void> call(TableColumn<Pelicula, Void> param) {
                return new TableCell<Pelicula, Void>() {

                    private final Button btnModificar = new Button("Modificar");

                    public HBox createButtonContainer() {
                        HBox hBox = new HBox();
                        hBox.getChildren().add(btnModificar);
                        HBox.setHgrow(btnModificar, Priority.ALWAYS);
                        btnModificar.setMaxWidth(Double.MAX_VALUE);
                        return hBox;
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item != null) {
                            setGraphic(null);
                        } else {
                            Pelicula pelicula = getTableView().getItems().get(getIndex());
                            btnModificar.setOnAction(event -> {
                                // modificarPelicula(pelicula);
                            });
                            setGraphic(createButtonContainer());
                        }
                    }
                };
            }
        });

        colDesactivar.setCellValueFactory(new PropertyValueFactory<>(""));
        colDesactivar.setCellFactory(new Callback<TableColumn<Pelicula, Void>, TableCell<Pelicula, Void>>() {
            @Override
            public TableCell<Pelicula, Void> call(TableColumn<Pelicula, Void> param) {
                return new TableCell<Pelicula, Void>() {
                    private final Button btnDesactivar = new Button("AltEstado");

                    public HBox createButtonContainer() {
                        HBox hBox = new HBox();
                        hBox.getChildren().add(btnDesactivar);
                        HBox.setHgrow(btnDesactivar, Priority.ALWAYS);
                        btnDesactivar.setMaxWidth(Double.MAX_VALUE);
                        return hBox;
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item != null) {
                            setGraphic(null);
                        } else {
                            Pelicula pelicula = getTableView().getItems().get(getIndex());
                            btnDesactivar.setOnAction(event -> {
                                // desactivarPelicula(pelicula);
                            });
                            setGraphic(createButtonContainer());
                        }

                    }
                };
            }
        });
    }

}
