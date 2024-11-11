package views.administrador.pelicula;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import controllers.ClasificacionController;
import controllers.DirectorController;
import controllers.PeliculaController;
import controllers.GeneroController;
import database.DatabaseConnection;
import entities.Clasificacion;
import entities.Pelicula;
import entities.Director;
import entities.Genero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


import java.sql.Connection;

public class AltaPeliculaController {

    @FXML
    private TextField tNombre;

    @FXML
    private TextField tDuracion;

    @FXML
    private ComboBox<Clasificacion> cClasificacion;

    @FXML
    private TextField tDirector;

    @FXML
    private TextArea tSinopsis;

    @FXML
    private TextField tNacionalidad;

    @FXML
    private Button bCerrar;

    @FXML
    private Button bAgregarImagen;

    @FXML
    private String imagenPath = null;

    @FXML
    private ImageView posterPreview;

    @FXML
    private ComboBox<Genero> cGenero;

    PeliculaController peliculaController = new PeliculaController();
    DirectorController directorController = new DirectorController();
    
    @FXML
    private Connection connection;

    public AltaPeliculaController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @FXML
    public void initialize() {
        ClasificacionController clasificacionController = new ClasificacionController();
        GeneroController generoController = new GeneroController();

        // Cargar clasificaciones en el ComboBox
        ObservableList<Clasificacion> clasificaciones = FXCollections.observableArrayList(clasificacionController.findAll());
        cClasificacion.setItems(clasificaciones);

        // Cargar géneros en el ComboBox de Géneros
        ObservableList<Genero> generos = FXCollections.observableArrayList(generoController.findAll());
        cGenero.setItems(generos);

        // Validar que duración solo acepte números
        tDuracion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tDuracion.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    void agregarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de la película");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                // Mostrar la imagen seleccionada en el ImageView para vista previa
                Image image = new Image(selectedFile.toURI().toString());
                posterPreview.setImage(image);

                // Almacenar la ruta temporal de la imagen seleccionada para luego usarla al dar de alta la película
                imagenPath = selectedFile.getAbsolutePath();

                mensajeExito("Imagen seleccionada.");
            } catch (Exception e) {
                e.printStackTrace();
                mensajeError("Error al cargar la imagen seleccionada.");
            }
        }
    }

    private int obtenerIdSiguiente() {
        int siguienteId = 1; 

        String query = "SELECT MAX(id_pelicula) AS max_id FROM Pelicula"; 

        try (PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int ultimoId = rs.getInt("max_id");
                siguienteId = ultimoId + 1;
            }
            
        } catch (SQLException e) {
            System.out.println("Error al obtener el siguiente ID de la película: " + e.getMessage());
        }

        return siguienteId;
    }

    @FXML
void altaPelicula(ActionEvent event) {
    if (verificarCampos()) {
        
        if (tNombre.getText().length() > 200) {
            mensajeError("El nombre de la película no puede tener más de 200 caracteres.");
            return;
        }

        // Validar que la sinopsis no exceda los 350 caracteres
        if (tSinopsis.getText().length() > 350) {
            mensajeError("La sinopsis no puede tener más de 350 caracteres.");
            return;
        }

        // Validar que la nacionalidad no exceda los 100 caracteres
        if (tNacionalidad.getText().length() > 100) {
            mensajeError("La nacionalidad no puede tener más de 100 caracteres.");
            return;
        }
        // Solo proceder con el copiado de la imagen cuando se da de alta la película
        if (imagenPath != null) {
            try {
                // Define la carpeta de destino para las miniaturas
                String thumbnailsDirPath = "src/Resources/thumbnails";
                File thumbnailsDir = new File(thumbnailsDirPath);
                
                if (!thumbnailsDir.exists()) {
                    thumbnailsDir.mkdirs();
                }

                // Obtener el siguiente ID de la película para el nombre del archivo
                int peliculaId = obtenerIdSiguiente(); 
                String nuevoNombre = "Portada" + peliculaId + ".jpg";

                // Ruta de destino para la imagen copiada
                File destino = new File(thumbnailsDir, nuevoNombre);

                // Copiar la imagen seleccionada a la carpeta de miniaturas
                Files.copy(new File(imagenPath).toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Guardar la ruta de la imagen en el atributo correspondiente
                imagenPath = destino.getAbsolutePath();
                
                // Crear la película y almacenarla en la base de datos
                Pelicula pelicula = new Pelicula();
                pelicula.setNombre(tNombre.getText());
                pelicula.setDuracion(Integer.parseInt(tDuracion.getText()));
                pelicula.setIdClasificacion(cClasificacion.getValue().getIdClasificacion());
                pelicula.setSinopsis(tSinopsis.getText());
                pelicula.setNacionalidad(tNacionalidad.getText());
                pelicula.setImagen(imagenPath);

                String nombreDirector = tDirector.getText().trim();
                peliculaController.createPelicula(pelicula, nombreDirector);

                mensajeExito("Película creada con éxito!");
                CerrarFormulario(event);
            } catch (IOException e) {
                e.printStackTrace();
                mensajeError("Error al copiar la imagen seleccionada.");
            }
        } else {
            mensajeError("Debes seleccionar una imagen para la película.");
        }
    }
}
    
    private boolean verificarCampos() {
        if (tNombre == null || tNombre.getText().isEmpty()) {
            mensajeError("El nombre de la película es obligatorio.");
            return false;
        }

        if (tDuracion == null || tDuracion.getText().isEmpty()) {
            mensajeError("La duración es obligatoria.");
            return false;
        }

        if (cClasificacion.getValue() == null) {
            mensajeError("Debes seleccionar una clasificación.");
            return false;
        }

        if (tDirector == null || tDirector.getText().isEmpty()) {
            mensajeError("El nombre del director es obligatorio.");
            return false;
        }

        if (tSinopsis == null || tSinopsis.getText().isEmpty()) {
            mensajeError("La sinopsis es obligatoria.");
            return false;
        }

        return true;
    }

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }

    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}