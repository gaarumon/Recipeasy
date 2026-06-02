package Controller;

import Model.Database;
import Model.Recipe;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import GUI.SpeechBubbleHelper;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * this class is responible for showing the user their recipes and storing a button for
 * a new recipe
 */
public class MyRecipeController implements Initializable {
    private SceneFactory sceneFactory;
    private Database database;
    private User user;

    @FXML
    private ListView<Recipe> myRecipesListView;
    @FXML
    private Button newRecipeButton;

    @FXML private Button deleteRecipeButton;

    private SpeechBubbleHelper speechBubbleHelper;

    @FXML
    private ImageView myRecipeCharacter;

    @FXML
    /**
     * Deletes the selected recipe from the user's recipe list in both the UI and the database.
     * @author Fatema Ahmadi
     */
    public void handleDeleteRecipe() {
        Recipe selected = myRecipesListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        try {
            database.deleteUserRecipe(sceneFactory.getCurrentUser(), selected.getIndex());
            user.removeUserRecipe(selected);
            myRecipesListView.getItems().remove(selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * initializes the list view for user's own recipes. when a recipe in the list is selected,
     * calls recipeSelected() method. currently hard coded to show that user has no recipes.
     * @param url
     * @param resourceBundle
     * @author Kotryna
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myRecipesListView.setFocusTraversable(false);
        myRecipesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ((Stage) myRecipesListView.getScene().getWindow()).close();
                recipeSelected();
            }
        });
        deleteRecipeButton.setFocusTraversable(false);
    }


    /**
     * this method gets all user recipes to show in my recipe window. if the program hasn't fetched
     * the user recipe data and it is null, it gets the list of user recipes itself
     * @throws Exception
     * @author Kotryna
     */

    public void loadMyRecipes() throws Exception {
        ArrayList<Recipe> userRecipes = user.getUserRecipes();
        if(userRecipes == null) {
            userRecipes = database.getUserRecipes(user.getUsername());
        }
        if (userRecipes != null) {
            for (Recipe r : userRecipes) {
                myRecipesListView.getItems().add(r);
            }
        } else {
            myRecipesListView.setPlaceholder(new Label("No user recipes found :("));
        }
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

        speechBubbleHelper = new SpeechBubbleHelper(
                myRecipeCharacter,
                "Click me!",
                "Your recipes appear here. Double-click a recipe to open it on the main page, press New Recipe to create one, or select a recipe and use the trash button to delete it."
        );

        speechBubbleHelper.setFlipped(false);
        speechBubbleHelper.setClickPlacement(SpeechBubbleHelper.Placement.LEFT);
        speechBubbleHelper.setHelpPlacement(SpeechBubbleHelper.Placement.LEFT);
        speechBubbleHelper.setClickAdjustment(190, -22);
        speechBubbleHelper.setHelpAdjustment(335, -40);

        Platform.runLater(() -> speechBubbleHelper.showClickBubbleAfterDelay(2));
    }

    /**
     * method called when a recipe is selected from the recipe list. currently prints recipe's index
     * in the database
     * @author Kotryna
     */
    public void recipeSelected() {
        Recipe selectedRecipe = myRecipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            sceneFactory.selectedRecipe(selectedRecipe);
            int index = selectedRecipe.getIndex();
            System.out.println("Recipe index in database: " + index);
        }
    }


    /**
     * method called when New Recipe button is clicked
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void pressedNewRecipeButton(ActionEvent event) throws IOException {
        sceneFactory.createNewRecipeScene(event);

    }

    @FXML
    public void handleMyRecipeCharacterClick() {
        speechBubbleHelper.toggleHelpBubble();
    }

    public void closeSpeechBubble() {
        if (speechBubbleHelper != null) {
            speechBubbleHelper.hideAll();
        }
    }
}
