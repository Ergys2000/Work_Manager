package models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert {
    public CustomAlert(String str, String type){
        if(type.toLowerCase().equals("information")){
            Alert al = new Alert(Alert.AlertType.INFORMATION, str, ButtonType.OK);
            al.show();
        }else{
            Alert al = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
            al.show();
        }
    }
}
