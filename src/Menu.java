import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    enum Codes {
        Participant(1),
        Chef(2);

        int i;
        Codes(int i) {
            this.i = i;
        }
    }
    private static final int PARTICIPANT_CODE = 1;
    private static final int CHEF_CODE = 2;

    private final CheckInput checkInput = new CheckInput();
    private final UserServices userService = new UserServices();
    private final Participant participant = new Participant();
    private final Chef chef = new Chef();
    private final ChefsTeam team = new ChefsTeam();
    private int menuCode;
    private int userCode;
    private long userID;

    public void startMenu(MongoCollection<Document> userDetails) {

        List<String> menu = new ArrayList<>();
        System.out.println("------- MENU -------");
        menu.add("Code 0: Register");
        menu.add("Code 1: View Participant's List");
        menu.add("Code 2: View Chef's List");
        menu.add("Code 3: View Participant's results");
        menu.add("Code 4: Generate Random Teams");
        menu.add("Code 5: View Team Averages");
        menu.add("Code 6: Rewards");
        menu.add("Code 7: Exit\n");

        for (String code : menu) {
            System.out.println(code);
        }

        menuCode = checkInput.getInt(checkInput.getString());

        try {
            if (!checkInput.isValidMenuCode(menuCode)) {
                throw new MenuCodeisnotValidException("Your code is not in the menu list!");
                //Redesign the method to  be called again when an exception is thrown
            }
        } catch (MenuCodeisnotValidException e) {
            System.out.println(e.getMessage());
            menuCode = checkInput.getInt(checkInput.getString());
        }

        switch (menuCode) {
            case 0: {
                //Choose if user will be Participant or Chef
                userCode = getUserCode(userDetails);
                userID = userService.getUserID(userDetails);

                userService.registerUser(userDetails, userCode);
                if (userCode == Codes.Participant.i)
                    participant.startContest(userID, userDetails);
                else
                    chef.startMenu(userID, userDetails);

                System.out.println("Thank you for participating!");
                break;
            }
            case 1: {
                userService.showUserList("Participant".toUpperCase(), userDetails);
                break;
            }

            case 2: {
                userService.showUserList("Chef".toUpperCase(), userDetails);
                break;
            }

            case 3: {
                userService.showResults(userDetails);
                break;
            }

            case 4: {
                team.generateRandomTeams(userDetails);
                break;
            }

            case 5: {
                team.getTeamAverage(userDetails);
                team.viewTeamScores(userDetails);
                break;
            }
            case 6: {
                userService.setRewards(userDetails);
                break;
            }

            case 7: {
                System.out.println("Session ended!");
                System.exit(0);
            }
        }
    }


    public int getUserCode(MongoCollection<Document> userDetails) {
        int userCode;
        System.out.println("Choose the code that fits your position in the competition:");
        List<String> userScope = new ArrayList<>();
        userScope.add("Participant      Code: 1");
        userScope.add("Chef             Code: 2");
        userScope.add("Back to menu     Code: 0\n");

        for (String code : userScope) {
            System.out.println(code);
        }

        userCode = checkInput.getInt(checkInput.getString());

        while (!checkInput.isValidRegisterCode(userCode))
            userCode = checkInput.getInt(checkInput.getString());

        if (userCode == 0)
            startMenu(userDetails);

        return userCode;
    }
}
