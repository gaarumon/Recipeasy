package GUI;

import javafx.scene.control.Alert;

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
}
