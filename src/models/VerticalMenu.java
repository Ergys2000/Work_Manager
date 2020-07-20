package models;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VerticalMenu extends VBox {
    public VerticalMenu(Stage primaryStage){
        super();
        super.prefHeightProperty().bind(primaryStage.heightProperty());
        super.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.25));
        super.setAlignment(Pos.CENTER);
        super.getStyleClass().add("vertical-menu");
    }

    public void add(Button... nodes){
        for(Button n: nodes){
            super.getChildren().add(n);
            n.prefWidthProperty().bind(super.widthProperty());
            n.getStyleClass().add("vertical-menu-btn");
        }
    }

}
