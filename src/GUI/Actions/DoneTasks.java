package GUI.Actions;

import DataHandling.RWUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DoneTasks {
    DrawPane drawPane;
    RWUser user;
    Stage primaryStage;
    CustomTableView<Task> table;

    // --- Contains the bottom part of the drawpane
    HBox buttonBox;
    JFXButton add;
    JFXDatePicker start;
    JFXDatePicker end;
    JFXButton refresh;

    public DoneTasks(Stage primaryStage, RWUser u){

        this.primaryStage = primaryStage;
        this.drawPane = new DrawPane(primaryStage);
        this.user = u;
        ArrayList<Task> tasks = user.getTasks();

        table = createTable();
        table.setItems(tasks);
        table.populate();


        add = new JFXButton("Add");
        start = new JFXDatePicker();
        end = new JFXDatePicker();
        refresh = new JFXButton("Refresh");

        refresh.getStyleClass().setAll("button-flat");
        add.getStyleClass().setAll("button-flat");

        buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.2));
        buttonBox.prefWidthProperty().bind(drawPane.widthProperty());
        buttonBox.getChildren().addAll(add, start, end, refresh);

        refresh.setOnAction(e -> {
            refreshDate();
        });

        add.setOnAction(e -> {
            add();
        });

        ScrollPane sp = new ScrollPane(table);
        sp.prefWidthProperty().bind(drawPane.widthProperty());
        sp.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.8));
        table.prefWidthProperty().bind(sp.widthProperty());
        table.prefHeightProperty().bind(sp.heightProperty());

        drawPane.getChildren().clear();
        drawPane.add(sp, 0, 0);
        drawPane.add(buttonBox, 0, 1);


    }

    private CustomTableView<Task> createTable(){

        CustomTableView<Task> table = new CustomTableView<>();

        table.addColumn("Name", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label nameLb = new Label(task.getName());
                return nameLb;
            }

            @Override
            public double getWidth() {
                return 0.25;
            }
        });

        table.addColumn("Duration", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label duration = new Label(task.getStart().toString() + " - " + task.getEnd().toString());
                return duration;
            }
            @Override
            public double getWidth() {
                return 0.25;
            }
        });

        table.addColumn("Date", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label date = new Label(task.getDate().toString());
                return date;
            }
            @Override
            public double getWidth() {
                return 0.25;
            }
        });

        table.addColumn("Total length", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label minsleft = new Label(task.getTotalLength()+"");
                return minsleft;
            }
            @Override
            public double getWidth() {
                return 0.25;
            }
        });
        return table;
    }

    private void add(){
        Stage primaryStage = new Stage();
        // Create the pane layout
        GridPane grid = new GridPane();
        grid.prefHeightProperty().bind(primaryStage.heightProperty());
        grid.prefWidthProperty().bind(primaryStage.widthProperty());
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);

        // Creating the nodes needed
        Label lb1 = new Label("Task Name : "); JFXTextField taskTf = new JFXTextField();
        Label lb2 = new Label("Start Time : "); JFXTimePicker startTf = new JFXTimePicker();
        Label lb3 = new Label("End Time : "); JFXTimePicker endTf = new JFXTimePicker();
        Label lb4 = new Label("Date : "); JFXDatePicker dateTf = new JFXDatePicker();
        dateTf.setValue(LocalDate.now());
        JFXButton add = new JFXButton("Add");
        add.getStyleClass().add("button-flat");
        JFXButton cancel = new JFXButton("Cancel");
        cancel.getStyleClass().add("button-flat");

        add.setOnAction(e -> {
            try{
                String name = taskTf.getText();
                LocalTime start = startTf.getValue();
                LocalTime end = endTf.getValue();
                LocalDate date = dateTf.getValue();
                if(start.isBefore(end)){
                    Task task = new Task(start,end, name, date);
                    try {
                        user.addDone(task);
                        user.update();
                        new CustomAlert("Added successfuly", "information");
                        refreshTable();
                    } catch (IOException ex) {
                        new CustomAlert(ex.getMessage(), "information");
                    }
                }else new CustomAlert("Start can't be after end time.", "error");
            } catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }

        });
        cancel.setOnAction(e -> {
            primaryStage.close();
        });


        // setting up the placement of the nodes
        VBox v1 = new VBox(16); // this will hold the labels
        VBox v2 = new VBox(5); // this will hold the textfields
        v1.getChildren().addAll(lb1, lb2, lb3, lb4);
        v2.getChildren().addAll(taskTf, startTf, endTf, dateTf);
        HBox hBox = new HBox(10); // this will hold the buttons
        hBox.getChildren().addAll(add, cancel);

        grid.add(v1, 0, 0);
        grid.add(v2, 1, 0);
        grid.add(hBox, 1,1);

        if(user.getColorScheme() != null){
            setColorScheme(grid, user.getColorScheme());
        }

        Scene sc = new Scene(grid, 600, 400);
        sc.getStylesheets().add(Settings.cssFile);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Add");
        primaryStage.show();
    }
    private void setColorScheme(Pane root, ColorScheme colorScheme){
        root.setStyle("-fx-root:#" + colorScheme.getColor1() + ";" +
                "-fx-primary:#" + colorScheme.getColor2() + ";" +
                "-fx-secondary:#" + colorScheme.getColor3() + ";" +
                "-fx-outsideText:#" + colorScheme.getColor4() + ";" +
                "-fx-insideText:#" + colorScheme.getColor5() + ";");
    }

    public void refreshTable(){
        table.getItems().clear();
        for(Task t: user.getTasks())
            table.getItems().add(t);
    }

    private void refreshDate(){
        try{
            LocalDate startDate = start.getValue();
            LocalDate endDate = end.getValue();
            table.clear();
            ArrayList<Task> newTasks = new ArrayList<>();
            for(Task t: user.getTasks()){
                if((t.getDate().isAfter(startDate) || t.getDate().equals(startDate)) &&
                        ( t.getDate().isBefore(endDate) || t.getDate().equals(endDate)))
                    newTasks.add(t);
            }
            table.setItems(newTasks);
            table.populate();
        }catch (Exception ex){
            new CustomAlert("Please fill in the date boxes!", "error");
        }
    }

    public DrawPane getDrawPane(){
        return drawPane;
    }
}