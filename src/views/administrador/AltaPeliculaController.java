package views.administrador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AltaPeliculaController {

    @FXML
    private Button bCerrar;

    @FXML
    private ComboBox<?> cCalificacion;

    @FXML
    private ComboBox<?> cGenero;

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

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
    private TextField tNombre;

    @FXML
    private TextField tSinopsis;

    @FXML
    private TextField tUrlPoster;

    @FXML
    void submitForm(ActionEvent event) {

    }

    @FXML
    void uploadPoster(ActionEvent event) {

    }

    @FXML
    void altaPelicula(ActionEvent event) {

    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

}
