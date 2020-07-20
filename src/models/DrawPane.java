package models;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DrawPane extends GridPane {
    public DrawPane(Stage primaryStage){
        super();
        super.prefHeightProperty().bind(primaryStage.heightProperty());
        super.prefWidthProperty().bind(primaryStage.widthProperty());
    }
}
