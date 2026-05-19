package Controller;

import Model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class FilterController {
    private SceneFactory sceneFactory;
    private Database database;
    @FXML
    private CheckBox allergyCheckBox;
    @FXML
    private CheckBox ingredientCheckBox;
    private MainSceneController mainSceneController;

    public void setMainSceneController(MainSceneController mainSceneController){
        this.mainSceneController = mainSceneController;
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    public void handleSaveFilters() {
        mainSceneController.setFilters(allergyCheckBox.isSelected(), ingredientCheckBox.isSelected());
        ((Stage) allergyCheckBox.getScene().getWindow()).close();
    }

    @FXML
    public void handleCancelFilters() {
        ((Stage) allergyCheckBox.getScene().getWindow()).close();
    }

    public void loadFilterState(){
        allergyCheckBox.setSelected(sceneFactory.getMainSceneController().getFilterByAllergies());
        ingredientCheckBox.setSelected(sceneFactory.getMainSceneController().getFilterByOwnedIngredients());
    }
}
