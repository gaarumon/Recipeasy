package Controller;

import GUI.GUILaunch;
import Model.Database;
import Model.Recipe;
import Model.ShoppingList;
import GUI.Alerts;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Controller for the Main Scene (main window after logging in). All actions relating to Main
 * Scene will be called here.
 * @author Kotryna
 */

public class MainSceneController implements Initializable {
    private Database database;
    private SceneFactory sceneFactory;
    private ShoppingList shoppingList;
    private Recipe currentRecipe; //i dont know if this is needed/if guys have some different varaible for it but this is for selecting favorite recipes - trung

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
                (observableValue, oldValue, newValue) -> searchListViewRecipe()
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
    public void recipeSelected(Recipe selectedRecipe) {

        if (selectedRecipe != null) {
            this.currentRecipe = selectedRecipe;
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

    public void searchListViewRecipe() {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        recipeSelected(selectedRecipe);
    }

    private void addMissingIngredientsToShoppingList(Recipe recipe){
        ArrayList<String> added = shoppingList.addMissingIngredientsFromRecipe(recipe, Collections.emptyList());
        if(!added.isEmpty()){
            System.out.println("Added to shopping list: " + added);
        }
    }

    public void pressedMyRecipeButton(ActionEvent event) throws IOException {
        sceneFactory.createMyRecipeScene(event);

    }

    public void handleFavouritesButton(ActionEvent event) throws Exception{
        sceneFactory.createFavouritesScene();
    }

    public void addIngredientsToShoppingList(ActionEvent event) {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        if(selectedRecipe != null) {
            ArrayList<String> ingredients = selectedRecipe.getIngredients();
            System.out.println("Add recipe for: " + selectedRecipe.getRecipeName());
            //Todo: Lägg till shoppinglist later.
        }
    }

    public void pressedShoppingListButton(ActionEvent event) throws IOException{
        sceneFactory.createShoppingListScene(event);
    }

    public void handleAddToFavourtiesButton(ActionEvent event) throws Exception{
        Alerts alerts = new Alerts();

        if (currentRecipe == null){
            alerts.basicError("Pick a recipe first before adding it to favourties.");
            return;
        }
        String username = sceneFactory.getCurrentUser();
        String result = database.addFavouriteRecipe(username, currentRecipe.getIndex());

        if(result.equals("ADDED")){
            alerts.basicConfirmation(currentRecipe.getRecipeName() + " added to favourites!");
        } else if (result.equals("ALREADY EXISTS")){
            alerts.basicError(currentRecipe.getRecipeName() + " is already in your favourites.");
        } else{
            alerts.basicError("Could not add to favourites, try again:");
        }
    }

    public void handleSignOut(ActionEvent event) throws Exception{
        Alerts alert = new Alerts();

        if (alert.confirmDialog("Are you sure you want to sign out?")){
            sceneFactory.setCurrentUser(null);
            sceneFactory.createLoginScene(event);
        }
    }




}
