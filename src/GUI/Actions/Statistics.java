package GUI.Actions;

import DataHandling.RWUser;
import com.jfoenix.controls.JFXComboBox;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.ColumnCreator;
import models.CustomTableView;
import models.DrawPane;
import models.Task;

import java.time.LocalDate;
import java.util.ArrayList;

public class Statistics {
    RWUser user;
    DrawPane drawPane;

    public Statistics(Stage primaryStage, RWUser u){
        user = u;
        drawPane = new DrawPane(primaryStage);
        double[] weekDaysWork = {0, 0, 0, 0, 0, 0, 0};

        NumberAxis yAxis = new NumberAxis(0, 16, 1);
        CategoryAxis xAxis = new CategoryAxis();

        BarChart<String, Number> barchart = new BarChart<>(xAxis, yAxis);

        yAxis.setLabel("Hours worked");
        xAxis.setLabel("Day of the week");

        LocalDate startOfWeek = getStartofWeek(LocalDate.now());
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Task t: user.getTasks()){ // getting the data that we will add to the chart
            if(t.getDate().isAfter(startOfWeek) || t.getDate().equals(startOfWeek)){
                if(t.getDate().getDayOfWeek().toString().equals("MONDAY")) weekDaysWork[0] += t.getTotalLength();
                else if(t.getDate().getDayOfWeek().toString().equals("TUESDAY")) weekDaysWork[1] += t.getTotalLength();
                else if(t.getDate().getDayOfWeek().toString().equals("WEDNESDAY")) weekDaysWork[2] += t.getTotalLength();
                else if(t.getDate().getDayOfWeek().toString().equals("THURSDAY")) weekDaysWork[3] += t.getTotalLength();
                else if(t.getDate().getDayOfWeek().toString().equals("FRIDAY")) weekDaysWork[4] += t.getTotalLength();
                else if(t.getDate().getDayOfWeek().toString().equals("SATURDAY")) weekDaysWork[5] += t.getTotalLength();
                else weekDaysWork[6] += t.getTotalLength();
            }
        }
        // adding the data to the chart
        series1.getData().add(new XYChart.Data<>("Monday", weekDaysWork[0]));
        series1.getData().add(new XYChart.Data<>("Tuesday", weekDaysWork[1]));
        series1.getData().add(new XYChart.Data<>("Wednesday", weekDaysWork[2]));
        series1.getData().add(new XYChart.Data<>("Thursday", weekDaysWork[3]));
        series1.getData().add(new XYChart.Data<>("Friday", weekDaysWork[4]));
        series1.getData().add(new XYChart.Data<>("Saturday", weekDaysWork[5]));
        series1.getData().add(new XYChart.Data<>("Sunday", weekDaysWork[6]));

        // creating the labels for each unique task done this week


        barchart.getData().add(series1);
        barchart.setTitle("Amount of work done this week");

        JFXComboBox<String> cmb = new JFXComboBox<>();
        cmb.getItems().add("By Day");
        cmb.getItems().add("By Week");
        cmb.getItems().add("By Month");

        drawPane.getChildren().clear();
        drawPane.setAlignment(Pos.CENTER);
        drawPane.add(barchart, 0, 0);
        drawPane.add(cmb, 1, 0);

    }

    private LocalDate getStartofWeek(LocalDate today){
        if(today.getDayOfWeek().toString().equals("MONDAY")) return today;
        else if(today.getDayOfWeek().toString().equals("TUESDAY")) return today.minusDays(1);
        else if(today.getDayOfWeek().toString().equals("WEDNESDAY")) return today.minusDays(2);
        else if(today.getDayOfWeek().toString().equals("THURSDAY")) return today.minusDays(3);
        else if(today.getDayOfWeek().toString().equals("FRIDAY")) return today.minusDays(4);
        else if(today.getDayOfWeek().toString().equals("SATURDAY")) return today.minusDays(5);
        else return today.minusDays(6);
    }


    private CustomTableView<Task> createTable(){
        CustomTableView<Task> table = new CustomTableView<>();
        table.addColumn("Task name", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                return new Label(task.getName());
            }

            @Override
            public double getWidth() {
                return 0.5;
            }
        });
        table.addColumn("Work done(hours)", new ColumnCreator<Task, Control>() {
            @Override
            public Control call(Task task) {
                return new Label(task.getTotalLength() + "");
            }

            @Override
            public double getWidth() {
                return 0.5;
            }
        });
        return table;
    }

    public DrawPane getDrawPane(){return drawPane;}
}
