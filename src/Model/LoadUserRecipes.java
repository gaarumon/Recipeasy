package Model;

public class LoadUserRecipes implements Runnable{

    private String username;
    private User user;
    private Database database;

    public LoadUserRecipes(User user){
        this.user = user;
        this.username = user.getUsername();
        this.database = new Database();
    }
    @Override
    public void run() {

        try {
            user.setFavouriteRecipes(database.getFavouriteRecipes(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            user.setUserRecipes(database.getUserRecipes(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        user.setCurrentRandomRecipe(database.getRandomRecipe(username));
        user.setNextRandomRecipe(database.getRandomRecipe(username));

    }
}
