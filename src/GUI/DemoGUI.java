package GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DemoGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(DemoGUI.class.getResource("SceneForMain.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(DemoGUI.class.getResource("SceneForLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Recipeasy!");
        stage.setScene(scene);
        stage.show();
    }
}