import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class UserServices {
    private static int numberOfUsers;
    public static long userID;
    private static final int maxNumberOfParticipants = 6;
    private static final int maxNumberOfChefs = 3;

    private final Scanner scanner = new Scanner(System.in);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String userType;
    private String userName;

    public long getUserID(MongoCollection<Document> userDetails) {
        userID = userDetails.count() + 1;
        return userID;
    }

    public void registerUser(MongoCollection<Document> userDetails, int userCode) {
        switch (userCode) {
            case 1: {
                userType = "Participant";
                if (!isParticipantAllowed(userType, userDetails)) {
                    System.out.println("The number of 6 participants has already been achieved. You may try next year!");
                    backToMenu(userDetails);
                } else {
                    setUserDetails(userType, userID, userDetails);
                }
                break;
            }
            case 2: {
                userType = "Chef";
                if (!isChefAllowed(userType, userDetails)) {
                    System.out.println("The number of 3 Chefs has been already achieved. You may try next year!");
                    backToMenu(userDetails);
                } else {
                    setUserDetails(userType, userID, userDetails);
                }
                break;
            }
        }
    }

    public int getUserNumber(String userType, MongoCollection<Document> userDetails) {
        numberOfUsers = 0;
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                String jsonHistory = document.toJson();
                JsonNode jsonNode = null;

                try {
                    jsonNode = objectMapper.readTree(jsonHistory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                numberOfUsers++;
            }
        };
        userDetails.find(eq("TITLE", userType.toUpperCase())).forEach(printBlock);
        return numberOfUsers;
    }

    public boolean isParticipantAllowed(String userType, MongoCollection<Document> userDetails) {
        return getUserNumber(userType, userDetails) < maxNumberOfParticipants;
    }

    public boolean isChefAllowed(String userType, MongoCollection<Document> userDetails) {
        return getUserNumber(userType, userDetails) < maxNumberOfChefs;
    }

    public void setUserDetails(String userType, long userID, MongoCollection<Document> userDetails) {
        System.out.println("Enter your details below.");
        System.out.print("Name: ");
        userName = scanner.nextLine();
        Document doc = new Document("ID", userID)
                .append("NAME", userName.toUpperCase())
                .append("TITLE", userType.toUpperCase())
                .append("GRADE", 0);
        userDetails.insertOne(doc);
        System.out.println("You have been registered to the Chef Contest!");
    }

    public void showUserList(String userType, MongoCollection<Document> userDetails) {
        if (getUserNumber(userType, userDetails) == 0)
            System.out.println("No " + userType + " are registered at the moment.\n");
        else {
            Block<Document> printBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    String jsonHistory = document.toJson();
                    JsonNode jsonNode = null;

                    try {
                        jsonNode = objectMapper.readTree(jsonHistory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String split = "            ";
                    System.out.println(jsonNode.get("ID").get("$numberLong").asText() + "\t" +
                            jsonNode.get("NAME").asText() +
                            split.substring(jsonNode.get("NAME").asText().length()));
                }
            };
            userDetails.find(eq("TITLE", userType.toUpperCase())).forEach(printBlock);
        }
        backToMenu(userDetails);
    }

    public void showResults(MongoCollection<Document> userDetails) {
        System.out.println("No.-NAME-----------GRADE");
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                String jsonHistory = document.toJson();
                JsonNode jsonNode = null;

                try {
                    jsonNode = objectMapper.readTree(jsonHistory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String split = "            ";
                System.out.println(jsonNode.get("ID").get("$numberLong").asText() + "\t" +
                        jsonNode.get("NAME").asText() +
                        split.substring(jsonNode.get("NAME").asText().length()) + "\t" +
                        jsonNode.get("GRADE"));
            }
        };
        userDetails.find(eq("TITLE", "Participant".toUpperCase())).sort(new Document("GRADE", -1)).forEach(printBlock);
        backToMenu(userDetails);
    }

    public void setRewards(MongoCollection<Document> userDetails) {
        numberOfUsers = getUserNumber("PARTICIPANT", userDetails);

        if (numberOfUsers == 6) {
            Document first = userDetails.find(eq("TITLE", "PARTICIPANT")).sort(new Document("GRADE", -1)).first();
            userDetails.updateOne(eq("ID", first.get("ID")), new Document("$set", new Document("ID", first.get("ID")).append("REWARD", "Golden Knife")));

            Object firstGrade = first.get("GRADE");

            Document second = userDetails.find(Filters.and(Filters.eq("TITLE", "PARTICIPANT"), Filters.lt("GRADE", firstGrade))).sort(new Document("GRADE", -1)).first();
            userDetails.updateOne(eq("ID", second.get("ID")), new Document("$set", new Document("ID", second.get("ID")).append("REWARD", "Apron")));
            String winnersString = first.toJson();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(winnersString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No.-NAME------------SCORE---REWARD");
            String split = "            ";
            System.out.println(jsonNode.get("ID").get("$numberLong").asText() + "\t" +
                    jsonNode.get("NAME").asText() +
                    split.substring(jsonNode.get("NAME").asText().length()) + "\t" +
                    jsonNode.get("GRADE") + "\t\t" +
                    jsonNode.get("REWARD").asText());

            winnersString = second.toJson();
            jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(winnersString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(jsonNode.get("ID").get("$numberLong").asText() + "\t" +
                    jsonNode.get("NAME").asText() +
                    split.substring(jsonNode.get("NAME").asText().length()) + "\t" +
                    jsonNode.get("GRADE") + "\t\t" +
                    jsonNode.get("REWARD").asText());
            System.out.println();
        } else {
            System.out.println("The contest is not finished yet. Return later.");
        }
        backToMenu(userDetails);
    }

    public void backToMenu(MongoCollection<Document> userDetails) {
        Menu menu = new Menu();
        System.out.println();
        menu.startMenu(userDetails);
    }
}
