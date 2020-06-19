import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Participant {
    Plate plate = new Plate();
    Grade grade = new Grade();
    public static String userType = "PARTICIPANT";
    ObjectMapper objectMapper = new ObjectMapper();

    public void startContest(long userID, MongoCollection<Document> userDetails){

            int recipeSize = plate.getRecipeSize();

            //Show ingredients list
            Ingredient i = new Ingredient();
            i.showIngredients(recipeSize);

            int gradeParticipant = grade.calculateIngredientScore(plate.makeYourPlate(recipeSize));
            System.out.println("Your score in the competition is: " + gradeParticipant);
            grade.setParticipantGrade(userID,userDetails,gradeParticipant);

            backToMenu(userDetails);
    }

    public void backToMenu(MongoCollection<Document> userDetails){
        Menu menu = new Menu();
        System.out.println();
        menu.startMenu(userDetails);
    }

    public List<Long> participantIDs (MongoCollection<Document> userDetails){
        List<Long> listIDs = new ArrayList<>();
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
                listIDs.add(jsonNode.get("ID").get("$numberLong").asLong());
            }
        };
        userDetails.find(eq("TITLE", userType.toUpperCase())).forEach(printBlock);
        return listIDs;
    }

    public int getParticipantGrade(long id, MongoCollection<Document> userDetails){
        Document doc = userDetails.find(eq("ID", id)).first();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(doc.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonNode.get("GRADE").asInt();
    }
}
