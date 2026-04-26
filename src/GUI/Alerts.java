package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    public void basicError(String alertMessage) {

        Alert alert =new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Woops");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
        alert.showAndWait();
    }

    public void basicConfirmation(String alertMessage) {

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Woopsohoo");
        alert.setContentText(alertMessage);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
        alert.showAndWait();
    }

    public boolean confirmDialog(String alertMessage){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("You are about to sign out");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
