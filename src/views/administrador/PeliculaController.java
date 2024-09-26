package views.administrador;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.MainController;

public class PeliculaController {

    @FXML
    private ComboBox<?> cGenero;

    @FXML
    private ComboBox<?> cGenero1;

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Button bCerrar;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private ImageView posterImageView;

    @FXML
    private TextField tDirector;

    @FXML
    private TextField tDuracion;

    @FXML
    private TextField tNacionalidad;

    @FXML
    private TextField tNacionalidad1;

    @FXML
    private TextField tNombre;

    MainController mainController = new MainController();

    @FXML
    void logout(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void toPAdmin(ActionEvent event){
        try {
            mainController.toPAdmin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void altaPelicula(ActionEvent event) {

    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void uploadPoster(ActionEvent event) {

    }

    @FXML
    void formularioPelicula(ActionEvent event) {
        // Parent root;
        // try {
        // root = FXMLLoader.load(getClass().getResource("/Paneles/AltaPelicula.fxml"));
        // Scene scene = new Scene(root);
        // Pane panel = (Pane) scene.lookup("#mainPanel");
        // mainController.setUpScene(event, panel, scene);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrador/AltaPelicula.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.initModality(Modality.APPLICATION_MODAL); // Hacer la ventana modal
            stage.initStyle(StageStyle.UNDECORATED); // Estilo de ventana
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Muestra la ventana y espera a que se cierre

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
