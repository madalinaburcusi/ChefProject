import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ingredient {

    public int ingredientCode;
    public final static List<String> ingredientsList = new ArrayList<>(Arrays.asList(
            "Salt",
            "Pepper",
            "White wine",
            "Olive Oil",
            "Lemon",
            "Garlic",
            "Onion",
            "Spinach",
            "Green pea",
            "Sweet Potatoes",
            "Rice",
            "Carrots",
            "Squid",
            "Octopus",
            "Saint-Jacques"
    ));

    public Ingredient(){}

    public Ingredient(int ingredientCode) {
        this.ingredientCode = ingredientCode;
    }

    public static void showIngredients(int recipeSize) {
        int i=0;
        System.out.println("You will be able to choose only "+ recipeSize +" from the following ingredients for your plate:");
        for (String ingredient : ingredientsList) {
            System.out.println("Code " + ++i +": "+ ingredient);
        }
        System.out.println();
    }
}
