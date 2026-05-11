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

//import java.awt.event.MouseEvent;
import javafx.scene.input.MouseEvent;
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

    public MainSceneController createMainWindow (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForMain.fxml"));
        rootMain = loader.load();
        stageMain = (Stage)((Node)event.getSource()).getScene().getWindow();

        MainSceneController controllerMain = loader.getController(); //saves exact controller instance
        controllerMain.setSceneFactory(sceneFactory.getSceneFactory()); //passes on scene factory for database access

        sceneMain = new Scene(rootMain);
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        sceneMain.getStylesheets().add(css);

        double scale = scaleSceneToScreen(sceneMain, 1094, 801);
        stageMain.setWidth(1094 * scale);
        stageMain.setHeight(801 * scale + 28); //anpassa efter skärm

        stageMain.setScene(sceneMain);
        stageMain.setResizable(true);
        //stageMain.sizeToScene();
        stageMain.centerOnScreen();
        stageMain.show();
        return controllerMain;
    }

    /**
     * method responsible for initializing and creating My Recipe window when My Recipe button is
     * clicked
     * @param event
     * @throws IOException
     * @author Kotryna
     */

    public void createMyRecipeWindow(MouseEvent event) throws IOException {
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

        double scale = scaleSceneToScreen(sceneMyRecipe, 343, 469);
        stageMyRecipe.setWidth(343 * scale);
        stageMyRecipe.setHeight(469 * scale + 28);

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

        double scale = scaleSceneToScreen(sceneNewRecipe, 385, 551);
        stageNewRecipe.setWidth(385 * scale);
        stageNewRecipe.setHeight(551 * scale + 28);

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageNewRecipe.initOwner(mainStage);
        stageNewRecipe.show();
    }
    public void openIngredientsWindow(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForIngredientsWindow.fxml"));
        Parent root = loader.load();

        IngredientsController controller = loader.getController();
        controller.setSceneFactory(sceneFactory);
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        double scale = scaleSceneToScreen(scene, 383, 465);
        stage.setWidth(383 * scale);
        stage.setHeight(465 * scale + 28);

        stage.setScene(scene);

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(mainStage);

        stage.show();
    }


    public void createShoppingListWindow(MouseEvent event) throws IOException{
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

        double scale = scaleSceneToScreen(sceneShoppingList, 377, 528);
        stageShoppingList.setWidth(377 * scale);
        stageShoppingList.setHeight(528 * scale + 28);

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

        double scale = scaleSceneToScreen(favouriteStage.getScene(), 381, 471);
        favouriteStage.setWidth(381 * scale);
        favouriteStage.setHeight(471 * scale + 28);

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        favouriteStage.getScene().getStylesheets().add(css);
        favouritesController.loadFavourites();
        favouriteStage.show();

    }

    public void createLogInWindow(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUILaunch.class.getResource("SceneForLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("styleSheet.css").toExternalForm();

        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        LogInSignUpController loginLogInSignUpController = fxmlLoader.getController();
        loginLogInSignUpController.setSceneFactory(sceneFactory.getSceneFactory());

        scene.getStylesheets().add(css);
        loginStage.setScene(scene);
        loginStage.centerOnScreen();
        loginStage.show();
    }

    public void createSignUpWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SceneForSignUp.fxml"));
        Parent root = loader.load();
        LogInSignUpController controller = loader.getController();
        controller.setSceneFactory(sceneFactory);
        Stage signUpStage = new Stage();

        signUpStage.setScene(new Scene(root));
        double scale = scaleSceneToScreen(signUpStage.getScene(), 284, 280);
        signUpStage.setWidth(284 * scale);
        signUpStage.setHeight(280 * scale + 28);
        signUpStage.show();
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }

    private double scaleSceneToScreen(Scene scene, double designWidth, double designHeight) {
        javafx.geometry.Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        double scale = Math.min(1.0, Math.min(screen.getWidth() / designWidth, screen.getHeight() / designHeight) * 0.92);
        scene.getRoot().getTransforms().add(new javafx.scene.transform.Scale(scale, scale, 0, 0));
        return scale;
    }
}


