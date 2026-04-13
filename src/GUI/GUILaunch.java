package GUI;
import Controller.LogInSignUpController;
import Controller.SceneFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GUILaunch extends Application {

    private SceneFactory sceneFactory = new SceneFactory();

    /**
     * method responsible for launching the program and initializing the log in window.
     * @param stage
     * @throws IOException
     * @author Kotryna
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUILaunch.class.getResource("SceneForLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        LogInSignUpController loginLogInSignUpController = fxmlLoader.getController();
        loginLogInSignUpController.setSceneFactory(sceneFactory.getSceneFactory());

        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


}