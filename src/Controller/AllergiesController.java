
package Controller;

import GUI.Alerts;
import Model.Database;
import Model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import GUI.SpeechBubbleHelper;
import javafx.application.Platform;

import java.sql.SQLException;
import java.util.ArrayList;

public class AllergiesController {

    private String currentUsername;
    private Database database;
    private SceneFactory sceneFactory;
    private User user; //kotryna
    private Alerts alert = new Alerts();

    private SpeechBubbleHelper speechBubbleHelper;

    @FXML
    private ImageView allergyCharacter;

    @FXML
    private ListView<String> allergyList;

    @FXML
    private TextField newAllergyField;
    /**
     * Sets the username for the current user and loads their allergies.
     * @param username
     * @author Fatema Ahmadi
     */
    public void setUsername(String username) {
        this.currentUsername = username;
        loadAllergies();
    }

    @FXML
    public void addAllergy() {
        String allergy = newAllergyField.getText().trim();
        ObservableList<String> allergies = allergyList.getItems();
        if (!allergy.isEmpty()) {
            if(allergies.contains(allergy)){
                alert.basicError("Looks like you already have this allergy in your list.");
                return;
            }
            allergyList.getItems().add(allergy);
            try {
                database.addAllergy(currentUsername, allergy);
                //kotryna
                user.addAllergy(allergy);
                new Thread(() -> {
                    try {
                        user.setCurrentRandomRecipe(database.getRandomRecipe(user.getUsername()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        user.setNextRandomRecipe(database.getRandomRecipe(user.getUsername()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();//kotryna
            } catch (Exception e) {
                e.printStackTrace();
            }
            newAllergyField.clear();
        }
    }

    /**
     * Loads all allergies for the current user from the database and displays them in the list.
     * @author Fatema Ahmadi
     */
    private void loadAllergies() {
        allergyList.getItems().clear();

        ArrayList<String> list = user.getAllergyList();
        if (list != null && !list.isEmpty()) {
            allergyList.getItems().addAll(list);
        } else {
            allergyList.setPlaceholder(new Label("No user allergies found :("));
        }
    }
    /**
     * Sets the scene factory and initializes the database and current user.
     * @param sceneFactory the scene factory to set
     * @author Fatema Ahmadi
     */
    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
        setUsername(sceneFactory.getCurrentUser());

        createSpeechBubble();
    }

    @FXML
    /**
     * Removes the selected allergy from both the UI list and the database.
     * @author Fatema Ahmadi
     */
    public void removeAllergy() {
        String selected = allergyList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            allergyList.getItems().remove(selected);
            try {
                database.removeAllergy(currentUsername, selected);
                user.removeAllergy(selected); //kotryna
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleAllergyCharacterClick(){
        speechBubbleHelper.toggleHelpBubble();
    }

    public void closeSpeechBubble(){
        if(speechBubbleHelper != null){
            speechBubbleHelper.hideAll();
        }
    }

    private void createSpeechBubble(){
        speechBubbleHelper = new SpeechBubbleHelper(
                allergyCharacter,
                "Click me!",
                "Add allergies here, like peanut or milk. Recipeasy uses them for random recipes and for search when the allergy filter is on."
        );

        speechBubbleHelper.setFlipped(true);
        speechBubbleHelper.setClickPlacement(SpeechBubbleHelper.Placement.LEFT);
        speechBubbleHelper.setHelpPlacement(SpeechBubbleHelper.Placement.LEFT);

        speechBubbleHelper.setClickAdjustment(75, -12);
        speechBubbleHelper.setHelpAdjustment(85, -30);

        Platform.runLater(() -> speechBubbleHelper.showClickBubbleAfterDelay(2));
    }
}
