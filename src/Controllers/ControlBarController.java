package Controllers;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ControlBarController {

    @FXML
    private HBox controlBar;

    private double xOffset = 0;
    private double yOffset = 0;

    private Stage stage;

    @FXML
    void pressBar(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void dragBar(MouseEvent event) {
        stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();

        if (stage != null) {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        }
    }

    @FXML
    void releaseBar(MouseEvent event) {
        stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();
        
        if (stage != null) {
            // Obtener los límites de la pantalla
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Coordenadas actuales de la ventana
            double stageX = stage.getX();
            double stageY = stage.getY();
            double stageWidth = stage.getWidth();
            double stageHeight = stage.getHeight();

            // Proximidad en píxeles para considerar que está en el borde
            double proximity = 20;

            // Comprobar si la ventana está lo suficientemente cerca de los bordes para
            // maximizar
            if (stageX <= screenBounds.getMinX() + proximity || // Izquierda
                    stageX + stageWidth >= screenBounds.getMaxX() - proximity || // Derecha
                    stageY <= screenBounds.getMinY() + proximity || // Arriba
                    stageY + stageHeight >= screenBounds.getMaxY() - proximity) { // Abajo

                // Maximizar la ventana
                stage.setMaximized(true);
            }
        }
    }
}
