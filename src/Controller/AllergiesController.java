
package Controller;

import Model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class AllergiesController {

    private String currentUsername;
    private Database database;
    private SceneFactory sceneFactory;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            newAllergyField.clear();
        }
    }

    private void loadAllergies() {
        try {
            ArrayList<String> list = database.getUserAllergies(currentUsername);
            if (list != null) {
                allergyList.getItems().addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        setUsername(sceneFactory.getCurrentUser());
    }

    @FXML
    public void removeAllergy() {
        String selected = allergyList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            allergyList.getItems().remove(selected);
            try {
                database.removeAllergy(currentUsername, selected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
