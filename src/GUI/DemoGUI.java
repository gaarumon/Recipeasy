package GUI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DemoGUI extends Application {
    private Stage stageMain;
    private Scene sceneMain;
    private Parent rootMain;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DemoGUI.class.getResource("SceneForLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void createMainWindow (ActionEvent event) throws IOException {
        rootMain = FXMLLoader.load(getClass().getResource("SceneForMain.fxml"));
        stageMain = (Stage)((Node)event.getSource()).getScene().getWindow();
        sceneMain = new Scene(rootMain);
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        sceneMain.getStylesheets().add(css);
        stageMain.setScene(sceneMain);
        stageMain.setResizable(true);
        stageMain.setMaximized(true);
        stageMain.show();
    }
}