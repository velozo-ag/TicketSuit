package views.administrador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AltaFuncionController {

    @FXML
    private ComboBox<?> cPelicula;

    @FXML
    private ComboBox<?> cTipoFuncion;

    @FXML
    private ComboBox<?> cTipoFuncion1;

    @FXML
    private Pane formPanel;

    @FXML
    private AnchorPane pFormulario;

    @FXML
    private TextField tHorarioCierre;

    @FXML
    private TextField tHorarioInicio;

    @FXML
    private TextField tNombre;

    @FXML
    void submitForm(ActionEvent event) {

    }

}


