package Controller;


import Model.Database;
import Model.Recipe;
import Model.ShoppingList;
import GUI.Alerts;
import Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.concurrent.Task;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private Recipe currentRecipe;
    private Recipe previousRecipe;
    private boolean filterByAllergies = false;
    private boolean filterByOwnedIngredients = false;
    private User user;

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
    private Button searchButton;

    @FXML
    private Pane recipePicture;

    private Alerts alerts = new Alerts();

    /**
     * method called when search button is clicked, sends the value typed into searchbar
     * to the database class, if it finds matching recipes, it updates the search list
     * @param event
     * @author Kotryna
     */
    @FXML
    public void search(ActionEvent event) throws Exception {
        searchListView.getItems().clear();
        //ArrayList <Recipe> recipes = database.searchRecipesByName(searchBarField.getText(), sceneFactory.getCurrentUser());
        ArrayList <Recipe> recipes;

        if (filterByAllergies || filterByOwnedIngredients){
            recipes = database.searchRecipesWithFilters(searchBarField.getText(), sceneFactory.getCurrentUser(), filterByAllergies, filterByOwnedIngredients);
        }else{
            recipes = database.searchRecipesByName(searchBarField.getText());
        }

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

        searchButton.setDefaultButton(true); //So you can press enter for searching instead of clicking with the mouse.

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
        this.user = sceneFactory.getUser();
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
     * method called when recipe from the list is selected
     * @author Kotryna
     */
    public void recipeSelected(Recipe selectedRecipe) {

        if (selectedRecipe != null) {
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Recipe fullRecipe = database.getRecipeDetails(index); //do we need this? or can we say fullRecipe = selectedRecipe?
                        //final Recipe fullRecipe = selectedRecipe;
                        javafx.application.Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                previousRecipe = currentRecipe;
                                currentRecipe = fullRecipe;
                                recipeNameLabel.setText(selectedRecipe.getRecipeName());

                                ingredientsListView.getItems().clear();
                                if (fullRecipe.getIngredients() != null) {
                                    ingredientsListView.getItems().addAll(fullRecipe.getIngredients());
                                }
                                if (fullRecipe.getInstructions() != null) {
                                    instructionsTextArea.setText(fullRecipe.getInstructions());
                                } else {
                                    instructionsTextArea.setText("No instructions to show ");
                                }

                                //by kotryna
                                String imageUrl = fullRecipe.getRecipeImage().getUrl();
                                setCSS(imageUrl);


                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    /**
     * gets the selected recipe from the search list
     * @author Kotryna
     */
    public void searchListViewRecipe() {
        Recipe selectedRecipe = searchListView.getSelectionModel().getSelectedItem();
        recipeSelected(selectedRecipe);
    }

    // should create task that does the logic of updating shoppinglist database in the
    // backend without disrupting speed because it dosent wait for logic to be finished
    // to update ui.    
    @FXML
    public void handleAddIngredientsToShoppingListButton(MouseEvent event){
        if(currentRecipe == null){
            alerts.basicError("Pick a recipe first before adding ingredients to your shopping list.");
            return;
        }

        try{
            String username = sceneFactory.getCurrentUser();
            ArrayList<String> ownedIngredients = database.getUserIngredients(username);
            ArrayList<String> addedIngredients = shoppingList.addMissingIngredientsFromRecipe(currentRecipe, ownedIngredients);

            database.replaceShoppingList(username, shoppingList.getIngredients());

            if(addedIngredients.isEmpty()){
                alerts.basicConfirmation("You already have all ingredients for this recipe.");
            } else {
                alerts.basicConfirmation("Added " + addedIngredients.size() + " missing ingredients to your shopping list.");
            }
        } catch (Exception e){
            e.printStackTrace();
            alerts.basicError("Could not add ingredients to shopping list. Please try again.");
        }

    }

    public void pressedMyRecipeButton(MouseEvent event) throws Exception {
        sceneFactory.createMyRecipeScene(event);

    }

    public void pressedFavouritesButton(MouseEvent event) throws Exception{
        sceneFactory.createFavouritesScene();
    }

    public void pressedShoppingListButton(MouseEvent event) throws IOException{
        sceneFactory.createShoppingListScene(event);
    }

    public void handleAddToFavouritesButton(MouseEvent event) throws Exception{
        if (currentRecipe == null){
            alerts.basicError("Pick a recipe first before adding it to favourties.");
            return;
        }
        String username = sceneFactory.getCurrentUser();
        String result = database.addFavouriteRecipe(username, currentRecipe.getIndex());

        if(result.equals("ADDED")){
            user.addFavourite(currentRecipe);
            alerts.basicConfirmation(currentRecipe.getRecipeName() + " added to favourites!");
        } else if (result.equals("ALREADY EXISTS")){
            alerts.basicError(currentRecipe.getRecipeName() + " is already in your favourites.");
        } else{
            alerts.basicError("Could not add to favourites, try again:");
        }
    }

    public void handleSignOut(MouseEvent event) throws Exception{
        if (alerts.confirmDialog("Are you sure you want to sign out?")){
            sceneFactory.setCurrentUser(null);
            sceneFactory.createLoginScene(event);
        }
    }

    public void handleRandomRecipe(MouseEvent event) throws Exception{
       Recipe recipe = user.getCurrentRandomRecipe();
       user.setCurrentRandomRecipe(user.getNextRandomRecipe());

       if (recipe == null){
           alerts.basicError("No recipes found :(");
       }else{
           recipeSelected(recipe);
           new Thread(() -> {
               try {
                   user.setNextRandomRecipe(database.getRandomRecipe(user.getUsername()));
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }
           }).start();
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
    /*
    this method maybe can be moved to a different class later on
    kotryna
     */
    private void setCSS(String imageUrl) {

        recipePicture.setStyle(
                "-fx-background-image: url('" + imageUrl + "');" +
                        "-fx-background-size: 100% auto;" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-radius: 10px;"
        );
    }

    public void openAllergies(MouseEvent event) {
        try {
            sceneFactory.createAllergyWindow(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlePreviousRecipe(ActionEvent event){
        if (previousRecipe != null){
            Recipe temp = currentRecipe;
            recipeSelected(previousRecipe);
            previousRecipe = temp;
        }
    }

    public void handleFilterButton(ActionEvent event) throws IOException{
        sceneFactory.createFilterScene(event);
    }

    public void setFilters(boolean allergyFilter, boolean ingredientFilter){
        this.filterByAllergies = allergyFilter;
        this.filterByOwnedIngredients = ingredientFilter;
    }

    public boolean getFilterByAllergies(){
        return this.filterByAllergies;
    }

    public boolean getFilterByOwnedIngredients(){
        return this.filterByOwnedIngredients;
    }

    public void showIngredientFilteredRecipes(ArrayList<Recipe> recipes){
        searchListView.getItems().clear();

        if (recipes != null){
            for (Recipe r: recipes){
                searchListView.getItems().add(r);
            }
        }else{
            searchListView.setPlaceholder(new Label("No matching recipes found."));
        }
    }
}
