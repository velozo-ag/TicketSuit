package Controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdministracionController {

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
