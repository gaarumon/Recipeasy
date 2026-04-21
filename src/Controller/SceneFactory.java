package Controller;

import GUI.GUIScenes;
import Model.Database;
import javafx.event.ActionEvent;
import java.io.IOException;

public class SceneFactory {

    private Database database = new Database();
    private GUIScenes gui;
    private String currentUser;


    /**
     * method that is called when initializing the main window.
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void createMainScene(ActionEvent event) throws IOException {
        gui = new GUIScenes();
        gui.setSceneFactory(this);
        gui.createMainWindow(event);

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
    public void createIngredientsScene(ActionEvent event) throws IOException {
        gui.openIngredientsWindow(event);
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
}
