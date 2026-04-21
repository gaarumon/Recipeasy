package Controller;

import Model.Database;
import Model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewRecipeController {
    private SceneFactory sceneFactory;
    private Database database;
    private Recipe newRecipe = new Recipe();

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
            ingredientsNewRecipeListView.getItems().add(ingredient);
        }

    }
}
