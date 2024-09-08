import java.io.IOException;

import Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Paneles/Login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MainController mainController = new MainController();

        Scene scene = new Scene(root);

        Pane panel = (Pane) scene.lookup("#mainPanel");
        Pane mainButton = (Pane) scene.lookup("#mainButton");
        mainController.configureButtonHoverEffect(mainButton);
        
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            double stageWidth = newValue.doubleValue();
            AnchorPane.setLeftAnchor(panel, (stageWidth - panel.getPrefWidth()) / 2);
            AnchorPane.setRightAnchor(panel, (stageWidth - panel.getPrefWidth()) / 2);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            double stageHeight = newValue.doubleValue();
            AnchorPane.setTopAnchor(panel, 20.0);
            AnchorPane.setTopAnchor(panel, (stageHeight - panel.getPrefHeight()) / 2);
        });

        primaryStage.setTitle("Ticket Suit");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
