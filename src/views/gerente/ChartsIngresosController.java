package views.gerente;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import views.MainController;
/**1 */
public class ChartsIngresosController {

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

    @FXML
    void ChartsIngresos(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsIngresos.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChartsAsistencias(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsAsistencia.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChartsLogistica(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsLogistica.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPGerente(ActionEvent event) {
        try {
            mainController.toPGerente(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}