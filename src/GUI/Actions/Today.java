package GUI.Actions;

import DataHandling.RWUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Today {
    private RWUser user;
    private DrawPane drawPane;
    private long lastTimerCall;
    private JFXTextField focusTf;
    private JFXTextField breakTf;
    private CustomTableView<Task> table;
    private HBox bottomBox;

    public Today(Stage primaryStage, RWUser u) {
        user = u;
        drawPane = new DrawPane(primaryStage);

        LocalDate today = LocalDate.now();

        // creating and configuring tha table
        table = createTable();
        ArrayList<Task> tableItems = new ArrayList<>();

        // add all the tasks on the table that are due for today and remove those that have less than 5 minutes left
        ArrayList<Task> toRemove = new ArrayList<>();
        for (Task t : user.getUndoneTasks()){
            if (t.getDate().equals(today))
                if (t.getMinutesLeft() >= 5) tableItems.add(t);
                else toRemove.add(t);
        }
        for(Task t: toRemove){
            user.did(t);
        }

        table.setItems(tableItems);
        table.populate();

        Label focusLb = new Label("Focus length: ");
        focusTf = new JFXTextField();
        Label breakLb = new Label("Break length: ");
        breakTf = new JFXTextField();
        focusTf.setText("25"); breakTf.setText("5");


        // creating the bottom elements of the screen
        bottomBox = new HBox(15);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.2));
        bottomBox.prefWidthProperty().bind(drawPane.widthProperty());
        bottomBox.getChildren().addAll(focusLb, focusTf, breakLb, breakTf);

        // wrappning the table in a scrollpane so that we can scroll if there are a lot of elements
        ScrollPane sp = new ScrollPane(table);
        sp.prefWidthProperty().bind(drawPane.widthProperty());
        table.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.8));
        table.prefWidthProperty().bind(sp.widthProperty());


        // adding it to the drawpane
        drawPane.getChildren().clear();
        drawPane.add(sp, 0 , 0);
        drawPane.add(bottomBox, 0, 1);
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
                return 0.3;
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
                return 0.3;
            }
        });

        table.addColumn("Minutes Left", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                Label minsLeft = new Label(task.getMinutesLeft()+"");
                return minsLeft;

            }
            @Override
            public double getWidth() {
                return 0.3;
            }
        });

        table.addColumn("", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                JFXButton btn = new JFXButton("Do");
                btn.getStyleClass().add("button-flat");
                btn.setOnAction(e -> {
                    try{
                        int focus = Integer.parseInt(focusTf.getText());
                        int Break = Integer.parseInt(breakTf.getText());
                        createTimer(task, focus, Break);
                    }catch (Exception ex){
                        new CustomAlert("Please specify the focus and break time!", "information");
                    }
                });
                return btn;

            }
            @Override
            public double getWidth() {
                return 0.1;
            }
        });

        return table;
    }

    public void refreshTable(){
        ArrayList<Task> tableItems = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // add all the tasks on the table that are due for today and remove those that have less than 5 minutes left
        ArrayList<Task> toRemove = new ArrayList<>();
        for (Task t : user.getUndoneTasks()){
            if (t.getDate().equals(today))
                if (t.getMinutesLeft() >= 5) tableItems.add(t);
                else toRemove.add(t);
        }
        for(Task t: toRemove){
            user.did(t);
        }
        table.clear();
        table.setItems(tableItems);
        table.populate();
    }

    public void resetDrawpane(){
        ArrayList<Task> tableItems = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // add all the tasks on the table that are due for today and remove those that have less than 5 minutes left
        ArrayList<Task> toRemove = new ArrayList<>();
        for (Task t : user.getUndoneTasks()){
            if (t.getDate().equals(today))
                if (t.getMinutesLeft() >= 5) tableItems.add(t);
                else toRemove.add(t);
        }
        for(Task t: toRemove){
            user.did(t);
        }
        table.clear();
        table.setItems(tableItems);
        table.populate();

        ScrollPane sp = new ScrollPane(table);
        sp.prefWidthProperty().bind(drawPane.widthProperty());
        table.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.8));
        table.prefWidthProperty().bind(sp.widthProperty());

        drawPane.getChildren().clear();
        drawPane.add(sp, 0, 0);
        drawPane.add(bottomBox, 0 , 1);

    }

    public void createTimer(Task task, int focus, int Break){
        VisualTimer gauges = new VisualTimer(focus, Break); // create the gauges
        Pane timerPane = gauges.getTimer(); // return the pane which contains the gauges

        timerPane.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.8)); // fit the pane to the window
        timerPane.prefWidthProperty().bind(drawPane.widthProperty());

        JFXButton cancel = new JFXButton("Cancel");
        cancel.getStyleClass().setAll("button-flat");

        HBox buttonBox = new HBox(15);// will hold the cancel button
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.prefHeightProperty().bind(drawPane.heightProperty().multiply(0.2));
        buttonBox.prefWidthProperty().bind(drawPane.widthProperty());
        buttonBox.getChildren().add(cancel);

        // will hold both the clocks and the buttons
        VBox box = new VBox(15);
        box.prefWidthProperty().bind(drawPane.widthProperty());
        box.prefHeightProperty().bind(drawPane.heightProperty());

        box.getChildren().addAll(timerPane, buttonBox);
        box.setAlignment(Pos.CENTER);

        drawPane.getChildren().clear();
        drawPane.add(box, 0, 0);

        lastTimerCall = System.nanoTime();// we use this to keep track of the time in nanoseconds
        AnimationTimer timer = new AnimationTimer() {
            boolean flag = true;
            @Override public void handle(long now) {// now represents the time at the moment of calling this method
                if (now > lastTimerCall + 1_000_000_000l) { // lastTimerCall holds the last "now" value
                                                            // if a full second passed, in other words
                    gauges.setSmallGauge(gauges.getSmallGaugeValue() + 1);// increment the small clock
                    if(gauges.getSmallGaugeValue() == 60){// if the small clock does a full minute
                        gauges.setSmallGauge(0);// reset it
                        gauges.setBigGauge(gauges.getBigGaugeValue() + 1);// increment the big clock
                    }
                    if(gauges.getBigGaugeValue() == focus && flag) { // if we focus for that amount of time
                        task.setMinutesLeft(focus);// substract that amount of time from the task
                        user.update();// update the user
                        flag = false;// this keeps it from substracting every 60 seconds that the big clock is at that
                                    // particular spot
                    }else if(gauges.getBigGaugeValue() == focus + Break){// reset the drawpane and update the table
                        this.stop();
                        resetDrawpane();
                    }
                    lastTimerCall = now;
                }
            }
        };
        timer.start(); // starts the timer defined above

        cancel.setOnAction(e -> {
            timer.stop();
            resetDrawpane();
        });
    }

    public DrawPane getDrawPane(){return drawPane;}
}
