package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Model.Database;
import GUI.SceneController;
import javafx.event.ActionEvent;

public class Controller {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    Database database = new Database();
    SceneController sceneController = new SceneController();

    @FXML
    public void handleLogIn(ActionEvent event) throws Exception{
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (database.logIn(username, password)){
            sceneController.switchToMainMenu(event);
        }else{
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Woops");
            alert.setHeaderText(null);
            alert.setContentText("Looks like you entered the wrong username or password, please try again!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
            alert.showAndWait();
        }
    }
}
