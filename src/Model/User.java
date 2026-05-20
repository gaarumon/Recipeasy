package Model;

import java.util.ArrayList;

/**
 * user class: its only responsibility is storing user data so it doesn't have to get
 * fetched from the database every time. only includes getters/setters, add and remove methods.
 * @author Kotryna
 */
public class User {
    private String username;
    private ArrayList<String> allergyList = new ArrayList<>();
    private ArrayList<String> ingredientList = new ArrayList<>();
    private ArrayList<Recipe> favouriteRecipes = new ArrayList<>();
    private ArrayList<Recipe> userRecipes = new ArrayList<>();
    private Recipe currentRandomRecipe;
    private Recipe nextRandomRecipe;
    private ArrayList<Recipe> ingredientBasedRecipes = new ArrayList<>();

    public User (String username) {
        this.username = username;
    }

    public void addAllergy(String allergy) {
        allergyList.add(allergy);
    }

    public void removeAllergy(String allergy) {
        allergyList.remove(allergy);
    }

    public void addIngredient(String ingredient){
        ingredientList.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        ingredientList.remove(ingredient);
    }

    public void addFavourite(Recipe favourite) {
        favouriteRecipes.add(favourite);
    }

    public void removeFavourite(Recipe favourite) {
        favouriteRecipes.remove(favourite);
    }

    public void removeUserRecipe(Recipe recipe) {
        userRecipes.remove(recipe);
    }

    public ArrayList<String> getAllergyList() {
        return allergyList;
    }

    public void setAllergyList(ArrayList<String> allergyList) {
        this.allergyList = allergyList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public ArrayList<Recipe> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public void setFavouriteRecipes(ArrayList<Recipe> favouriteRecipes) {
        this.favouriteRecipes = favouriteRecipes;
    }

    public ArrayList<Recipe> getUserRecipes() {
        return userRecipes;
    }

    public void setUserRecipes(ArrayList<Recipe> userRecipes) {
        this.userRecipes = userRecipes;
    }

    public Recipe getCurrentRandomRecipe() {
        return currentRandomRecipe;
    }

    public void setCurrentRandomRecipe(Recipe currentRandomRecipe) {
        this.currentRandomRecipe = currentRandomRecipe;
    }

    public Recipe getNextRandomRecipe() {
        return nextRandomRecipe;
    }

    public void setNextRandomRecipe(Recipe nextRandomRecipe) {
        this.nextRandomRecipe = nextRandomRecipe;
    }

    public void setIngredientBasedRecipes(ArrayList<Recipe> filteredRecipes){
        this.ingredientBasedRecipes = filteredRecipes;
    }

    public ArrayList<Recipe> getIngredientBasedRecipes(){
        return this.ingredientBasedRecipes;
    }
}
