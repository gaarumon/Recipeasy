package Controller;

import Model.Database;
import Model.Recipe;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import GUI.Alerts;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * NewRecipeController takes care of everything that happens when user adds a new recipe
 */

public class NewRecipeController {
    private SceneFactory sceneFactory;
    private Database database;
    private Recipe newRecipe = new Recipe();
    private Alerts alert = new Alerts();
    private User user;

    @FXML
    private TextField nameNewRecipeField;
    @FXML
    private TextField ingredientNewRecipeField;
    @FXML
    private TextField measurementNewRecipeField;
    @FXML
    private Button addIngredientNewRecipe;
    @FXML
    private ListView<String> ingredientsNewRecipeListView;
    @FXML
    private TextArea newRecipeInstructionsText;
    @FXML
    private Button cancelNewRecipeButton;
    @FXML
    private Button saveRecipeButton;


    /**
     * creates a placeholder text for added ingredients
     * @param
     * @author Trung Nguyen
     */
    @FXML
    public void initialize(){
        Label placeholderText = new Label("Ingredients will appear here...");
        placeholderText.getStyleClass().add("list-placeholder-text");

        StackPane placeholder = new StackPane(placeholderText);
        placeholder.getStyleClass().add("list-placeholder-box");

        ingredientsNewRecipeListView.setPlaceholder(placeholder);
    }
    /**
     * sets scene factory and database
     * @param sceneFactory
     * @author Kotryna
     */
    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
    }

    /**
     * fetches the ingredient and amount the user put in, sends it to the recipe class and shows the ingredient
     * and amount in the list
     * @author Kotryna
     */
    public void addIngredient() {
        String ingredient = ingredientNewRecipeField.getText();
        String amount = measurementNewRecipeField.getText();

        if(ingredient != null && amount != null && !ingredient.isEmpty() && !amount.isEmpty()) {
            newRecipe.addIngredient(ingredient);
            newRecipe.addIngredientAmount(amount);
            ingredientsNewRecipeListView.getItems().add(ingredient + " " + amount);

            ingredientNewRecipeField.clear();
            measurementNewRecipeField.clear();
            ingredientNewRecipeField.requestFocus();
        } else {
            alert.basicError("Please fill out both ingredient and amount.");
        }
    }

    /**
     * fetches the data the user has submitted for a new recipe, creates a new Recipe object and
     * sends it to the database object to get added to the database. calls for methods to inform the user
     * if it was successful or not.
     * @throws Exception
     * @author Kotryna
     */
    public void pressedSaveRecipeButton() throws Exception {
        String recipeName = nameNewRecipeField.getText();
        String instructions = newRecipeInstructionsText.getText();
        ArrayList<String> ingredients = newRecipe.getIngredients();
        ArrayList<String> amounts = newRecipe.getIngredientAmount();
        System.out.println("When clicking new recipe " + newRecipe.getIngredients());
        System.out.println("When clicking new recipe " + newRecipe.getIngredientAmount());

        if(!recipeName.isEmpty() && !instructions.isEmpty() && !ingredientsNewRecipeListView.getItems().isEmpty()) {

            Recipe newRecipe = new Recipe();
            newRecipe.setRecipeName(recipeName);
            newRecipe.setInstructions(instructions);
            newRecipe.setIngredients(ingredients);
            newRecipe.setIngredientAmount(amounts);
            boolean wasItSuccessful = database.addNewRecipe(newRecipe);
            if(wasItSuccessful){
                alert.basicConfirmation("Recipe added successfully: " + newRecipe.getRecipeName());
                reloadUserRecipes();
            } else {
                alert.basicError("There was a problem with saving the recipe, please try again.");
            }

        } else {
            alert.basicError("Please fill out all fields.");
        }
    }

    /**
     * this method creates a thread to fetch all user recipes in the background after
     * the user has added a new recipe
     * @author Kotryna
     */

    public void reloadUserRecipes() {
        new Thread(() -> {
            try {
                user.setUserRecipes(database.getUserRecipes(user.getUsername()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}