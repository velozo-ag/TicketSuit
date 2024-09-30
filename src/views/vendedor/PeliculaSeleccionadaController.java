package views.vendedor;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import entities.Pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import views.MainController;

public class PeliculaSeleccionadaController {

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tSinopsis;

    private MainController mainController = new MainController();
    public Pelicula pelicula;

    @FXML
    void continuarCompra(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/SeleccionAsiento.fxml"));
            root = loader.load();

            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(new ActionEvent(event.getSource(), event.getTarget()), panel, scene);
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
    }

}
