package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AltaPeliculaController {

    @FXML
    private ComboBox<?> cGenero;

    @FXML
    private Pane formPanel;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private ImageView posterImageView;

    @FXML
    private TextField tCalificacion;

    @FXML
    private TextField tDirector;

    @FXML
    private TextField tDuracion;

    @FXML
    private TextField tNacionalidad;

    @FXML
    private TextField tNombre;

    @FXML
    void submitForm(ActionEvent event) {

    }

    @FXML
    void uploadPoster(ActionEvent event) {

    }

}

