package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToMainMenu(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("SceneForMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void openAllergiesWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AllergiesWindow.fxml"));
            Stage stage = new Stage();

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Allergier");

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openIngredientsWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("IngredientsWindow.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Ingredients");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}