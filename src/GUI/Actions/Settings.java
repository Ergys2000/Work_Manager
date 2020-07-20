package GUI.Actions;

import DataHandling.RWUser;
import GUI.LogIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.ColorScheme;
import models.CustomAlert;
import models.VerticalMenu;


public class Settings {
    RWUser user;
    GridPane mainPane;
    GridPane innerPane;
    VerticalMenu verticalMenu;
    public static String cssFile = "resources/dark.css";

    public Settings(Stage primaryStage, RWUser user){
        mainPane = new GridPane();
        this.user = user;
        innerPane = new GridPane();
        innerPane.setAlignment(Pos.CENTER);
        innerPane.setVgap(20);
        innerPane.setHgap(20);
        innerPane.prefHeightProperty().bind(mainPane.heightProperty());
        innerPane.prefWidthProperty().bind(mainPane.widthProperty().multiply(0.8));

        verticalMenu = new VerticalMenu(primaryStage);
        JFXButton logOut = new JFXButton("Log out");
        JFXButton info = new JFXButton("Info");
        JFXButton account = new JFXButton("Account");
        JFXButton style = new JFXButton("Style");
        verticalMenu.add(account, info, style, logOut);

        account.setOnAction(e -> account());

        style.setOnAction(e -> style());

        info.setOnAction(e -> info());

        logOut.setOnAction(e -> {
            try {
                new LogIn().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        mainPane.add(verticalMenu, 0, 0);
        mainPane.add(innerPane, 1, 0);
    }

    public GridPane getDrawPane(){
        return mainPane;
    }

    public static String getStyle(){
        return cssFile;
    }

    public static void setStyle(String file){
        cssFile = file;
    }

    public void style(){
        VBox vbox = new VBox(15);
        Label lb0 = new Label("Create your own color scheme: ");
        HBox color1Box = new HBox(15);
        HBox color2Box = new HBox(15);
        HBox color3Box = new HBox(15);
        HBox color4Box = new HBox(15);
        HBox color5Box = new HBox(15);

        JFXButton done = new JFXButton("Create"); done.getStyleClass().add("button-flat");
        JFXButton reset = new JFXButton("Reset"); reset.getStyleClass().add("button-flat");


        JFXColorPicker cp1 = new JFXColorPicker();
        JFXColorPicker cp2 = new JFXColorPicker();
        JFXColorPicker cp3 = new JFXColorPicker();
        JFXColorPicker cp4 = new JFXColorPicker();
        JFXColorPicker cp5 = new JFXColorPicker();
        if(user.getColorScheme() != null){
            cp1.setValue(Color.web("#" + user.getColorScheme().getColor1()));
            cp2.setValue(Color.web("#" + user.getColorScheme().getColor2()));
            cp3.setValue(Color.web("#" + user.getColorScheme().getColor3()));
            cp4.setValue(Color.web("#" + user.getColorScheme().getColor4()));
            cp5.setValue(Color.web("#" + user.getColorScheme().getColor5()));
        }

        Label lb1 = new Label("-> The background color of the window.");
        Label lb2 = new Label("-> The primary color of the tabs.");
        Label lb3 = new Label("-> The secondary color of the tabs, which is also the color of the buttons.");
        Label lb4 = new Label("-> The color of the text outside buttons and tabs.");
        Label lb5 = new Label("-> The color of the text inside buttons and tabs.");

        color1Box.getChildren().addAll(cp1, lb1);
        color2Box.getChildren().addAll(cp2, lb2);
        color3Box.getChildren().addAll(cp3, lb3);
        color4Box.getChildren().addAll(cp4, lb4);
        color5Box.getChildren().addAll(cp5, lb5);


        vbox.getChildren().addAll(lb0, color1Box, color2Box, color3Box, color4Box,
                color5Box, done, reset);

        innerPane.getChildren().clear();
        innerPane.add(vbox, 0, 0);

        reset.setOnAction(e -> {
            user.setColorScheme(null);
            user.update();
            new CustomAlert("Scheme added successfully! Please log out and in again.", "information");
        });

        done.setOnAction(e -> {
            String color1 = cp1.getValue().toString();
            String color2 = cp2.getValue().toString();
            String color3 = cp3.getValue().toString();
            String color4 = cp4.getValue().toString();
            String color5 = cp5.getValue().toString();
            ColorScheme colorScheme = new ColorScheme(color1.substring(2, color1.length()-2),
                    color2.substring(2, color2.length()-2), color3.substring(2, color3.length()-2),
                    color4.substring(2, color4.length()-2), color5.substring(2, color5.length()-2));
            user.setColorScheme(colorScheme);
            user.update();
            new CustomAlert("Scheme added successfully! Please log out and in again.", "information");
        });
    }

    private void account(){
        // --- Creating the buttons and text fields ---
        JFXButton changeName = new JFXButton("Change name"); changeName.setStyle("-fx-pref-width: 200;");
        JFXButton changeSurname = new JFXButton("Change surname");changeSurname.setStyle("-fx-pref-width: 200;");
        JFXButton changeUsername = new JFXButton("Change username");changeUsername.setStyle("-fx-pref-width: 200;");
        JFXButton changePassword = new JFXButton("Change password");changePassword.setStyle("-fx-pref-width: 200;");

        changeName.getStyleClass().add("button-flat");
        changeSurname.getStyleClass().add("button-flat");
        changeUsername.getStyleClass().add("button-flat");
        changePassword.getStyleClass().add("button-flat");

        JFXTextField nameTf = new JFXTextField(); nameTf.setVisible(false); nameTf.setPromptText("new name");
        JFXTextField surnameTf = new JFXTextField(); surnameTf.setVisible(false); surnameTf.setPromptText("new surname");
        JFXTextField usernameTf = new JFXTextField(); usernameTf.setVisible(false); usernameTf.setPromptText("new username");
        JFXTextField passwordTf = new JFXTextField(); passwordTf.setVisible(false); passwordTf.setPromptText("new password");

        JFXButton nameDone = new JFXButton("Done"); nameDone.setVisible(false);
        JFXButton surnameDone = new JFXButton("Done"); surnameDone.setVisible(false);
        JFXButton usernameDone = new JFXButton("Done"); usernameDone.setVisible(false);
        JFXButton passwordDone = new JFXButton("Done"); passwordDone.setVisible(false);

        nameDone.getStyleClass().add("acc-button");
        surnameDone.getStyleClass().add("acc-button");
        usernameDone.getStyleClass().add("acc-button");
        passwordDone.getStyleClass().add("acc-button");

        // --- Setting functions to buttons ---
        changeName.setOnAction(e -> {
            nameTf.setVisible(true);
            nameDone.setVisible(true);
        });
        changeSurname.setOnAction(e -> {
            surnameTf.setVisible(true);
            surnameDone.setVisible(true);
        });
        changeUsername.setOnAction(e -> {
            usernameTf.setVisible(true);
            usernameDone.setVisible(true);
        });
        changePassword.setOnAction(e -> {
            passwordTf.setVisible(true);
            passwordDone.setVisible(true);
        });

        nameDone.setOnAction(e -> {
            try{
                String name = nameTf.getText();
                if(!name.isEmpty()){
                    user.setName(name);
                    user.update();
                    nameTf.setVisible(false);
                    nameDone.setVisible(false);
                    new CustomAlert("Changed successfully!", "information");
                }else{
                    nameTf.setVisible(false);
                    nameDone.setVisible(false);
                }
            }catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }
        });
        surnameDone.setOnAction(e -> {
            try{
                String surname = surnameTf.getText();
                if(!surname.isEmpty()){
                    user.setSurname(surname);
                    user.update();
                    surnameTf.setVisible(false);
                    surnameDone.setVisible(false);
                    new CustomAlert("Changed successfully!", "information");
                }else {
                    surnameTf.setVisible(false);
                    surnameDone.setVisible(false);
                }
            }catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }
        });
        usernameDone.setOnAction(e -> {
            try{
                String username = usernameTf.getText();
                if(!username.isEmpty()){
                    user.setUsername(username);
                    user.update();
                    usernameTf.setVisible(false);
                    usernameDone.setVisible(false);
                    new CustomAlert("Changed successfully!", "information");
                }else {
                    usernameTf.setVisible(false);
                    usernameDone.setVisible(false);
                }
            }catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }
        });
        passwordDone.setOnAction(e -> {
            try{
                String password = passwordTf.getText();
                if(!password.isEmpty()){
                    user.setPassword(password);
                    user.update();
                    passwordTf.setVisible(false);
                    passwordDone.setVisible(false);
                    new CustomAlert("Changed successfully!", "information");
                }else {
                    passwordTf.setVisible(false);
                    passwordDone.setVisible(false);
                }
            }catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }
        });

        innerPane.getChildren().clear();
        innerPane.addColumn(0, changeName, changeSurname, changeUsername, changePassword);
        innerPane.addColumn(1, nameTf, surnameTf, usernameTf, passwordTf);
        innerPane.addColumn(2, nameDone, surnameDone, usernameDone, passwordDone);

    }

    private void info(){
        Label text = new Label();
        text.setText("This is a program created by Ergys Rrjolli.");

        innerPane.getChildren().clear();
        innerPane.add(text, 1, 0);
    }
}
