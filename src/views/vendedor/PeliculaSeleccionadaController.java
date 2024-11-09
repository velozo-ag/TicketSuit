package views.vendedor;

import java.io.IOException;
import java.util.List;

import controllers.FuncionController;
import controllers.Sala_FuncionController;
import controllers.TipoFuncionController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import entities.Funcion;
import entities.Pelicula;
import entities.Sala_Funcion;
import entities.TipoFuncion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.MainController;
import views.SceneManager;

public class PeliculaSeleccionadaController {

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tSinopsis;

    private FuncionController funcionController = new FuncionController();
    private TipoFuncionController tipoFuncionController = new TipoFuncionController();
    private MainController mainController = new MainController();
    private Sala_FuncionController sala_FuncionController = new Sala_FuncionController();
    public Pelicula pelicula;
    public List<Sala_Funcion> funciones;

    @FXML
    void initialize() {
        // cargarFunciones();
    }

    @FXML
    void continuarCompra(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/SeleccionAsiento.fxml"));
            root = loader.load();

            SeleccionAsientoController seleccionAsientoController = loader.getController();
            seleccionAsientoController.setPelicula(pelicula);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPVendedor(ActionEvent event) {
        try {
            mainController.toPVendedor(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        tNombrePelicula.setText(pelicula.getNombre());
        tSinopsis.setText(pelicula.getSinopsis());

        cargarFunciones();
    }

    private void cargarFunciones() {
        int panelHeight = 180;
        int panelWidth = 128;
        int rowHeight = 30;

        double PosY = 337;
        double ColPos2D = 546;
        double ColPos3D = 705;
        double ColPosIMAX = 873;

        GridPane gridPane2D = new GridPane();
        GridPane gridPane3D = new GridPane();
        GridPane gridPaneIMAX = new GridPane();

        gridPane2D.setLayoutX(ColPos2D);
        gridPane2D.setLayoutY(PosY);
        gridPane3D.setLayoutX(ColPos3D);
        gridPane3D.setLayoutY(PosY);
        gridPaneIMAX.setLayoutX(ColPosIMAX);
        gridPaneIMAX.setLayoutY(PosY);

        funciones = sala_FuncionController.findByIdPelicula(this.pelicula.getIdPelicula());

        for (Sala_Funcion funcion : funciones) {
            TipoFuncion tipoFuncion = tipoFuncionController
                    .findById(funcionController.findById(funcion.getId_funcion()).getId_tipoFuncion());

            Button button = new Button(funcion.getPelicula().getNombre() + " " + funcion.getInicioFuncion());

            if (tipoFuncion.getIdTipoFuncion() == 1) {
                gridPane2D.add(button, 0, gridPane2D.getRowCount());
            }

            if (tipoFuncion.getIdTipoFuncion() == 2) {
                gridPane3D.add(button, 0, gridPane3D.getRowCount());
            }

            if (tipoFuncion.getIdTipoFuncion() == 3) {
                gridPaneIMAX.add(button, 0, gridPaneIMAX.getRowCount());
            }
        }

        mainPanel.getChildren().addAll(gridPane2D, gridPane3D, gridPaneIMAX);

    }

}
