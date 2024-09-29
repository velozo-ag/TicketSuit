package views.vendedor;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import views.MainController;

public class CarteleraController {

    @FXML
    private AnchorPane spPeliculas;

    private MainController mainController = new MainController();

    @FXML
    public void initialize() {
        // spPeliculas.setPrefHeight((double) 800);;

        double panelWidth = 280;
        double gap = 14.8;
        int totalPanels = 5; // Total peliculas
        int panelsPerRow = 4;

        int rows = (int) Math.ceil((double) totalPanels / panelsPerRow);
        double totalHeight = gap + rows * (400 + gap);
        spPeliculas.setPrefHeight(totalHeight);

        for (int i = 0; i < totalPanels; i++) {
            Pane panel = new Pane();
            panel.setPrefWidth(panelWidth);
            panel.setPrefHeight(400);
            panel.getStyleClass().add("cartelera");

            int column = i % panelsPerRow;
            int row = i / panelsPerRow;

            double positionX = gap + column * (panelWidth + gap);
            double positionY = gap + row * (400 + gap);

            AnchorPane.setLeftAnchor(panel, positionX);
            AnchorPane.setTopAnchor(panel, positionY);

            spPeliculas.getChildren().add(panel);
        }

    }

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
