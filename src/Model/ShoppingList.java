package Model;

import java.util.ArrayList;
import java.util.Collection;

public class ShoppingList {

    private final ArrayList<String> ingredients = new ArrayList<>();

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

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

    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    public void clear() {
        ingredients.clear();
    }

    public ArrayList<String> addMissingIngredientsFromRecipe(Recipe recipe, Collection<String> ownedIngredients) {
        ArrayList<String> added = new ArrayList<>();
        if (recipe == null || recipe.getIngredients() == null) {
            return added;
        }
        for (String recipeIngredient : recipe.getIngredients()) {
            if (containsIgnoreCase(ownedIngredients, recipeIngredient)) {
                continue; // user already has it at home
            }
            if (addIngredient(recipeIngredient)) {
                added.add(recipeIngredient);
            }
        }
        return added;
    }

    public boolean contains(String ingredient) {
        return containsIgnoreCase(ingredients, ingredient);
    }

    public int size() {
        return ingredients.size();
    }

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
}
