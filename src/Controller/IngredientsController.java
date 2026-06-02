package Controller;

import GUI.Alerts;
import Model.Database;
import Model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import GUI.SpeechBubbleHelper;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class IngredientsController {

    @FXML
    private ListView<String> ingredientList;

    @FXML
    private TextField newIngredientField;

    private Database database;
    private SceneFactory sceneFactory;
    private User user;
    private Alerts alert = new Alerts();

    private SpeechBubbleHelper speechBubbleHelper;

    @FXML
    private ImageView ingredientCharacter;


    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
        loadIngredients();

        createSpeechBubble();
    }

    //before ingredients were set in setSceneFactory method, I separated them
    // added a check if user has ingredients, if not, user gets a message /kotryna
    public void loadIngredients() {

        ingredientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ArrayList<String> list = user.getIngredientList();
        if (list != null) {
            ingredientList.getItems().addAll(list);
        } else {
            ingredientList.setPlaceholder(new Label("No user ingredients found :("));
        }

    }


    @FXML
    private void addIngredient() {

        String ingredient = newIngredientField.getText();
        ObservableList<String> ingredients = ingredientList.getItems();

        if (!ingredient.isEmpty()) {
            if(ingredients.contains(ingredient)) {
                alert.basicError("Looks like you already have this ingredient in your list.");
                return;
            }

            try {
                database.addIngredient(sceneFactory.getCurrentUser(), ingredient);
                user.addIngredient(ingredient); //kotryna
                ingredientList.getItems().add(ingredient);
                newIngredientField.clear();
                refreshIngredientBasedRecipes();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void removeIngredient() {

        String selected = ingredientList.getSelectionModel().getSelectedItem();

        if (selected != null) {

            try {
                database.removeIngredient(sceneFactory.getCurrentUser(), selected);
                user.removeIngredient(selected); //kotryna
                ingredientList.getItems().remove(selected);
                refreshIngredientBasedRecipes();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshIngredientBasedRecipes(){
        new Thread(() -> {
            try{
                user.setIngredientBasedRecipes(database.getRecipesBasedOnIngredients(user.getIngredientList()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void handleIngredientRecipesButton(){
        ObservableList<String> selected = ingredientList.getSelectionModel().getSelectedItems();

        if(selected.isEmpty()) {
            sceneFactory.getMainSceneController().showIngredientFilteredRecipes(user.getIngredientBasedRecipes());
        } else {
            sceneFactory.getMainSceneController().showIngredientFilteredRecipes(user.getSelectedIngredientsBasedRecipe(selected));

        }
    }

    @FXML
    public void handleIngredientCharacterClick(){
        speechBubbleHelper.toggleHelpBubble();
    }

    public void closeSpeechBubble(){
        if(speechBubbleHelper != null){
            speechBubbleHelper.hideAll();
        }
    }

    private void createSpeechBubble(){
        speechBubbleHelper = new SpeechBubbleHelper(
                ingredientCharacter,
                "Click me!",
                "Add ingredients you have at home, like pasta or milk. Recipeasy can then show recipes based on what you already have."
        );

        speechBubbleHelper.setFlipped(false);
        speechBubbleHelper.setClickPlacement(SpeechBubbleHelper.Placement.RIGHT);
        speechBubbleHelper.setHelpPlacement(SpeechBubbleHelper.Placement.RIGHT);

        speechBubbleHelper.setClickAdjustment(-55, -12);
        speechBubbleHelper.setHelpAdjustment(-65, -30);

        Platform.runLater(() -> speechBubbleHelper.showClickBubbleAfterDelay(2));
    }
}