package views.vendedor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfirmacionVentaController {

    @FXML
    private Button bCerrar;

    @FXML
    private Pane formPanel;

    @FXML
    private Label lPrecio1;

    @FXML
    private Pane mainButton;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private Pane panelImagen;

    @FXML
    private Label tAsientos;

    @FXML
    private Label tFecha;

    @FXML
    private Label tHorario;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tPrecioTotal;

    @FXML
    private Label tSala;

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void realizarVenta(ActionEvent event) {

    }

}
