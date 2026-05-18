package Model;

import javafx.scene.control.CheckBox;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.Period;
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

    public ArrayList<Recipe> searchRecipesByName(String searchText, String username) throws Exception {
        Connection con = getDatabaseConnection();
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            String QUERY =
                    "SELECT recipe_id, recipe_name, recipe_instructions " +
                            "FROM recipe " +
                            "WHERE recipe_name ILIKE ? " +
                            "AND NOT EXISTS ( " +
                            "SELECT 1 FROM ingredient i " +
                            "JOIN allergylist a ON LOWER(i.recipe_ingredient) LIKE '%' || LOWER(a.allergy) || '%' " +
                            "WHERE i.recipe_id = recipe.recipe_id AND a.username = ? " +
                            ")";

            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe();

                int recipeId = rs.getInt("recipe_id");
                recipe.setIndex(recipeId);
                recipe.setRecipeName(rs.getString("recipe_name"));
                recipe.setInstructions(rs.getString("recipe_instructions"));


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

    /**
     *
     * @param recipeId
     * @return
     * @throws Exception
     */
    public Recipe getRecipeDetails(int recipeId) throws Exception {
        Connection con = getDatabaseConnection();
        Recipe recipe = null;
        try {
            String recipeSQL = "SELECT recipe_id, recipe_name, recipe_instructions, recipe_image "
                    + "FROM recipe " +
                    "WHERE recipe_id = ?";
            PreparedStatement recipeStmt = con.prepareStatement(recipeSQL);
            recipeStmt.setInt(1, recipeId);

            ResultSet recipeRs = recipeStmt.executeQuery();

            if(recipeRs.next()) {
                recipe = new Recipe();
                recipe.setIndex(recipeRs.getInt("recipe_id"));
                recipe.setRecipeName(recipeRs.getString("recipe_name"));
                recipe.setInstructions(recipeRs.getString("recipe_instructions"));
                recipe.setImage(recipeRs.getString("recipe_image")); //added by kotryna

            }
            recipeRs.close();
            recipeStmt.close();

            if(recipe != null) {
                ArrayList<String> ingredients = new ArrayList<>();
                String ingredientSQL = "SELECT recipe_ingredient FROM ingredient WHERE recipe_id = ?";
                PreparedStatement ingredientStmt = con.prepareStatement(ingredientSQL);
                ingredientStmt.setInt(1, recipeId);
                ResultSet ingredientRs = ingredientStmt.executeQuery();

                while(ingredientRs.next()) {
                    ingredients.add(ingredientRs.getString("recipe_ingredient"));

                }
                ingredientRs.close();
                ingredientStmt.close();

                recipe.setIngredients(ingredients);
            }

            con.close();
            return recipe;
        } catch (Exception e) {
            if(con != null) {
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

    public String addFavouriteRecipe(String username, int recipe_id) throws Exception{
        Connection con = getDatabaseConnection();
        try {
            String CHECK = "SELECT COUNT(*) FROM favoritelist WHERE username = ? AND recipe_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(CHECK);
            checkStmt.setString(1, username);
            checkStmt.setInt(2, recipe_id);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            checkStmt.close();

            if (count > 0) {
                System.out.println("Recipe is already a favorite");
                con.close();
                return "ALREADY_EXISTS";
            }

            String INSERT = "INSERT INTO favoritelist (username, recipe_id) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, username);
            pstmt.setInt(2, recipe_id);
            pstmt.executeUpdate();
            System.out.println("Favourite added!");
            pstmt.close();
            con.close();
            return "ADDED";
        } catch (Exception e){
            if(con != null){
                con.close();
            }
            return "ERROR";
        }
    }

    public ArrayList<String> getUserAllergies(String username) throws Exception {

        Connection con = getDatabaseConnection();
        ArrayList<String> allergies = new ArrayList<>();

        try {
            String QUERY = "SELECT allergy FROM allergylist WHERE username = ?";

            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                allergies.add(rs.getString("allergy"));
            }

            rs.close(); 
            pstmt.close();
            con.close();

            if (allergies.isEmpty()) {
                return null;
            }
            return allergies;

        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            throw e;
        }
    }
    

    public ArrayList<Recipe> getUserRecipes(String username) throws Exception {
        try (Connection con = getDatabaseConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT r.recipe_id, r.recipe_name, r.recipe_instructions " +
                             "FROM recipe r JOIN userrecipe u ON r.recipe_id = u.recipe_id " +
                             "WHERE u.username = ?"
             )) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<Recipe> userRecipes = new ArrayList<>();

                try (PreparedStatement ingredientStmt =
                             con.prepareStatement("SELECT recipe_ingredient FROM ingredient WHERE recipe_id = ?")) {

                    while (rs.next()) {
                        int recipeId = rs.getInt("recipe_id");

                        Recipe recipe = new Recipe();
                        recipe.setIndex(recipeId);
                        recipe.setRecipeName(rs.getString("recipe_name"));
                        recipe.setInstructions(rs.getString("recipe_instructions"));

                        ArrayList<String> ingredients = new ArrayList<>();
                        ingredientStmt.setInt(1, recipeId);

                        try (ResultSet ingredientRs = ingredientStmt.executeQuery()) {
                            while (ingredientRs.next()) {
                                ingredients.add(ingredientRs.getString("recipe_ingredient"));
                            }
                        }

                        recipe.setIngredients(ingredients);
                        userRecipes.add(recipe);
                    }
                }

                return userRecipes.isEmpty() ? null : userRecipes;
            }
        }
    }

    public boolean addNewRecipe(Recipe newRecipe) {
        Connection con = null;

        try {
            con = getDatabaseConnection();
            con.setAutoCommit(false);

            String insertRecipeSql =
                    "INSERT INTO recipe (recipe_name, recipe_instructions) " +
                            "VALUES (?, ?) " +
                            "RETURNING recipe_id";

            PreparedStatement insertRecipeStmt = con.prepareStatement(insertRecipeSql);
            insertRecipeStmt.setString(1, newRecipe.getRecipeName());
            insertRecipeStmt.setString(2, newRecipe.getInstructions());

            ResultSet rs = insertRecipeStmt.executeQuery();
            if (!rs.next()) {
                con.rollback();
                return false;
            }
            int recipeId = rs.getInt("recipe_id");

            rs.close();
            insertRecipeStmt.close();

            newRecipe.setIndex(recipeId);

            String insertIngredientSql =
                    "INSERT INTO ingredient (recipe_id, recipe_ingredient, amount) VALUES (?, ?, ?)";

            PreparedStatement insertIngredientStmt = con.prepareStatement(insertIngredientSql);

            if (newRecipe.getIngredients() != null) {
                for (String ing : newRecipe.getIngredients()) {
                    insertIngredientStmt.setInt(1, recipeId);
                    insertIngredientStmt.setString(2, ing);
                    insertIngredientStmt.setNull(3, java.sql.Types.VARCHAR);
                    insertIngredientStmt.addBatch();
                }
                insertIngredientStmt.executeBatch();
            }

            insertIngredientStmt.close();

            String insertUserRecipeSql =
                    "INSERT INTO userrecipe (username, recipe_id) VALUES (?, ?)";

            PreparedStatement insertUserRecipeStmt = con.prepareStatement(insertUserRecipeSql);
            insertUserRecipeStmt.setString(1, username);
            insertUserRecipeStmt.setInt(2, recipeId);
            insertUserRecipeStmt.executeUpdate();
            insertUserRecipeStmt.close();

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ignored) {}
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    public Recipe getRandomRecipe(String username){
        Connection con = getDatabaseConnection();
        Recipe recipe = null;

        try{
            String QUERY = "SELECT r.recipe_id, r.recipe_name, r.recipe_instructions " +
                    "FROM recipe r " +
                    "WHERE NOT EXISTS ( " +
                    " SELECT 1 FROM ingredient i " +
                    " JOIN allergylist a ON LOWER(i.recipe_ingredient) LIKE '%' || LOWER(a.allergy) || '%' " +
                    " WHERE i.recipe_id = r.recipe_id AND a.username = ? " +
                    ") " +
                    "ORDER BY RANDOM() " +
                    "LIMIT 1";

            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                recipe = new Recipe();
                int recipeId = rs.getInt("recipe_id");
                recipe.setIndex(recipeId);
                recipe.setRecipeName(rs.getString("recipe_name"));
                recipe.setInstructions(rs.getString("recipe_instructions"));

                ArrayList<String> ingredients = new ArrayList<>();
                String ingredientQUERY = "SELECT recipe_ingredient FROM ingredient WHERE recipe_id = ? ";
                PreparedStatement ingredientStmt = con.prepareStatement(ingredientQUERY);
                ingredientStmt.setInt(1, recipeId);
                ResultSet ingredientRs = ingredientStmt.executeQuery();
                while(ingredientRs.next()){
                    ingredients.add(ingredientRs.getString("recipe_ingredient"));
                }
                ingredientRs.close();
                ingredientStmt.close();
                recipe.setIngredients(ingredients);

            }
            rs.close();
            pstmt.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
            try {
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return recipe;
    }

    public ArrayList<String> getShoppingList(String username) throws Exception {
        Connection con = getDatabaseConnection();
        ArrayList<String> ingredients = new ArrayList<>();
        try {
            String QUERY = "SELECT ingredient FROM shoppinglist WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredients.add(rs.getString("ingredient"));
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

    public void addToShoppingList(String username, String ingredient) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String CHECK = "SELECT COUNT(*) FROM shoppinglist WHERE username = ? AND ingredient = ?";
            PreparedStatement checkStmt = con.prepareStatement(CHECK);
            checkStmt.setString(1, username);
            checkStmt.setString(2, ingredient);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            checkStmt.close();

            if (count > 0) {
                con.close();
                return;
            }

            String INSERT = "INSERT INTO shoppinglist (username, ingredient) VALUES (?, ?)";
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

    public void removeFromShoppingList(String username, ArrayList<String> ingredients) throws Exception{
        if(ingredients == null || ingredients.isEmpty()){
            return;
        }

        Connection con = getDatabaseConnection();
        try{
            con.setAutoCommit(false);

            String DELETE = "DELETE FROM shoppinglist WHERE username = ? AND ingredient = ?";
            PreparedStatement pstmt = con.prepareStatement(DELETE);

            for(String ingredient : ingredients){
                pstmt.setString(1, username);
                pstmt.setString(2, ingredient);
                pstmt.addBatch();
            }
            pstmt.executeUpdate();
            pstmt.close();

            con.commit();
            con.close();
        } catch (Exception e){
            if(con != null){
                try{
                    con.rollback();
                } catch (Exception ignored){
                }
                con.close();
            }
            throw e;
        }
    }

    public void clearShoppingList(String username) throws Exception{
        Connection con = getDatabaseConnection();
        try{
            String DELETE = "DELETE FROM shoppinglist WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(DELETE);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (Exception e){
            if (con != null){
                con.close();
            } throw e;
        }
    }

    public void replaceShoppingList(String username, ArrayList<String> ingredients) throws Exception{
        Connection con = getDatabaseConnection();
        try{
            con.setAutoCommit(false);

            String DELETE = "DELETE FROM shoppinglist WHERE username = ?";
            PreparedStatement deleteStmt = con.prepareStatement(DELETE);
            deleteStmt.setString(1, username);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            if(!ingredients.isEmpty()){
                String INSERT = "INSERT INTO shoppinglist (username, ingredient) VALUES (?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(INSERT);
                for(String ingredient : ingredients){
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, ingredient);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
                insertStmt.close();
            }

            con.commit();
            con.close();
        } catch (Exception e){
            if (con != null){
                try{
                    con.rollback();
                } catch (Exception ignored){}
                con.close();
            } throw e;
        }
    }

    public void addIngredient(String username, String ingredient) throws Exception {
// fixa de ska ej va username
        Connection con = getDatabaseConnection();

        try {
            String INSERT = "INSERT INTO ownedingredient (username, ingredient) VALUES (?, ?)";

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
            String DELETE = "DELETE FROM ownedingredient WHERE username = ? AND ingredient = ?";

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
        String QUERY = "SELECT ingredient FROM ownedingredient WHERE username = ?";

        PreparedStatement pstmt = con.prepareStatement(QUERY);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            ingredients.add(rs.getString("ingredient"));
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
    public void addAllergy(String username, String allergy) throws Exception {

        Connection con = getDatabaseConnection();

        try {
            String INSERT = "INSERT INTO allergylist (username, allergy) VALUES (?, ?)";

            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, username);
            pstmt.setString(2, allergy);

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
    public void removeAllergy(String username, String allergy) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String DELETE = "DELETE FROM allergylist WHERE username = ? AND allergy = ?";
            PreparedStatement pstmt = con.prepareStatement(DELETE);
            pstmt.setString(1, username);
            pstmt.setString(2, allergy);
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
}
