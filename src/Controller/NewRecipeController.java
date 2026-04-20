package Controller;

import Model.Database;

public class NewRecipeController {
    private SceneFactory sceneFactory;
    private Database database;


    /**
     * sets scene factory and database
     * @param sceneFactory
     * @author Kotryna
     */
    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }
}
