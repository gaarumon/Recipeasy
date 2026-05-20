package Model;

public class LoadUserInfo implements Runnable{
    private String username;
    private User user;
    private Database database;

    public LoadUserInfo(User user){
        this.user = user;
        this.username = user.getUsername();
        this.database = new Database();
    }

    @Override
    public void run() {
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

      /*  try {
            user.setFavouriteRecipes(database.getFavouriteRecipes(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            user.setUserRecipes(database.getUserRecipes(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        try {
            user.setShoppingList(database.getShoppingList(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //user.setCurrentRandomRecipe(database.getRandomRecipe(username));
        //user.setNextRandomRecipe(database.getRandomRecipe(username));
    }
}
