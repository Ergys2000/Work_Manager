package models;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class CustomTableView<T> extends VBox {
    private ArrayList<T> objects;
    private ArrayList<String> names;
    private ArrayList<ColumnCreator<T, ? extends Control>> functions;
    private HBox headers;

    public static String help(){
        return "Step 1: Clear the table via table.clear()" +
                "Step 2: Set the items by table.setItems(items)" +
                "Step 3: Populate the table by table.populate()";
    }

    public CustomTableView(){
        super();
        objects = new ArrayList<>();
        functions = new ArrayList<>();
        names = new ArrayList<>();

        headers = new HBox();
        headers.getStyleClass().add("header");
        headers.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(headers);
        this.getStyleClass().add("customTable");
    }


    public <E extends Control> void addColumn(String name, ColumnCreator<T, E> function){
        this.names.add(name);
        this.functions.add(function);
        // create the label for the column
        Label label = new Label(name);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial", 20));
        label.prefWidthProperty().bind(headers.widthProperty().multiply(function.getWidth()));

        headers.getChildren().add(label);

    }

    public void clear(){
        this.getChildren().remove(1, this.getChildren().size());
        this.objects.clear();
    }


    public ArrayList<T> getItems(){return this.objects;}

    public void setItems(ArrayList<T> items){
        this.objects = (ArrayList<T>) items.clone();
    }

    public void populate(){
        if(objects.isEmpty()){
            HBox empty = new HBox();
            empty.prefHeightProperty().bind(this.heightProperty().multiply(0.7));
            empty.setAlignment(Pos.CENTER);
            empty.getChildren().add(new Label("The table is empty"));
            this.getChildren().add(empty);
        }else {
            this.getChildren().remove(1, this.getChildren().size());
            for(T item: objects){

                // creating the hbox which will hold the task
                HBox box = new HBox();
                box.prefWidthProperty().bind(this.widthProperty());
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(10, 16, 10 , 16));

                int columnsNumber = functions.size();
                for(ColumnCreator<T, ? extends Control> function:functions){
                    Control node = function.call(item);
                    node.prefWidthProperty().bind(box.widthProperty().multiply(function.getWidth()));
                    node.getStyleClass().add("customTable-cell");
                    box.getChildren().add(node);
                }
                this.getChildren().add(box);
            }
        }
    }

}
