package Model;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private String username;
    private String password;

    public static Connection getDatabaseConnection() {
        String url = System.getenv("url_db");
        String user = System.getenv("user_db");
        String password = System.getenv("password_db");

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");
            return con;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println("con failed");
            return null;
        }
    }

    public static void addUser(String name, String password) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String INSERT = "INSERT INTO appuser (username, pass_word) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            System.out.println("User added!");
            pstmt.close();
            con.close();
        } catch (Exception e){
            if (con!=null){
                con.close();
            }
        }
    }

    public boolean logIn(String username, String password) throws Exception{
        Connection con = getDatabaseConnection();
        this.username = username;
        this.password = password;
        int count = 0;
        try {
            String QUERY = "SELECT COUNT(*) FROM appuser WHERE username = ? AND pass_word = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            pstmt.close();
            con.close();
            return count > 0;
        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
        }
        return count > 0;
    }

    public ArrayList<Recipe> searchRecipesByName(String searchText) throws Exception {

        Connection con = getDatabaseConnection();
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            String QUERY =
                    "SELECT recipe_id, recipe_name, recipe_instructions " +
                            "FROM recipe " +
                            "WHERE recipe_name ILIKE ?";

            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe();

                int recipeId = rs.getInt("recipe_id");
                recipe.setIndex(recipeId);
                recipe.setRecipeName(rs.getString("recipe_name"));
                recipe.setInstructions(rs.getString("recipe_instructions"));

                ArrayList<String> ingredients = new ArrayList<>();
                String ingredientQuery =
                        "SELECT recipe_ingredient FROM ingredient WHERE recipe_id = ?";

                PreparedStatement ingredientStmt = con.prepareStatement(ingredientQuery);
                ingredientStmt.setInt(1, recipeId);

                ResultSet ingredientRs = ingredientStmt.executeQuery();
                while (ingredientRs.next()) {
                    ingredients.add(ingredientRs.getString("recipe_ingredient"));
                }

                ingredientRs.close();
                ingredientStmt.close();

                recipe.setIngredients(ingredients);
                recipes.add(recipe);
            }

            rs.close();
            pstmt.close();
            con.close();

            if (recipes.isEmpty()) {
                return null;
            }

            return recipes;

        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            throw e;
        }
    }

    public boolean doesUsernameAlreadyExist(String username) throws Exception{
        Connection con = getDatabaseConnection();
        int count = 0;
        try {
            String QUERY = "SELECT COUNT(*) FROM appuser WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            pstmt.close();
            con.close();
            return count > 0;
        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
        }
        return count > 0;
    }

    public void addNewUserToDatabase(String username, String password) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String INSERT = "INSERT INTO appuser (username, pass_word) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            System.out.println("You have been registered!");
            pstmt.close();
            con.close();
        }catch (Exception e){
            if (con!=null){
                con.close();
            }
        }
    }

    public ArrayList<Recipe> getFavouriteRecipes(String username) throws Exception{
        Connection con = getDatabaseConnection();
        ArrayList<Recipe> favouriteRecipes = new ArrayList<>();

        try {
            String QUERY =
                    "SELECT r.recipe_id, r.recipe_name, r.recipe_instructions " +
                            "FROM recipe r " +
                            "JOIN favoritelist f ON r.recipe_id= f.recipe_id " +
                            "WHERE f.username = ?";

            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe();

                int recipeId = rs.getInt("recipe_id");
                recipe.setIndex(recipeId);
                recipe.setRecipeName(rs.getString("recipe_name"));
                recipe.setInstructions(rs.getString("recipe_instructions"));

                ArrayList<String> ingredients = new ArrayList<>();
                String ingredientQuery =
                        "SELECT recipe_ingredient FROM ingredient WHERE recipe_id = ?";

                PreparedStatement ingredientStmt = con.prepareStatement(ingredientQuery);
                ingredientStmt.setInt(1, recipeId);

                ResultSet ingredientRs = ingredientStmt.executeQuery();
                while (ingredientRs.next()) {
                    ingredients.add(ingredientRs.getString("recipe_ingredient"));
                }

                ingredientRs.close();
                ingredientStmt.close();

                recipe.setIngredients(ingredients);
                favouriteRecipes.add(recipe);
            }

            rs.close();
            pstmt.close();
            con.close();

            if (favouriteRecipes.isEmpty()) {
                return null;
            }
            return favouriteRecipes;

        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            throw e;
        }
    }

    public void removeFavouriteRecipe(String username, int recipe_id) throws Exception{
        Connection con = getDatabaseConnection();
        try {
            String QUERY = "DELETE FROM favoritelist WHERE username = ? AND recipe_id = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            pstmt.setInt(2, recipe_id);
            int rows = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch (Exception e){
            if (con!=null){
                con.close();
            }
        }
    }
    public void addIngredient(String username, String ingredient) throws Exception {

        Connection con = getDatabaseConnection();

        try {
            String INSERT = "INSERT INTO useringredient (username, ingredient_name) VALUES (?, ?)";

            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, username);
            pstmt.setString(2, ingredient);

            pstmt.executeUpdate();

            pstmt.close();
            con.close();

        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            throw e;
        }
    }

    public void removeIngredient(String username, String ingredient) throws Exception {

        Connection con = getDatabaseConnection();

        try {
            String DELETE = "DELETE FROM useringredient WHERE username = ? AND ingredient_name = ?";

            PreparedStatement pstmt = con.prepareStatement(DELETE);
            pstmt.setString(1, username);
            pstmt.setString(2, ingredient);

            pstmt.executeUpdate();

            pstmt.close();
            con.close();

        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            throw e;
        }
    }

public ArrayList<String> getUserIngredients(String username) throws Exception {

    Connection con = getDatabaseConnection();

    ArrayList<String> ingredients = new ArrayList<>();

    try {
        String QUERY = "SELECT ingredient_name FROM useringredient WHERE username = ?";

        PreparedStatement pstmt = con.prepareStatement(QUERY);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            ingredients.add(rs.getString("ingredient_name"));
        }

        rs.close();
        pstmt.close();
        con.close();

        return ingredients;

    } catch (Exception e) {
        if (con != null) {
            con.close();
        }
        throw e;
    }
}
    3}