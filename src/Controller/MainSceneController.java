package Controller;

import Model.Database;
import Model.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Main Scene (main window after logging in). All actions relating to Main
 * Scene will be called here.
 */

public class MainSceneController implements Initializable {
    private Database database;
    private SceneFactory sceneFactory;

    @FXML
    private TextField searchBarField;

    @FXML
    private ListView<Recipe> searchListView;

    /**
     * method called when search button is clicked, sends the value typed into searchbar
     * to the database class, if it finds matching recipes, it updates the search list
     * @param event
     */
    @FXML
    public void search(ActionEvent event) throws Exception {
        searchListView.getItems().clear();
        ArrayList <Recipe> recipes = database.searchRecipesByName(searchBarField.getText());

        if (recipes != null) {
            for (Recipe r : recipes) {
                searchListView.getItems().add(r);
            }
        }
    }

    /**
     * initializes the search listview, listens to when an item is selected from the
     * listview and calls recipeSelected() method when something is picked
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> recipeSelected()
        );
    }

    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    /**
     * method called when recipe from the list is selected, right now prints the recipe index
     */
    public void recipeSelected() {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);
        }
    }
}
