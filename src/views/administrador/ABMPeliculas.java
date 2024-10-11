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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import views.MainController;

public class ABMPeliculas {

    @FXML
    private Button bActivar;

    @FXML
    private Button bDesactivar;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private TableColumn<Pelicula, Integer> colClasificacion;

    @FXML
    private TableColumn<Pelicula, Director> colDirector;

    @FXML
    private TableColumn<Pelicula, Genero> colGeneros;

    @FXML
    private TableColumn<Pelicula, String> colNombre;

    @FXML
    private TableColumn<Pelicula, String> colSinopsis;

    @FXML
    private Pane dataPanel;

    @FXML
    private Label labClasificacion;

    @FXML
    private Label labDirector;

    @FXML
    private Label labDuracion;

    @FXML
    private Label labGenero;

    @FXML
    private Label labNacionalidad;

    @FXML
    private Label labNombre;

    @FXML
    private Label labSinopsis;

    @FXML
    private Label labelSeleccionar;

    @FXML
    private Pane mainPanel;

    @FXML
    private ImageView portadaPelicula;

    MainController mainController = new MainController();
    AdministracionController administracionController = new AdministracionController();
    PeliculaController peliculaController = new PeliculaController();

    Pelicula peliculaSeleccionada;

    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("nombreClasificacion"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("nombreDirector"));
        colSinopsis.setCellValueFactory(new PropertyValueFactory<>("Sinopsis"));
        colGeneros.setCellValueFactory(new PropertyValueFactory<>("generos"));
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

        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updatePeliculaSeleccionada(newValue);
                updateDataPanel();
            }
        });

        dataPanel.setVisible(false);
        cargarPeliculas();
    }

    private void cargarPeliculas() {
        ObservableList<Pelicula> peliculas = FXCollections.observableArrayList(peliculaController.findAll());
        tablaPeliculas.setItems(peliculas);
    }

    private void updatePeliculaSeleccionada(Pelicula pelicula) {
        peliculaSeleccionada = pelicula;
    }

    private void updateDataPanel() {
        labelSeleccionar.setVisible(false);
        dataPanel.setVisible(true);

        labNombre.setText(peliculaSeleccionada.getNombre());
        labDirector.setText(peliculaSeleccionada.getNombreDirector());
        labDuracion.setText(peliculaSeleccionada.getDuracion() + " min");
        labGenero.setText(peliculaSeleccionada.getNombreGeneros());
        labClasificacion.setText(peliculaSeleccionada.getNombreClasificacion());
        labNacionalidad.setText(peliculaSeleccionada.getNacionalidad());
        labSinopsis.setText(peliculaSeleccionada.getSinopsis());
        portadaPelicula.setImage(new Image(peliculaSeleccionada.getImagen()));
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/AltaPelicula.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void modificarPelicula() {

    }
}
