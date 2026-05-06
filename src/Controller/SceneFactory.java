package Controller;

import GUI.GUIScenes;
import Model.Database;
import Model.Recipe;
import Model.ShoppingList;
import javafx.event.ActionEvent;
import java.io.IOException;

public class SceneFactory {

    private Database database = new Database();
    private GUIScenes gui = new GUIScenes();
    private String currentUser;
    private ShoppingList shoppingList = new ShoppingList();
    private MainSceneController mainSceneController;

    public SceneFactory(){
        gui.setSceneFactory(this);
    }

    /**
     * method that is called when initializing the main window.
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void createMainScene(ActionEvent event) throws IOException {
        //gui = new GUIScenes();
        //gui.setSceneFactory(this);
        gui = new GUIScenes();
        gui.setSceneFactory(this);
        mainSceneController = gui.createMainWindow(event);

    }
    /**
     * method that is called when initializing the My Recipe window.
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void createMyRecipeScene(ActionEvent event) throws IOException {
        gui.createMyRecipeWindow(event);
    }

    /**
     * method that is called when initializing the New Recipe window.
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void createNewRecipeScene(ActionEvent event) throws IOException {
        gui.createNewRecipeWindow(event);
    }

    public void createFavouritesScene() throws Exception{
        gui.createFavouritesWindow();
    }

    public void createShoppingListScene(ActionEvent event) throws IOException {
        gui.createShoppingListWindow(event);
    }

    public void createLoginScene(ActionEvent event) throws IOException {
        gui.createLogInWindow(event);
    }

    public void createSignUpScene() throws IOException{
        gui.createSignUpWindow();
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public Database getDatabase() {
        return database;
    }

    public SceneFactory getSceneFactory() {
        return this;
    }

    public void setCurrentUser(String currentUser){
        this.currentUser = currentUser;
    }

    public String getCurrentUser(){
        return this.currentUser;
    }

    public void selectedRecipe(Recipe selectedRecipe) {
        mainSceneController.recipeSelected(selectedRecipe);

    }
}
