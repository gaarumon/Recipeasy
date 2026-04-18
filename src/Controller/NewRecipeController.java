package Controller;

import Model.Database;

public class NewRecipeController {
    private SceneFactory sceneFactory;
    private Database database;

    public void setSceneFactory(SceneFactory sceneFactory){

        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }
}
