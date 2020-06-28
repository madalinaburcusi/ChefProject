import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Plate {
    public List<Ingredient> recipe = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    CheckInput check = new CheckInput();

    public Plate() {

    }

    public Plate(List<Ingredient> list) {
        this.recipe = new ArrayList<>(list);
    }

    public void addIngredientToRecipe(Ingredient ingredient) {
        recipe.add(ingredient);
    }

    public Plate makeYourPlate(int recipeSize) {
        int ingredientCode;
        while (recipe.size() < recipeSize) {
            System.out.print("Ingredient no. " + (recipe.size() + 1) + ": ");
            ingredientCode = check.getInt(scanner.next());
            while (!check.ingredientCode1to15(ingredientCode)) {
                System.out.print("Ingredient no. " + (recipe.size() + 1) + ": ");
                ingredientCode = check.getInt(scanner.next());
            }
            addIngredientToRecipe(new Ingredient(ingredientCode));
        }
        return new Plate(recipe);
    }

    public int getRecipeSize() {
        int recipeSize;
        System.out.print("How many ingredients will you use for your recipe. Type 3 or 4.\n");
        recipeSize = check.getInt(check.getString());

        while (!check.numberIngredients3or4(recipeSize))
            recipeSize = check.getInt(check.getString());

        return recipeSize;
    }

}
