package Controller;

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
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Woops");
            alert.setHeaderText(null);
            alert.setContentText("Looks like you entered the wrong username or password, please try again!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
            alert.showAndWait();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SceneForSignUp.fxml"));
        Parent root = loader.load();
        LogInSignUpController controller = loader.getController();
        controller.setSceneFactory(sceneFactory);
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Woops");
            alert.setHeaderText(null);
            alert.setContentText("Looks like this username is already in use, choose a new one!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
            alert.showAndWait();
        } else if (password.length() < 6){
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Woops");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 6 characters, please try again!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
            alert.showAndWait();
        } else {
            database.addNewUserToDatabase(username, password);

            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("Sign up successful!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/GUI/styleSheet.css").toExternalForm());
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SceneForLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        }
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

}
