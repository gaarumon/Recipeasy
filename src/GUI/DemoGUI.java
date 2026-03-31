package GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        //stage.getIcons().add(new Image("/Users/elvirajensen/Desktop/Recipeasy/src/Images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }
}