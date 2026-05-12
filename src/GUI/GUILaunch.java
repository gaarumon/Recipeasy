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
        /*try {
            //Lägger till recept när appen startar
            sceneFactory.getDatabase().addTestRecipe();
            System.out.println("addTestRecipe() invoked");
        } catch (Exception e) {
            System.out.println("Fel vid addTestRecipe: " + e.getMessage());
        }*/

        javafx.scene.text.Font.loadFont(
                getClass().getResourceAsStream("/GUI/Font/TenorSans-Regular.ttf"), 12
        );

        FXMLLoader fxmlLoader = new FXMLLoader(GUILaunch.class.getResource("SceneForLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        LogInSignUpController loginLogInSignUpController = fxmlLoader.getController();
        loginLogInSignUpController.setSceneFactory(sceneFactory.getSceneFactory());

        scene.getStylesheets().add(css);

        //Anpassar fönstret efter skärmens storlek
        double scale = scaleSceneToScreen(scene, 282, 348);
        stage.setWidth(282 * scale);
        stage.setHeight(348 * scale + 28);
        stage.centerOnScreen();

        stage.setScene(scene);
        stage.show();
    }

    private double scaleSceneToScreen(Scene scene, double designWidth, double designHeight) {
        javafx.geometry.Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        double scale = Math.min(1.0, Math.min(screen.getWidth() / designWidth, screen.getHeight() / designHeight) * 0.92);
        scene.getRoot().getTransforms().add(new javafx.scene.transform.Scale(scale, scale, 0, 0));
        return scale;
    }


}