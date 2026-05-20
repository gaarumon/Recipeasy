package Controller;

import Model.Database;
import Model.Recipe;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import GUI.Alerts;

import java.util.ArrayList;

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
     * sets scene factory and database
     * @param sceneFactory
     * @author Kotryna
     */
    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
    }

    public void addIngredient() {
        String ingredient = ingredientNewRecipeField.getText();
        String amount = measurementNewRecipeField.getText();

        if(ingredient != null && amount != null && !ingredient.isEmpty() && !amount.isEmpty()) {
            newRecipe.addIngredient(ingredient);
            newRecipe.addIngredientAmount(amount);
            ingredientsNewRecipeListView.getItems().add(ingredient + " " + amount);
        } else {
            alert.basicError("Please fill out both ingredient and amount.");
        }
    }

    /*public void pressedSaveRecipeButton() throws Exception {
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
                user.addUserRecipe(newRecipe);
            } else {
                alert.basicError("There was a problem with saving the recipe, please try again.");
            }

        } else {
            alert.basicError("Please fill out all fields.");
        }
    }*/

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

    public void reloadUserRecipes() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                user.setUserRecipes(database.getUserRecipes(user.getUsername()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
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