import java.util.List;
import java.util.Scanner;

public class CheckInput {
    private final Scanner scanner = new Scanner(System.in);
    private String input;
    private int number;

    public String getString() {
        System.out.print("Enter a (valid) option/code: ");
        input = scanner.nextLine();
        return input;
    }

    public int getInt(String input) {
        try {
            number = Integer.parseInt(input);
        } catch (Exception e) {
            getInt(getString());
        }
        return number;
    }

    public boolean isValidMenuCode(int code) {
        return code <= 7 && code >= 1;
    }

    public boolean isValidMenuChefCode(int code) {
        return code <= 2 && code >= 1;
    }

    public boolean isValidRegisterCode(int code) {
        return code <= 2 && code >= 1;
    }

    public boolean isValidColor(String color, List<String> colorList) {
        return colorList.contains(color.toUpperCase());
    }

    public boolean numberIngredients3or4(int code) {
        return code == 3 || code == 4;
    }

    public boolean ingredientCode1to15(int code) {
        return code >= 1 && code <= 15;
    }
}
