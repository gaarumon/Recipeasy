package Controller;

import GUI.Alerts;
import GUI.GUIScenes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Model.Database;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class LogInSignUpController {
    private SceneFactory sceneFactory;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField signUpUsername;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private Button signUpButton;
    Database database;
    Alerts alert = new Alerts();

    /**
     * Hanterar inloggningsprocessen när användaren klickar på log in-knappen.
     * Verifierr användfarnamnet och lösenordet mot databasen.
     * Byter till mainmeny vid lyckad inloggning. vid misslyckad inloggning visas ett felmeddelande.
     * @param event
     * @throws Exception
     * @author Elvira Jensen
     */
    @FXML
    public void handleLogIn(ActionEvent event) throws Exception{
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (database.logIn(username, password)){
            sceneFactory.setCurrentUser(username);
            sceneFactory.createMainScene(event);
        }else{
            alert.basicError("Looks like you entered the wrong username or password, please try again!");
        }
    }

    /**
     * Byter från SceneForLogin-vyn till SceneForSignUp-vyn när användaren trycker på sign up-knappen.
     * @param event
     * @throws Exception
     * @author Elvira Jensen
     */
    @FXML
    public void pressedSignUpButton(ActionEvent event) throws Exception {
       sceneFactory.createSignUpScene();
    }

    /**
     * Hanterar registreringsprocessen när användaren klickar på sign up-knappen i SceneForSignUp-vyn.
     * Kontrollerar att användarnamnet inte redan existerar i databasen.
     * Kontrollerar att lösenordet är minst 6 tecken långt.
     * Vid lyckad registrering läggs användarens valda username och password till i databasen
     * och användaren omdirigeras till inloggningsvyn.
     * @param event
     * @throws Exception
     * @author Elvira Jensen
     */
    @FXML
    public void handleSignUp(ActionEvent event) throws Exception{
        String username = signUpUsername.getText();
        String password = signUpPassword.getText();

        if (database.doesUsernameAlreadyExist(username)){
            alert.basicError("Looks like this username is already in use, choose a new one!");
        } else if (password.length() < 6){
            alert.basicError("Password must be at least 6 characters, please try again!");
        } else {
            database.addNewUserToDatabase(username, password);
            alert.basicConfirmation("Sign up successful!");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        }
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

}
