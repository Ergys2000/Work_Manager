package GUI;

import DataHandling.RWUser;
import GUI.Actions.Settings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.CustomAlert;
import models.User;

import java.io.*;

public class LogIn extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(900);
        // Creating the layout pane and getting the users
        GridPane grid = new GridPane();
        grid.setMinSize(900, 400);
        grid.prefHeightProperty().bind(primaryStage.heightProperty());
        grid.prefWidthProperty().bind(primaryStage.widthProperty());
        grid.setAlignment(Pos.CENTER);

        // Creating the nodes needed in the window
        JFXTextField usernameTf = new JFXTextField();
        usernameTf.setPromptText("Username");
        usernameTf.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.3));

        JFXPasswordField passwordTf = new JFXPasswordField();
        passwordTf.setPromptText("Password");
        passwordTf.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.3));

        JFXButton cancel = new JFXButton("Cancel");
        cancel.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        cancel.getStyleClass().setAll("button-flat");

        JFXButton logIn = new JFXButton("Log in");
        logIn.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        logIn.getStyleClass().setAll("button-flat");

        Label signUp = new Label("Sign up!");
        signUp.setStyle("-fx-border-width: 0, 0, 2, 0; -fx-border-color: black");

        // Inserting the nodes in the pane
        HBox hb = new HBox(15);
        hb.getChildren().addAll(logIn, cancel);


        VBox fields = new VBox(25);
        fields.getChildren().addAll(usernameTf, passwordTf, hb, signUp);

        grid.add(fields, 0, 0);
        Scene scene = new Scene(grid);

        // setting the functions of buttons
        cancel.setOnAction(e->{
            primaryStage.close();
        });

        passwordTf.setOnAction(e -> {

            String username = usernameTf.getText();
            String password = passwordTf.getText();
            try{
                RWUser user = new RWUser(username, password);
                new UserView(primaryStage, user);
            } catch (IOException ex) {
                new CustomAlert("Wrong Credentials","error");
            }
        });

        logIn.setOnAction(e -> {
            String username = usernameTf.getText();
            String password = passwordTf.getText();
            try{
                RWUser user = new RWUser(username, password);
                new UserView(primaryStage, user);
            } catch (IOException ex) {
                new CustomAlert("Wrong Credentials","error");
            } catch (Exception ex){
                new CustomAlert(ex.getMessage(), "error");
            }
        });
        signUp.setOnMouseClicked(e -> {
            signUp();
        });

        // Creating the scene and putting it in the stage
        scene.getStylesheets().add(Settings.getStyle());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log in");
        primaryStage.show();
    }

    public static void main(String[] args){Application.launch(args);}

    public static GridPane createUser(Stage st) {
            GridPane grid = new GridPane();
            grid.prefWidthProperty().bind(st.widthProperty());
            grid.prefHeightProperty().bind(st.heightProperty());
            grid.setVgap(15);
            grid.setHgap(20);
            grid.setAlignment(Pos.CENTER);
            VBox fieldBox = new VBox(15);
            VBox labelBox = new VBox(28);

            JFXTextField nameTf = new JFXTextField(); Label nL = new Label("Name: ");
            JFXTextField surnameTf = new JFXTextField(); Label sL = new Label("Surname: ");
            JFXTextField usernameTf = new JFXTextField(); Label uL = new Label("Username: ");
            JFXTextField passwordTf = new JFXTextField(); Label pL = new Label("Password: ");

            JFXButton createuser = new JFXButton("Create account");
            createuser.getStyleClass().setAll("button-flat");

            fieldBox.getChildren().addAll(nameTf, surnameTf, usernameTf, passwordTf);
            labelBox.getChildren().addAll(nL, sL, uL, pL);

            grid.add(labelBox, 0, 0);
            grid.add(fieldBox, 1, 0);
            grid.add(createuser, 1, 1);

            createuser.setOnAction(e -> {
                String name = nameTf.getText();
                String surname = surnameTf.getText();
                String username = usernameTf.getText();
                String password = passwordTf.getText();
                if(name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()){
                    new CustomAlert("Please fill in all the fields!", "information");
                }else{
                    User user = new User(name, surname, username, password);

                    if(!new File("src/Data/").exists()) new File("src/Data/").mkdir();
                    File file = new File("src/Data/" + username + ".bin");

                    if(!file.exists()){
                        try {
                            file.createNewFile();
                            FileOutputStream fos=new FileOutputStream(file);
                            ObjectOutputStream oos=new ObjectOutputStream(fos);
                            oos.writeObject(user);
                            oos.close();fos.close();
                            new CustomAlert("Account created!", "information");
                            st.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }else new CustomAlert("Username exists!", "information");
                }
            });
        return grid;
    }
    public void signUp(){
        Stage st = new Stage();
        GridPane grid = createUser(st);
        Scene sc = new Scene(grid, 600, 400);
        sc.getStylesheets().add(Settings.cssFile);
        st.setScene(sc);
        st.show();
    }

}
