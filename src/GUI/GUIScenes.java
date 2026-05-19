package GUI;
import Controller.AllergiesController;
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
        stageMain.setScene(sceneMain);
        stageMain.setResizable(true);
        stageMain.sizeToScene();
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
    public void openIngredientsWindow(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForIngredientsWindow.fxml"));
        Parent root = loader.load();

        IngredientsController controller = loader.getController();
        controller.setSceneFactory(sceneFactory);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
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

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageShoppingList.initOwner(mainStage);
        stageShoppingList.show();
    }


    /**
     * sets the scene factory so all controllers have access to the same scene
     * @author Kotryna
     */

    public void createFavouritesWindow () throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForFavourites.fxml"));
        Parent root = loader.load();
        FavouritesController favouritesController = loader.getController();
        favouritesController.setSceneFactory(sceneFactory);
        Stage favouriteStage = new Stage();
        favouriteStage.initOwner(stageMain);
        favouriteStage.setScene(new Scene(root));
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
        signUpStage.show();
    }

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }

    public void createAllergyWindow(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AllergiesWindow.fxml"));
        Parent root = loader.load();

        AllergiesController allergiesController = loader.getController();
        allergiesController.setSceneFactory(sceneFactory);

        Stage allergyStage = new Stage();
        allergyStage.setScene(new Scene(root));

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        allergyStage.getScene().getStylesheets().add(css);

        allergyStage.setTitle("Allergies");

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        allergyStage.initOwner(mainStage);

        allergyStage.show();
    }

    public void createFilterWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneForFilters.fxml"));
        Parent root = loader.load();

        FilterController filterController = loader.getController();
        filterController.setMainSceneController(sceneFactory.getMainSceneController());
        filterController.setSceneFactory(sceneFactory);
        filterController.loadFilterState();

        Stage filterStage = new Stage();
        filterStage.setScene(new Scene(root));
        filterStage.setResizable(false);

        String css = this.getClass().getResource("styleSheet.css").toExternalForm();
        filterStage.getScene().getStylesheets().add(css);

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        filterStage.initOwner(mainStage);

        filterStage.show();

    }
}
