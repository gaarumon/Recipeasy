package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class IngredientsController {

    @FXML
    private ListView<String> ingredientList;

    @FXML
    private TextField newIngredientField;


    @FXML
    public void initialize() {

        ingredientList.getItems().add("Tomat");
        ingredientList.getItems().add("Pasta");
        ingredientList.getItems().add("Kyckling");
    }


    @FXML
    private void addIngredient() {

        String ingredient = newIngredientField.getText();

        if (!ingredient.isEmpty()) {
            ingredientList.getItems().add(ingredient);
            newIngredientField.clear();
        }
    }


    @FXML
    private void removeIngredient() {

        String selected = ingredientList.getSelectionModel().getSelectedItem();

        if (selected != null) {
            ingredientList.getItems().remove(selected);
        }
    }
}
