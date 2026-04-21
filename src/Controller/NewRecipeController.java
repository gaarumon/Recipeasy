package Controller;

import Model.Database;
import Model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import GUI.Alerts;

import java.util.ArrayList;

public class NewRecipeController {
    private SceneFactory sceneFactory;
    private Database database;
    private Recipe newRecipe = new Recipe();
    private Alerts alert = new Alerts();

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
     * sets scene factory and database
     * @param sceneFactory
     * @author Kotryna
     */
    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    public void addIngredient() {
        String ingredient = ingredientNewRecipeField.getText();
        String amount = measurementNewRecipeField.getText();

        if(ingredient != null && amount != null && !ingredient.isEmpty() && !amount.isEmpty()) {
            newRecipe.addIngredient(ingredient);
            ingredientsNewRecipeListView.getItems().add(ingredient + " " + amount);
        } else {
            alert.basicError("Please fill out both ingredient and amount.");
        }
    }

    public void pressedSaveRecipeButton() throws Exception {
        String recipeName = nameNewRecipeField.getText();
        String instructions = newRecipeInstructionsText.getText();

        if(!recipeName.isEmpty() && !instructions.isEmpty() && !ingredientsNewRecipeListView.getItems().isEmpty()) {

            Recipe newRecipe = new Recipe();
            newRecipe.setRecipeName(recipeName);
            newRecipe.setInstructions(instructions);
            ArrayList<String> ingredients = new ArrayList<>(ingredientsNewRecipeListView.getItems());
            newRecipe.setIngredients(ingredients);
            boolean wasItSuccessful = database.addNewRecipe(newRecipe);
            if(wasItSuccessful){
                alert.basicConfirmation("Recipe added successfully: " + newRecipe.getRecipeName());
            } else {
                alert.basicError("There was a problem with saving the recipe, please try again.");
            }

        } else {
            alert.basicError("Please fill out all fields.");
        }
    }
}