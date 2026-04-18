package Controller;

import Model.Database;
import Model.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyRecipeController implements Initializable {
    private SceneFactory sceneFactory;
    private Database database;

    @FXML
    private ListView<Recipe> myRecipesListView;
    @FXML
    private Button newRecipeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myRecipesListView.setPlaceholder(new Label("You haven't added any recipes yet."));
        myRecipesListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> recipeSelected()
        );
    }

    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    public void recipeSelected() {
        Recipe selectedRecipe = myRecipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);
        }
    }

    public void pressedNewRecipeButton(ActionEvent event) throws IOException {
        sceneFactory.createNewRecipeScene(event);

    }
}
