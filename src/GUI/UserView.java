package GUI;

import DataHandling.RWUser;
import GUI.Actions.*;
import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import models.ColorScheme;


public class UserView {
    public UserView(Stage primaryStage, RWUser user){



        Today TODAY = new Today(primaryStage, user);
        DoneTasks DONE = new DoneTasks(primaryStage, user);
        ToDo TODO = new ToDo(primaryStage, user);
        Statistics STATISTICS = new Statistics(primaryStage, user);
        Settings SETTINGS = new Settings(primaryStage, user);

        JFXTabPane tabPane = new JFXTabPane();
        tabPane.prefWidthProperty().bind(primaryStage.widthProperty());
        tabPane.prefHeightProperty().bind(primaryStage.heightProperty());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab doneTasks = new Tab("Done tasks");
        Tab today = new Tab("Today");
        Tab toDo = new Tab("To Do");
        Tab statistics = new Tab("Statistics");
        Tab settings = new Tab("Settings");
        tabPane.getTabs().addAll(today, toDo, doneTasks, statistics, settings);

        today.setOnSelectionChanged(e -> {
            TODAY.refreshTable();
        });


        doneTasks.setContent(DONE.getDrawPane());
        today.setContent(TODAY.getDrawPane());
        toDo.setContent(TODO.getDrawPane());
        statistics.setContent(STATISTICS.getDrawPane());
        settings.setContent(SETTINGS.getDrawPane());



        if(user.getColorScheme() != null){
            setColorScheme(tabPane, user.getColorScheme());
        }
        Scene sc = new Scene(tabPane, 1100, 500);
        sc.getStylesheets().add(Settings.cssFile);
        primaryStage.setMinHeight(650);
        primaryStage.setScene(sc);
        primaryStage.setTitle(user.getUser().getName() + " " + user.getUser().getSurname());
        primaryStage.show();
    }

    private void setColorScheme(JFXTabPane root, ColorScheme colorScheme){
        root.setStyle("-fx-root:#" + colorScheme.getColor1() + ";" +
                "-fx-primary:#" + colorScheme.getColor2() + ";" +
                "-fx-secondary:#" + colorScheme.getColor3() + ";" +
                "-fx-outsideText:#" + colorScheme.getColor4() + ";" +
                "-fx-insideText:#" + colorScheme.getColor5() + ";");
    }
}