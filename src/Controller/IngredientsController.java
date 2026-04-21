package Controller;

import Model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class IngredientsController {

    @FXML
    private ListView<String> ingredientList;

    @FXML
    private TextField newIngredientField;

    private Database database;
    private SceneFactory sceneFactory;


    @FXML
    public void initialize() {
        // lämnas tom medvetet
    }


    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();

        try {
            ingredientList.getItems().addAll(
                    database.getUserIngredients(sceneFactory.getCurrentUser())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addIngredient() {

        String ingredient = newIngredientField.getText();

        if (!ingredient.isEmpty()) {

            try {
                database.addIngredient(sceneFactory.getCurrentUser(), ingredient);
                ingredientList.getItems().add(ingredient);
                newIngredientField.clear();

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
                ingredientList.getItems().remove(selected);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}