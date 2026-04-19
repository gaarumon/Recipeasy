package Controller;

import Model.Database;
import Model.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Main Scene (main window after logging in). All actions relating to Main
 * Scene will be called here.
 * @author Kotryna
 */

public class MainSceneController implements Initializable {
    private Database database;
    private SceneFactory sceneFactory;

    @FXML
    private TextField searchBarField;

    @FXML
    private ListView<Recipe> searchListView;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private ListView<String> ingredientsListView;

    @FXML
    private TextArea instructionsTextArea;

    @FXML
    private Button addIngredientsButton;

    @FXML
    private VBox placeHolderBox;

    /**
     * method called when search button is clicked, sends the value typed into searchbar
     * to the database class, if it finds matching recipes, it updates the search list
     * @param event
     * @author Kotryna
     */
    @FXML
    public void search(ActionEvent event) throws Exception {
        searchListView.getItems().clear();
        ArrayList <Recipe> recipes = database.searchRecipesByName(searchBarField.getText());

        if (recipes != null) {
            for (Recipe r : recipes) {
                searchListView.getItems().add(r);
            }
        } else {
            searchListView.setPlaceholder(new Label("No matching recipes found"));
        }
    }

    /**
     * initializes the search listview, listens to when an item is selected from the
     * listview and calls recipeSelected() method when something is picked
     * @param url
     * @param resourceBundle
     * @author Kotryna
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> recipeSelected()
        );
    }

    /**
     * sets factory and database
     * @param sceneFactory
     * @author Kotryna
     */
    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    /**
     * method called when recipe from the list is selected, right now prints the recipe index
     * @author Kotryna
     */
    public void recipeSelected() {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);

            recipeNameLabel.setText(selectedRecipe.getRecipeName());

            ingredientsListView.getItems().clear();
            if(selectedRecipe.getIngredients() != null) {
                ingredientsListView.getItems().addAll(selectedRecipe.getIngredients());
            }
            if(selectedRecipe.getInstructions() != null) {
                instructionsTextArea.setText(selectedRecipe.getInstructions());
            } else {
                instructionsTextArea.setText("No instructions to show ");
            }

            placeHolderBox.setVisible(false);
        }
    }

    public void pressedMyRecipeButton(ActionEvent event) throws IOException {
        sceneFactory.createMyRecipeScene(event);

    }

    public void addIngredientsToShoppingList(ActionEvent event) {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        if(selectedRecipe != null) {
            ArrayList<String> ingredients = selectedRecipe.getIngredients();
            System.out.println("Add recipe for: " + selectedRecipe.getRecipeName());
            //Todo: Lägg till shoppinglist later.
        }
    }
}
