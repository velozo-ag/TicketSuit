package views.administrador;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import views.MainController;

public class ABMSalas {

    @FXML
    private Pane mainPanel;

    private AdministracionController administracionController = new AdministracionController();
    private MainController mainController = new MainController();

    @FXML
    void ABMFunciones(ActionEvent event) {
        administracionController.ABMFunciones(event);
    }
    
    @FXML
    void ABMPeliculas(ActionEvent event) {
        administracionController.ABMPeliculas(event);
    }

    @FXML
    void ABMUsuarios(ActionEvent event) {
        administracionController.ABMUsuarios(event);
    }

    @FXML
    void formularioSala(ActionEvent event) {

    }

    @FXML
    void toLogin(ActionEvent event) {
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

}
