package Controller;

import Model.Database;
import Model.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class FavouritesController {
    @FXML
    private ListView<Recipe> favouritesListView;
    private SceneFactory sceneFactory;
    private Database database;
    private MainSceneController mainSceneController;

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
    }

    public void setMainSceneController (MainSceneController mainSceneController){
        this.mainSceneController = mainSceneController;
    }

    public void loadFavourites() throws Exception {
        ArrayList<Recipe> favouriteRecipes = database.getFavouriteRecipes(sceneFactory.getCurrentUser());
        System.out.println(sceneFactory.getCurrentUser());
        if (favouriteRecipes != null) {
            for (Recipe r : favouriteRecipes) {
                favouritesListView.getItems().add(r);
            }
            } else {
            favouritesListView.setPlaceholder(new Label("No favourite recipes found :("));
        }

        favouritesListView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Recipe selected = favouritesListView.getSelectionModel().getSelectedItem();
                if (selected != null){
                    System.out.println("Visar recept: " + selected.getRecipeName()); // Här ska en metod som visar receptet i mainfönstret anropas (Michael kommer implementera en sådan metod)
                    ((Stage) favouritesListView.getScene().getWindow()).close();
                }
            }
        });
    }

    @FXML
    public void handleRemove(ActionEvent event) throws Exception{
        Recipe selected = favouritesListView.getSelectionModel().getSelectedItem();
        if (selected != null){
            int recipe_id = selected.getIndex();
            String username = sceneFactory.getCurrentUser();
            database.removeFavouriteRecipe(username, recipe_id);
            favouritesListView.getItems().remove(selected);
        }
    }
}
