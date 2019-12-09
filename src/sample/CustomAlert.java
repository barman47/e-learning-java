package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CustomAlert {
    public static void showAlert(AlertType alertType, String alertTitle, String alertHeader, String alertText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(alertHeader);
        alert.setTitle(alertTitle);
        alert.setContentText(alertText);
        alert.show();
    }
}