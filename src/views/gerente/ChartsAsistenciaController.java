package views.gerente;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import views.MainController;

public class ChartsAsistenciaController {

    private MainController mainController = new MainController();

    @FXML
    private Pane mainPanel;

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}