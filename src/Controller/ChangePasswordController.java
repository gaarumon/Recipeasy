package Controller;

import Model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import GUI.Alerts;

public class ChangePasswordController {

    @FXML private TextField oldPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmPasswordField;

    private SceneFactory sceneFactory;

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }

    @FXML
    public void handleChangePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            Alerts.showAlert("Lösenorden matchar inte!");
            return;
        }

        try {
            Database db = new Database();
            boolean success = db.changePassword(sceneFactory.getCurrentUser(), oldPassword, newPassword);
            if (success) {
                Alerts.showAlert("Lösenordet ändrat!");
            } else {
                Alerts.showAlert("Gamla lösenordet är fel!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}