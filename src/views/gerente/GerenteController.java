package views.gerente;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import views.MainController;

public class GerenteController {

    private MainController mainController = new MainController();

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
