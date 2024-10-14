package views.vendedor;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import views.MainController;

public class SeleccionAsientoController {

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Label tNombrePelicula;

    MainController mainController = new MainController();

    @FXML
    void toPVendedor(ActionEvent event) {
        try {
            mainController.toPVendedor(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
