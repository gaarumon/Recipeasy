package Model;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Recipe {

    private ArrayList <String> ingredients = new ArrayList<>();
    private String instructions;
    private ArrayList <String> theme = new ArrayList<>();
    private int index;
    private String recipeName;
    private Image recipeImage;


    /**
     * method that is called when getting image from database. if there is no
     * image, it sets a default image
     * @param url
     * @author Kotryna
     */
    public void setImage(String url) {
    if(url == null) {
        recipeImage = new Image("https://vvmyytosimklcpkmnrzx.supabase.co/storage/v1/object/public/Recipe%20images/00NoImageError.png");
    } else{
        recipeImage = new Image(url);
        }
    }

    public Image getRecipeImage(){
        return recipeImage;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public ArrayList<String> getTheme() {
        return theme;
    }

    public void setTheme(ArrayList<String> theme) {
        this.theme = theme;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public String toString() {
        return getRecipeName();
    }
}
