package Model;

import java.sql.SQLException;

/**
 * automatically fetches user information in advance to reduce loading times while using the app
 * the run method starts two mini threads for getting favourite and user recipes since they take
 * longer time to get processed
 * @author Kotryna
 */

public class LoadUserInfo implements Runnable{
    private String username;
    private User user;
    private Database database;

    public LoadUserInfo(User user){
        this.user = user;
        this.username = user.getUsername();
        this.database = new Database();
    }

    /**
     * this method fetches user data and saves it in the user class
     * automatically fetches: allergies, ingredients, favourite recipes, user recipes,
     * and two next random recipes
     * @author Kotryna
     */
    @Override
    public void run() {

        new Thread(() -> {
            try {
                user.setFavouriteRecipes(database.getFavouriteRecipes(username));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                user.setUserRecipes(database.getUserRecipes(username));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        try {
            user.setCurrentRandomRecipe(database.getRandomRecipe(username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            user.setNextRandomRecipe(database.getRandomRecipe(username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            user.setAllergyList(database.getUserAllergies(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            user.setIngredientList(database.getUserIngredients(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
           user.setIngredientBasedRecipes(database.getRecipesBasedOnIngredients(user.getIngredientList()));
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        /*try {
            user.setShoppingList(database.getShoppingList(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }
}
