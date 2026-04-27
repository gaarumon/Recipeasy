package Model;

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

    /**
     * Adding a Test-recipy for development.
     * The recipy contains name, instructions and ingredients.
     * @author Michael
     */
   /* public void addTestRecipe() throws Exception {
        Connection conn = getDatabaseConnection();
        try {
            String checkSQL = "SELECT COUNT (*) FROM recipe WHERE recipe_name = ?";
            PreparedStatement checkSTMT = conn.prepareStatement(checkSQL);
            checkSTMT.setString(1, "Bibimpap");
            ResultSet checkRs = checkSTMT.executeQuery();
            checkRs.next();
            int count = checkRs.getInt(1);
            checkRs.close();
            checkSTMT.close();

            if(count > 0) {
                System.out.println("Test recept (Bibimbap) finns redan");
                conn.close();
                return;
            }

            //Lägg till ett recept
            int recipeId = 4;
            String recipeSQL = "INSERT INTO recipe (recipe_id, recipe_name, recipe_instructions) VALUES (?, ?, ?) ";
            PreparedStatement recipeSTMT = conn.prepareStatement(recipeSQL, Statement.RETURN_GENERATED_KEYS);
            recipeSTMT.setInt(1, recipeId);
            recipeSTMT.setString(2, "Bibimbap");
            recipeSTMT.setString(3,
                    "1. Skölj av riset noggrant och koka upp enligt paketet. \n" +"" +
                       "2. Förbered grönsaker genom att skära i strimlor- morötter, spenat, svamp, courgette. \n" +
                       "3. Marinera köttet i sojan, socker, sesamolja, vitlök och ingefära. \n" +
                       "4. Stek det marinerade köttet. \n" +
                       "5. Lägg riset i en skål, toppa med grönsaker och kött. \n" +
                       "6. Lägg på en klick Gochujang och blanda alla ingredienser väl.");
            recipeSTMT.executeUpdate();
            recipeSTMT.close();

            String ingredientsSQL = "INSERT INTO ingredient(recipe_id, recipe_ingredient) VALUES (?, ?) ";
            PreparedStatement ingredientStmt = conn.prepareStatement(ingredientsSQL);
            String[] ingredients = {
                    "2 koppar ris",
                    "300g nötkött",
                    "3 msk Koreansk sojasås",
                    "1 msk socker",
                    "1 msk sesamolja",
                    "3 vitlöksklyftor",
                    "1 tsk ingefära",
                    "morötter",
                    "spenat",
                    "champinjoner",
                    "courgette",
                    "gochujang"
            };
            for(String ingredient : ingredients) {
                ingredientStmt.setInt(1, recipeId);
                ingredientStmt.setString(2, ingredient);
                ingredientStmt.addBatch();
            }
            ingredientStmt.executeBatch();
            ingredientStmt.close();
            conn.close();
            System.out.println("Test recept tillagt!" + recipeId);
        } catch(Exception e) {
            if(conn != null) {
                conn.close();
            }
            System.out.println("Fel vid addTestRecipe: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

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
}
