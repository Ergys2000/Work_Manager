package GUI.Actions;

import DataHandling.RWUser;
import com.jfoenix.controls.*;
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

public class ToDo {
    private DrawPane drawPane;
    private RWUser user;
    private CustomTableView<Task> table;

    public ToDo(Stage primaryStage, RWUser u){
        drawPane = new DrawPane(primaryStage);
        user = u;
        // Creating the table
        ArrayList<Task> tasks = user.getUndoneTasks();

        table =  createTable();
        table.setItems(tasks);
        table.populate();


        JFXButton add = new JFXButton("Add");
        add.getStyleClass().setAll("button-flat");

        JFXDatePicker start = new JFXDatePicker();
        JFXDatePicker end = new JFXDatePicker();

        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().setAll("button-flat");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.2));
        buttonBox.prefWidthProperty().bind(drawPane.widthProperty());
        buttonBox.getChildren().addAll(add, start, end, refresh);

        refresh.setOnAction(e -> {
            try{
                table.clear();
                LocalDate startDate = start.getValue();
                LocalDate endDate = end.getValue();
                ArrayList<Task> newList = new ArrayList<>();
                for(Task task: tasks){
                    if((task.getDate().isAfter(startDate)|| task.getDate().equals(startDate)) &&
                            (task.getDate().isBefore(endDate) || task.getDate().equals(endDate)))
                        newList.add(task);
                }
                table.setItems(newList);
                table.populate();
            }catch (Exception ex){
                new CustomAlert("Please fill in the date boxes!", "error");
            }
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

    private void refreshTable(){
        table.clear();
        table.setItems(user.getUndoneTasks());
        table.populate();
    }

    public CustomTableView<Task> createTable(){

        CustomTableView<Task> table = new CustomTableView<>();

        table.addColumn("Name", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label nameLb = new Label(task.getName());
                return nameLb;
            }

            @Override
            public double getWidth() {
                return 0.225;
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
                return 0.225;
            }
        });

        table.addColumn("Date", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label duration = new Label(task.getDate().toString());
                return duration;
            }
            @Override
            public double getWidth() {
                return 0.225;
            }
        });

        table.addColumn("Already Done?", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                JFXCheckBox box = new JFXCheckBox();
                box.setOnAction(e -> {
                    if(box.isSelected()){
                        user.did(task);
                        user.update();
                    }else{
                        user.getTasks().remove(task);
                        user.getUndoneTasks().add(task);
                        user.update();
                    }
                });
                return box;
            }
            @Override
            public double getWidth() {
                return 0.225;
            }
        });

        table.addColumn("", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                JFXButton btn = new JFXButton("Edit");
                btn.setOnAction(e -> editTask(task));
                btn.getStyleClass().add("button-flat");
                return btn;
            }
            @Override
            public double getWidth() {
                return 0.1;
            }
        });


        return table;
    }

    public void add(){
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
        add.getStyleClass().setAll("button-flat");
        JFXButton cancel = new JFXButton("Cancel");
        cancel.getStyleClass().setAll("button-flat");

        add.setOnAction(e -> {
            try{
                String name = taskTf.getText();
                LocalTime start = startTf.getValue();
                LocalTime end = endTf.getValue();
                LocalDate date = dateTf.getValue();
                Task task = new Task(start, end, name, date);
                try {
                    if(!start.isAfter(end)){
                        user.toDo(task);
                        user.update();
                        new CustomAlert("Added successfuly", "information");
                        refreshTable();
                    }else new CustomAlert("Start cannot be after end!", "error");
                } catch (IOException ex) {
                    new CustomAlert(ex.getMessage(), "information");
                }
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
        primaryStage.setTitle("Add new task");
        primaryStage.show();
    }

    public void editTask(Task task){

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
        taskTf.setText(task.getName());

        Label lb2 = new Label("Start Time : "); JFXTimePicker startTf = new JFXTimePicker();
        startTf.setValue(task.getStart());

        Label lb3 = new Label("End Time : "); JFXTimePicker endTf = new JFXTimePicker();
        endTf.setValue(task.getEnd());

        Label lb4 = new Label("Date : "); JFXDatePicker dateTf = new JFXDatePicker();
        dateTf.setValue(task.getDate());

        JFXButton done = new JFXButton("Done");
        done.getStyleClass().setAll("button-flat");

        JFXButton cancel = new JFXButton("Cancel");
        cancel.getStyleClass().setAll("button-flat");

        JFXButton delete = new JFXButton("Delete");
        delete.getStyleClass().setAll("button-flat");

        done.setOnAction(e -> {
            try{
                String name = taskTf.getText();
                LocalTime start = startTf.getValue();
                LocalTime end = endTf.getValue();
                LocalDate date = dateTf.getValue();
                if(start.isAfter(end)){
                    new CustomAlert("Start time cannot be after end time!", "error");
                }else {
                    task.setName(name);
                    task.setStart(start);
                    task.setEnd(end);
                    task.setDate(date);
                    user.update();
                    new CustomAlert("Updated Successfully", "information");
                    refreshTable();
                }

            } catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
                ex.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            primaryStage.close();
        });

        delete.setOnAction(e -> {
            user.getUndoneTasks().remove(task);
            user.update();
            refreshTable();
            primaryStage.close();
            new CustomAlert("The task was deleted!", "information");
        });


        // setting up the placement of the nodes
        VBox v1 = new VBox(16); // this will hold the labels
        VBox v2 = new VBox(5); // this will hold the textfields
        v1.getChildren().addAll(lb1, lb2, lb3, lb4);
        v2.getChildren().addAll(taskTf, startTf, endTf, dateTf);
        HBox hBox = new HBox(10); // this will hold the buttons
        hBox.getChildren().addAll(done, cancel, delete);

        grid.add(v1, 0, 0);
        grid.add(v2, 1, 0);
        grid.add(hBox, 1,1);

        if(user.getColorScheme() != null){
            setColorScheme(grid, user.getColorScheme());
        }

        Scene sc = new Scene(grid, 600, 400);
        sc.getStylesheets().add(Settings.cssFile);

        primaryStage.setScene(sc);
        primaryStage.setTitle("Edit");
        primaryStage.show();
    }

    private void setColorScheme(Pane root, ColorScheme colorScheme){
        root.setStyle("-fx-root:#" + colorScheme.getColor1() + ";" +
                "-fx-primary:#" + colorScheme.getColor2() + ";" +
                "-fx-secondary:#" + colorScheme.getColor3() + ";" +
                "-fx-outsideText:#" + colorScheme.getColor4() + ";" +
                "-fx-insideText:#" + colorScheme.getColor5() + ";");
    }

    public DrawPane getDrawPane(){return drawPane;}
}
