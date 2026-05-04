package Controller;

import Model.Database;
import Model.ShoppingList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ResourceBundle;

public class ShoppingListController implements Initializable{

    private SceneFactory sceneFactory;
    private ShoppingList shoppingList;
    private Database database;

    @FXML
    private ListView<String> shoppingListView;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField addIngredientField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shoppingListView.setPlaceholder(new Label("Your shopping list is empty"));
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.shoppingList = sceneFactory.getShoppingList();
        this.database = sceneFactory.getDatabase();
        refreshView();
    }

    @FXML
    public void pressedAddButton(ActionEvent event){
        String input = addIngredientField.getText();
        if(input == null || input.isBlank()){
            setStatus("Type an ingredient first");
            return;
        }
        boolean added = shoppingList.addIngredient(input);

        if(added){
            try{
                database.addToShoppingList(sceneFactory.getCurrentUser(), input.trim());
            } catch (Exception e){
                e.printStackTrace();
            }
            refreshView();
            setStatus("Added: " + input.trim());
            addIngredientField.clear();
        }else{
            setStatus(input.trim() + " is already on the list");
        }
    }

    @FXML
    public void pressedRemoveButton(ActionEvent event) {
        String selected = shoppingListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select an ingredient to remove");
            return;
        }
        shoppingList.removeIngredient(selected);
        try{
            database.removeFromShoppingList(sceneFactory.getCurrentUser(), selected);
        } catch (Exception e){
            e.printStackTrace();
        }

        refreshView();
        setStatus("Removed: " + selected);
    }

    @FXML
    public void pressedRefreshButton(ActionEvent event) {
        refreshView();
        setStatus("");
    }

    private void refreshView() {
        shoppingListView.getItems().setAll(shoppingList.getIngredients());
    }

    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}
