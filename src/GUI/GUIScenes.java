package GUI;

import Controller.FavouritesController;
import Controller.MainSceneController;
import Controller.SceneFactory;
import Controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

/**
 * Class responsible for creation/initialization of all scenes/windows except for login/signup.
 * This is where we have methods for creating Main window, favourite, allergies, shopping list and
 * other scenes.
 * After initializing the scene, its controller is created automatically, so you can save the
 * exact instance of the controller like in the createMainWindow(); example bellow.
 * @author Kotryna
 */

public class GUIScenes {

    private Stage stageMain;
    private Scene sceneMain;
    private Parent rootMain;
    private SceneFactory sceneFactory;

    /**
     * method responsible for initializing the main window of the program
     * @param event
     * @throws IOException
     * @author Kotryna
     */

    public void createMainWindow (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForMain.fxml"));
        rootMain = loader.load();
        stageMain = (Stage)((Node)event.getSource()).getScene().getWindow();

        MainSceneController controllerMain = loader.getController(); //saves exact controller instance
        controllerMain.setSceneFactory(sceneFactory.getSceneFactory()); //passes on scene factory for database access

        sceneMain = new Scene(rootMain);
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        sceneMain.getStylesheets().add(css);
        stageMain.setScene(sceneMain);
        stageMain.setResizable(true);
        stageMain.setMaximized(true);
        stageMain.show();
    }

    /**
     * method responsible for initializing and creating My Recipe window when My Recipe button is
     * clicked
     * @param event
     * @throws IOException
     * @author Kotryna
     */

    public void createMyRecipeWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(GUILaunch.class.getResource("SceneForMyRecipe.fxml"));
        Parent rootMyRecipe = loader.load();

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        MyRecipeController myRecipeController = loader.getController();
        myRecipeController.setSceneFactory(sceneFactory.getSceneFactory());

        Scene sceneMyRecipe = new Scene(rootMyRecipe);
        sceneMyRecipe.getStylesheets().add(css);

        Stage stageMyRecipe = new Stage();
        stageMyRecipe.setScene(sceneMyRecipe);
        stageMyRecipe.setResizable(false);


        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageMyRecipe.initOwner(mainStage);

        myRecipeController.loadMyRecipes();
        stageMyRecipe.show();
    }


    /**
     * method responsible for initializing and creating New Recipe window when New Recipe button is
     * clicked
     * @param event
     * @throws IOException
     * @author Kotryna
     */
    public void createNewRecipeWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(GUILaunch.class.getResource("SceneForNewRecipe.fxml"));
        Parent rootNewRecipe = loader.load();

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        NewRecipeController newRecipeController = loader.getController();
        newRecipeController.setSceneFactory(sceneFactory.getSceneFactory());

        Scene sceneNewRecipe = new Scene(rootNewRecipe);
        sceneNewRecipe.getStylesheets().add(css);

        Stage stageNewRecipe = new Stage();
        stageNewRecipe.setScene(sceneNewRecipe);
        stageNewRecipe.setResizable(false);


        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageNewRecipe.initOwner(mainStage);
        stageNewRecipe.show();
    }

    public void createShoppingListWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(GUILaunch.class.getResource("SceneForShoppingList.fxml"));
        Parent rootShoppingList = loader.load();

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        ShoppingListController shoppingListController = loader.getController();
        shoppingListController.setSceneFactory(sceneFactory.getSceneFactory());

        Scene sceneShoppingList = new Scene(rootShoppingList);
        sceneShoppingList.getStylesheets().add(css);

        Stage stageShoppingList = new Stage();
        stageShoppingList.setScene(sceneShoppingList);
        stageShoppingList.setResizable(false);
        stageShoppingList.setTitle("Shopping list");

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageShoppingList.initOwner(mainStage);
        stageShoppingList.show();
    }

    /**
     * sets the scene factory so all controllers have access to the same scene
     * @param sceneFactory
     * @author Kotryna
     */

    public void createFavouritesWindow () throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForFavourites.fxml"));
        Parent root = loader.load();
        FavouritesController favouritesController = loader.getController();
        favouritesController.setSceneFactory(sceneFactory);
        Stage favouriteStage = new Stage();
        favouriteStage.initModality(Modality.WINDOW_MODAL);
        favouriteStage.initOwner(stageMain);
        favouriteStage.setScene(new Scene(root));
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        favouriteStage.getScene().getStylesheets().add(css);
        favouritesController.loadFavourites();
        favouriteStage.show();

    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }
}
