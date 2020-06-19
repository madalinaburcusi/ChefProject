import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    CheckInput check = new CheckInput();
    UserServices user = new UserServices();
    Participant participant = new Participant();
    Chef chef = new Chef();
    ChefsTeam team = new ChefsTeam();
    int menuCode;
    int userCode;
    long userID;

    public void startMenu(MongoCollection<Document> userDetails){

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

        menuCode = check.getInt(check.getString());

       try{
           while(!check.isValidMenuCode(menuCode)) {
               throw new MenuCodeisnotValidException("Your code is not in the menu list!");
           }
       }catch (MenuCodeisnotValidException e){
           System.out.println(e.getMessage());
           menuCode = check.getInt(check.getString());
       }

            switch (menuCode){
                case 0 : {
                    //Choose if user will be Participant or Chef
                    userCode = getUserCode(userDetails);
                    userID = user.getUserID(userDetails);

                    user.registerUser(userDetails,userCode);
                    if(userCode == 1)
                        participant.startContest(userID,userDetails);
                    else
                        chef.startMenu(userID,userDetails);

                    System.out.println("Thank you for participating!");
                    break;
                }
                case 1 : {
                    user.showUserList("Participant".toUpperCase(),userDetails);
                    break;
                }

                case 2 : {
                    user.showUserList("Chef".toUpperCase(),userDetails);
                    break;
                }

                case 3 : {
                    user.showResults(userDetails);
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
                case 6 : {
                    user.setRewords(userDetails);
                    break;
                }

                case 7 : {
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

        userCode = check.getInt(check.getString());

        while(!check.isValidRegisterCode(userCode))
            userCode = check.getInt(check.getString());

        if(userCode == 0 )
            startMenu(userDetails);

        return userCode;
    }
}
