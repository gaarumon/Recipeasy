package Controller;

import GUI.GUILaunch;
import Model.Database;
import Model.Recipe;
import Model.ShoppingList;
import GUI.Alerts;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;

//import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.*;

import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

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
    private Label infoBoxLabel1;

    @FXML
    private Label infoBoxLabel2;
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
        ArrayList <Recipe> recipes = database.searchRecipesByName(searchBarField.getText(), sceneFactory.getCurrentUser());

        if (recipes != null) {
            for (Recipe r : recipes) {
                searchListView.getItems().add(r);
            }
        } else {
            searchListView.setPlaceholder(new Label("No matching recipes found"));
        }
    }
    @FXML
    private void handleIngredientsButton(MouseEvent event) throws IOException {
        sceneFactory.createIngredientsScene(event);
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
        this.shoppingList = sceneFactory.getShoppingList();

        try{
            ArrayList<String> saved = database.getShoppingList(sceneFactory.getCurrentUser());
            shoppingList.clear();
            for(String ingredient : saved){
                shoppingList.addIngredient(ingredient);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        startTipRotation();
        infoBoxLabel1.setText(sceneFactory.getCurrentUser());
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
            if(placeHolderBox != null){
                placeHolderBox.setVisible(false); // trung: put this in a null check because the errors it was spamming was mad annoying
                                                  // surpised nobody checked this
            }

            addMissingIngredientsToShoppingList(selectedRecipe); // not written by kotryna so if it bugs out its on trung
        }
    }

    public void searchListViewRecipe() {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        recipeSelected(selectedRecipe);
    }

    // should create task that does the logic of updating shoppinglist database in the
    // backend without disrupting speed because it dosent wait for logic to be finished
    // to update ui.    
    private void addMissingIngredientsToShoppingList(Recipe recipe){
        String username = sceneFactory.getCurrentUser();

        Task<Void> task = new Task<>(){
            @Override
            protected Void call() throws Exception{
                ArrayList<String> owned = database.getOwnedIngredients(username);
                shoppingList.addMissingIngredientsFromRecipe(recipe, owned);
                database.replaceShoppingList(username, shoppingList.getIngredients());
                return null;
            }
        };

        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }

    @FXML
    public void pressedMyRecipeButton(MouseEvent event) throws IOException {
        sceneFactory.createMyRecipeScene(event);

    }

    public void handleFavouritesButton(MouseEvent event) throws Exception{
        sceneFactory.createFavouritesScene();
    }


    public void pressedShoppingListButton(MouseEvent event) throws IOException{
        sceneFactory.createShoppingListScene(event);
    }

    public void handleAddToFavourtiesButton(MouseEvent event) throws Exception{
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

    public void handleSignOut(MouseEvent event) throws Exception{
        Alerts alert = new Alerts();

        if (alert.confirmDialog("Are you sure you want to sign out?")){
            sceneFactory.setCurrentUser(null);
            sceneFactory.createLoginScene(event);
        }
    }

    public void handleRandomRecipe(MouseEvent event) throws Exception{
       Recipe recipe = database.getRandomRecipe(sceneFactory.getCurrentUser());

       if (recipe == null){
           Alerts alert = new Alerts();
           alert.basicError("No recipes found :(");
       }else{
           recipeSelected(recipe);
       }
    }

    private void startTipRotation() {
        List<String> tips = List.of(
                "Tip: Always taste as you cook!",
                "Did you know? Pasta water makes great sauce!",
                "Pro tip: Let meat rest before cutting!",
                "Fun fact: Honey never expires!",
                "Tip: Cold butter makes flakier pastry!",
                "Did you know? Garlic gets milder when roasted!",
                "Pro tip: Salt your pasta water generously!",
                "Fun fact: Carrots were originally purple!",
                "Tip: A pinch of sugar fixes too much salt!",
                "Did you know? Avocados ripen faster in a bag!",
                "Pro tip: Freeze ginger for easy grating!",
                "Fun fact: Ketchup was once sold as medicine!",
                "Tip: Warm your plates before serving!",
                "Did you know? Broccoli is basically a flower!",
                "Pro tip: Add vinegar to poached egg water!",
                "Fun fact: Strawberries are not berries!",
                "Tip: Toast spices before using them!",
                "Did you know? Bananas are berries though!",
                "Pro tip: Use mayo instead of butter for grilling!",
                "Fun fact: Peanuts are not actually nuts!",
                "Tip: Rub steak with coffee for extra depth!",
                "Did you know? Chocolate was once used as currency!",
                "Pro tip: Deglaze your pan for instant sauce!",
                "Fun fact: Almonds are related to peaches!",
                "Tip: Let dough rest for better texture!",
                "Did you know? Apples float because they are 25% air!",
                "Pro tip: Butter the pan before flouring it!",
                "Tip: Squeeze lemon on cut fruit to prevent browning!",
                "Did you know? Cooking with wine makes you happy!"
        );
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(15), e -> {
                    int random = (int)(Math.random() * tips.size());
                    infoBoxLabel2.setText(tips.get(random));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



}
