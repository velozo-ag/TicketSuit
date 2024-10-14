package views;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static final String stylePath = "/styles/Styles.css"; 

    public static void setScene(Parent newRoot, Stage stage) {
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(newRoot);
            stage.setScene(scene);
            
            scene.getStylesheets().add(SceneManager.class.getResource(stylePath).toExternalForm());
            System.out.println("cargando estilos nuevamente");
        } else {
            scene.setRoot(newRoot);
            
            if (!scene.getStylesheets().contains(SceneManager.class.getResource(stylePath).toExternalForm())) {
                System.out.println("cargando estilos nuevamente");
                scene.getStylesheets().add(SceneManager.class.getResource(stylePath).toExternalForm());
            }
        }
    }
}
