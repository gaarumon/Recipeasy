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


    /**
     * initializes the list view for user's own recipes. when a recipe in the list is selected,
     * calls recipeSelected() method. currently hard coded to show that user has no recipes.
     * @param url
     * @param resourceBundle
     * @author Kotryna
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myRecipesListView.setPlaceholder(new Label("You haven't added any recipes yet."));
        myRecipesListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> recipeSelected()
        );
    }

    /**
     * sets scene factory and database
     * @param sceneFactory
     * @author Kotryna
     */

    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    /**
     * method called when a recipe is selected from the recipe list. currently prints recipe's index
     * in the database
     * @author Kotryna
     */
    public void recipeSelected() {
        Recipe selectedRecipe = myRecipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);
        }
    }


    /**
     * method called when New Recipe button is clicked
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void pressedNewRecipeButton(ActionEvent event) throws IOException {
        sceneFactory.createNewRecipeScene(event);

    }
}
