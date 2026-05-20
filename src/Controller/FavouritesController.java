package Controller;

import Model.Database;
import Model.Recipe;
import Model.User;
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
    private User user;

    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
        this.database = sceneFactory.getDatabase();
        this.user = sceneFactory.getUser();
    }

    public void loadFavourites() throws Exception {
        ArrayList<Recipe> favouriteRecipes = user.getFavouriteRecipes();
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
                    ((Stage) favouritesListView.getScene().getWindow()).close();
                     sceneFactory.selectedRecipe(selected);
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
            user.removeFavourite(selected);
            favouritesListView.getItems().remove(selected);
        }
    }
}
