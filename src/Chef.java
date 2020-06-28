import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Chef {
    public String color;
    private int menuCode;
    CheckInput check = new CheckInput();
    Scanner scanner = new Scanner(System.in);
    public static List<String> colorList = new ArrayList<>(Arrays.asList("RED", "YELLOW", "GREEN"));

    public void startMenu(long userID, MongoCollection<Document> userDetails) {

        List<String> menu = new ArrayList<>();
        menu.add("Code 1: Choose your color");
        menu.add("Code 2: Back to main page");

        for (String code : menu) {
            System.out.println(code);
        }
        menuCode = check.getInt(check.getString());

        try {
            if (!check.isValidMenuChefCode(menuCode)) {
                throw new MenuCodeisnotValidException("Your code is not in the menu list!");
            }
        } catch (MenuCodeisnotValidException e) {
            System.out.println(e.getMessage());
            menuCode = check.getInt(check.getString());
        }

        switch (menuCode) {
            case 1: {
                System.out.println("Choose from the following colors: " + "RED" + ", " + "YELLOW" + ", " + "GREEN");
                while (!check.isValidColor(getColor(), colorList)) {
                    System.out.println("Choose a valid color.");
                }

                setColor(userID, userDetails, color);
                backToMenu(userID, userDetails);
                break;
            }

            case 2: {
                backToMainMenu(userDetails);
                break;
            }
        }
    }

    public void setColor(long userID, MongoCollection<Document> userDetails, String color) {
        userDetails.updateOne(eq("ID", userID),
                new Document("$set", new Document("ID", userID).append("COLOR", color)));
    }

    public void backToMenu(long userID, MongoCollection<Document> userDetails) {
        startMenu(userID, userDetails);
    }

    public void backToMainMenu(MongoCollection<Document> userDetails) {
        Menu menu = new Menu();
        System.out.println();
        menu.startMenu(userDetails);
    }

    public String getColor() {
        System.out.print("Type your color: ");
        color = scanner.next();
        return color;
    }


}
