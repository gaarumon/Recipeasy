package Controller;

import GUI.GUIScenes;
import Model.Database;
import Model.Recipe;
import Model.ShoppingList;
import Model.User;
import javafx.event.ActionEvent;

//import java.awt.event.MouseEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

/**
 * this class holds responsibility for making sure that every controller shares the same database and user objects.
 * it as well communicates between different controllers and the GUIScene class to launch different scenes
 * @authors Kotryna,
 */

public class SceneFactory {

    private Database database = new Database();
    private GUIScenes gui = new GUIScenes();
    private String currentUser;
    private ShoppingList shoppingList = new ShoppingList();
    private MainSceneController mainSceneController;
    private User user;

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
    public void createMyRecipeScene(MouseEvent event) throws Exception {
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

    public void createShoppingListScene(MouseEvent event) throws IOException {
        gui.createShoppingListWindow(event);
    }

    public void createLoginScene(MouseEvent event) throws IOException {
        gui.createLogInWindow(event);
    }

    public void createSignUpScene() throws IOException{
        gui.createSignUpWindow();
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void createIngredientsScene(MouseEvent event) throws IOException {
        gui.openIngredientsWindow(event);
    }
    public Database getDatabase() {
        return database;
    }

    public SceneFactory getSceneFactory() {
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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
    public void createAllergyWindow(MouseEvent event) throws IOException {
        gui.createAllergyWindow(event);

    }

    public void createFilterScene(ActionEvent event) throws IOException{
        gui.createFilterWindow(event);
    }

    public MainSceneController getMainSceneController(){
        return this.mainSceneController;
    }
}
