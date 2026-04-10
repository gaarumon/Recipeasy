package Controller;

import GUI.GUIScenes;
import Model.Database;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SceneFactory {

    private Database database = new Database();
    private GUIScenes gui;


    /**
     * method that is called when initializing the main window.
     * @param event
     * @throws IOException
     */
    public void createMainScene(ActionEvent event) throws IOException {
        gui = new GUIScenes();
        gui.setSceneFactory(this);
        gui.createMainWindow(event);

    }

    public Database getDatabase() {
        return database;
    }

    public SceneFactory getSceneFactory() {
        return this;
    }
}
