package Controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CarteleraController {

    private MainController mainController = new MainController();

    @FXML
    void logout(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPelicula(ActionEvent event) {

    }

    @FXML
    void toTickets(ActionEvent event) {

    }

}
