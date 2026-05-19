package Model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Keeps track of the ingredients in the users shopping list.
 * this class also handles small list rules, like avoiding duplicates
 * and skipping ingredients the user already had at home.
 * @author Huu Trung Nguyen
 */
public class ShoppingList {

    private final ArrayList<String> ingredients = new ArrayList<>();

    /**
     * gives access to the current shopping list.
     *
     * @return the ingredients currently in the shopping list
     */
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    /**
     * adds an ingredient if it is not empty and is not already in the list.
     *
     * @param ingredient the ingredient to add
     * @return true if the ingredient was added, false otherwise
     */
    public boolean addIngredient(String ingredient) {
        if (ingredient == null || ingredient.isBlank()) {
            return false;
        }
        if (contains(ingredient)) {
            return false;
        }
        ingredients.add(ingredient.trim());
        return true;
    }

    /**
     * removes several ingredients from the list.
     *
     * @param ingredientsToRemove the ingredients to remove
     */
    public void removeIngredients(Collection<String> ingredientsToRemove) {
        if(ingredientsToRemove == null){
            return;
        }

        ingredients.removeAll(ingredientsToRemove);
    }

    /**
     * clears the whole shopping list
     */
    public void clear() {
        ingredients.clear();
    }

    /**
     * adds the recipe ingredients that the uer not already have.
     * keeps the existing shopping list and skips duplicates
     *
     * @param recipe the recipe to check ingredients from
     * @param ownedIngredients the ingredients the user already has at home
     * @return the ingredients that were added to the shopping list
     */
    public ArrayList<String> addMissingIngredientsFromRecipe(Recipe recipe, Collection<String> ownedIngredients) {
        ArrayList<String> added = new ArrayList<>();
        if (recipe == null || recipe.getIngredients() == null) {
            return added;
        }
        for (String recipeIngredient : recipe.getIngredients()) {
            if (isOwned(ownedIngredients, recipeIngredient)) {
                continue; // user already has it at home
            }
            if (addIngredient(recipeIngredient)) {
                added.add(recipeIngredient);
            }
        }
        return added;
    }

    /**
     * checks if an ingredient is already in the shopping list
     * @param ingredient the ingredient to check
     * @return true if the ingredient is already in the list, false otherwise
     */
    public boolean contains(String ingredient) {
        return containsIgnoreCase(ingredients, ingredient);
    }

    /**
     * returns how many ingredients are currently in the list
     *
     * @return the number of ingredients in the shopping list
     */
    public int size() {
        return ingredients.size();
    }

    /**
     * compares text without caring about uppercase/lowercase.
     *
     * @param list the list to search in
     * @param value the value to look for
     * @return true if hte value exists in the list, false otherwise
     */
    private boolean containsIgnoreCase(Collection<String> list, String value) {
        if (value == null || list == null) {
            return false;
        }
        String target = value.trim();
        for (String item : list) {
            if (item != null && item.trim().equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if a recipe ingredient seems to match something the user owns.
     * for example, "onion" can match "1 chopped onion".
     *
     * @param ownedIngredients the ingredients the user already has at home
     * @param recipeIngredient the recipe ingredient to check
     * @return true if the user already owns the ingredient, false otherwise
     */
    private boolean isOwned(Collection<String> ownedIngredients, String recipeIngredient){
        if(recipeIngredient == null || ownedIngredients == null){
            return false;
        }
        String target = recipeIngredient.toLowerCase();
        for(String owned : ownedIngredients){
            if(owned != null && target.contains(owned.trim().toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
