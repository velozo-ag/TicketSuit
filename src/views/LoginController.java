package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController {

    @FXML
    private Button bLogin;

    @FXML
    private AnchorPane pLogin;

    @FXML
    private PasswordField tPassword;

    @FXML
    private TextField tUsername;

    private MainController mainController = new MainController();

    @FXML
    void authAccount(ActionEvent event) {

        try {
            switch (tUsername.getText()) {
                case "vende":
                    mainController.toPVendedor(event);
                    break;
                case "admin":
                    mainController.toPAdmin(event);
                    break;
                case "gerente":
                    mainController.toPGerente(event);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
