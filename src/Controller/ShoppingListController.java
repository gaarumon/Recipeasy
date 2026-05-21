package Controller;

import Model.Database;
import Model.ShoppingList;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import java.util.ArrayList;

/**
 * Controller for the shopping list window.
 * Handles button clicks and keeps the view, model and database in sync.
 *
 * @author Huu Trung Nguyen
 */
public class ShoppingListController implements Initializable{

    private SceneFactory sceneFactory;
    private ShoppingList shoppingList;
    private Database database;
    private User user;

    @FXML
    private ListView<String> shoppingListView;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField addIngredientField;

    /**
     * Sets up the list view when the window opens.
     * Multiple selection is enabled so the user can remove several items at once.
     *
     * @param url location used by JavaFX
     * @param resourceBundle resources used by JavaFX
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shoppingListView.setPlaceholder(new Label("Your shopping list is empty"));
        shoppingListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Gives this controller access to shared objects from the app.
     *
     * @param sceneFactory the shared scene factory
     */
    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.shoppingList = sceneFactory.getShoppingList();
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
        refreshView();
    }

    /**
     * adds the typed ingredient to the shopping list.
     *
     * @param event the button click event
     */
    @FXML
    public void pressedAddButton(ActionEvent event){
        String input = addIngredientField.getText();
        if(input == null || input.isBlank()){
            setStatus("Type an ingredient first");
            return;
        }
        boolean added = shoppingList.addIngredient(input);

        if(added) {
            try {
                database.addToShoppingList(sceneFactory.getCurrentUser(), input.trim());
                refreshView();
                setStatus("Added: " + input.trim());
                addIngredientField.clear();
            } catch (Exception e){
                e.printStackTrace();
                setStatus("Could not add ingredient");
            }
        } else{
            setStatus(input.trim() + " is already on the list");
        }
    }

    /**
     * clears the whole shopping list
     *
     * @param event the button click event
     */
    @FXML
    public void pressedClearButton(ActionEvent event){
        if(shoppingList.getIngredients().isEmpty()){
            setStatus("Shopping list is already empty");
            return;
        }

        try{
            shoppingList.clear();
            database.clearShoppingList(sceneFactory.getCurrentUser());
            refreshView();
            setStatus("Shopping list cleared");
        } catch(Exception e){
            e.printStackTrace();
            setStatus("Could not clear shopping list");
        }
    }

    /**
     * removes the selected ingredient or ingredients.
     *
     * @param event the button click event
     */
    @FXML
    public void pressedRemoveButton(ActionEvent event) {
        ObservableList<String> selected = shoppingListView.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) {
            setStatus("Select one or more ingredients to remove");
            return;
        }
        removeIngredients(selected);
    }

    /**
     * removes several selected ingredients from the model and database.
     *
     * @param selectedIngredients the ingredients selected in the list view
     */
    private void removeIngredients(ObservableList<String> selectedIngredients){
        ArrayList<String> ingredientsToRemove = new ArrayList<>(selectedIngredients);
        shoppingList.removeIngredients(ingredientsToRemove);

        try{
            database.removeFromShoppingList(sceneFactory.getCurrentUser(), ingredientsToRemove);
            refreshView();

            if(ingredientsToRemove.size() == 1){
                setStatus("Removed: " + ingredientsToRemove.get(0));
            }else{
                setStatus("Removed " + ingredientsToRemove.size() + " ingredients");
            }
        } catch (Exception e){
            e.printStackTrace();
            setStatus("Could not remove selected ingredients");
        }
    }

    /**
     * refreshes the list view from the current shopping list.
     *
     * @param event the button click event
     */
    @FXML
    public void pressedRefreshButton(ActionEvent event) {
        refreshView();
        setStatus("");
    }

    /**
     * updates what the user sees in the list view
     */
    private void refreshView() {
        shoppingListView.getItems().setAll(shoppingList.getIngredients());
    }

    /**
     * shows a short message in the shopping list window.
     * @param text the message to show
     */
    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}
