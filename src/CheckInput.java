import java.util.List;
import java.util.Scanner;

public class CheckInput {
    Scanner scanner = new Scanner(System.in);
    String input;
    int number;

    public String getString (){
        System.out.print("Enter your option/code: ");
        input = scanner.nextLine();
        return input;
    }
    public int getInt (String input){
        try{
            number = Integer.parseInt(input);
        }catch (Exception e){
            getInt(getString());
        }
        return number;
    }

    public boolean isValidMenuCode (int code){
        if(code<=5)
            return true;
        return false;
    }

    public boolean isValidMenuChefCode (int code){
        if(code<=2)
            return true;
        return false;
    }

    public boolean isValidRegisterCode (int code){
        if(code<=2)
            return true;
        return false;
    }

    public boolean isValidColor(String color, List<String> colorList){
        if(colorList.contains(color.toUpperCase()))
            return true;
        return false;
    }

    public boolean numberIngredients3or4(int code){
        if(code==3 || code ==4)
            return true;
        return false;
    }
}
