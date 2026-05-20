
package Controller;

import Model.Database;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class AllergiesController {

    private String currentUsername;
    private Database database;
    private SceneFactory sceneFactory;
    private User user; //kotryna

    @FXML
    private ListView<String> allergyList;

    @FXML
    private TextField newAllergyField;

    public void setUsername(String username) {
        this.currentUsername = username;
        loadAllergies();
    }

    @FXML
    public void addAllergy() {
        String allergy = newAllergyField.getText();
        if (!allergy.isEmpty()) {
            allergyList.getItems().add(allergy);
            try {
                database.addAllergy(currentUsername, allergy);
                //kotryna
                user.addAllergy(allergy);
                new Thread(() -> {
                    user.setCurrentRandomRecipe(database.getRandomRecipe(user.getUsername()));
                    user.setNextRandomRecipe(database.getRandomRecipe(user.getUsername()));
                }).start();//kotryna
            } catch (Exception e) {
                e.printStackTrace();
            }
            newAllergyField.clear();
        }
    }

    //changed so instead of getting allergy list from database, it gets from user class
    // and if there are no allergies, it tells the user /kotryna
    private void loadAllergies() {
        ArrayList<String> list = user.getAllergyList();
        if (list != null) {
            allergyList.getItems().addAll(list);
        } else {
            allergyList.setPlaceholder(new Label("No user recipes found :("));
        }
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
        setUsername(sceneFactory.getCurrentUser());
    }

    @FXML
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
}
