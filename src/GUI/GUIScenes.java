package GUI;

import Controller.MainSceneController;
import Controller.SceneFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Class responsible for creation/initialization of all scenes/windows except for login/signup.
 * This is where we have methods for creating Main window, favourite, allergies, shopping list and
 * other scenes.
 * After initializing the scene, its controller is created automatically, so you can save the
 * exact instance of the controller like in the createMainWindow(); example bellow.
 * @author Kotryna
 */

public class GUIScenes {

    private Stage stageMain;
    private Scene sceneMain;
    private Parent rootMain;
    private SceneFactory sceneFactory;

    /**
     * method responsible for initializing the main window of the program
     * @param event
     * @throws IOException
     * @author Kotryna
     */

    public void createMainWindow (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForMain.fxml"));
        rootMain = loader.load();
        stageMain = (Stage)((Node)event.getSource()).getScene().getWindow();

        MainSceneController controllerMain = loader.getController(); //saves exact controller instance
        controllerMain.setSceneFactory(sceneFactory.getSceneFactory()); //passes on scene factory for database access

        sceneMain = new Scene(rootMain);
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        sceneMain.getStylesheets().add(css);
        stageMain.setScene(sceneMain);
        stageMain.setResizable(true);
        stageMain.setMaximized(true);
        stageMain.show();
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }
}
