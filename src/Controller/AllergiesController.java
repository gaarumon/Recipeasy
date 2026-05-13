package Controller;

import Model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class AllergiesController {


    private String currentUsername;

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
                Database db = new Database();
                db.addAllergy (currentUsername, allergy);
            } catch (Exception e) {
                e.printStackTrace();
            }

            newAllergyField.clear();
        }
    }


    private void loadAllergies() {

        try {
            Database db = new Database();
            ArrayList<String> list = db.getUserAllergies(currentUsername);

            allergyList.getItems().addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private SceneFactory sceneFactory;

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        setUsername(sceneFactory.getCurrentUser());
    }
    @FXML
    public void removeAllergy() {
        String selected = allergyList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            allergyList.getItems().remove(selected);
            try {
                Database db = new Database(); //sceneFactory.getDatabase()
                db.removeAllergy(currentUsername, selected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
