import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

public class ChefsTeam {
    ObjectMapper objectMapper = new ObjectMapper();
    Random random = new Random();
    Participant participant = new Participant();

    public List<Long> getRandIDList(List<Long> teamIDs){
        List<Long> team = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            int randomIndex = random.nextInt(teamIDs.size());
            team.add(teamIDs.get(randomIndex));
            teamIDs.remove(randomIndex);
        }
        return team;
    }

    public List<Long> getChefIDs(MongoCollection<Document> userDetails){
        List<Long> chefs = new ArrayList<>();
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
                chefs.add(jsonNode.get("ID").get("$numberLong").asLong());
            }
        };
        userDetails.find(eq("TITLE", "CHEF")).forEach(printBlock);
        return chefs;
    }

    public void generateRandomTeams(MongoCollection<Document> userDetails){
        List<Long> teamIDs = new ArrayList<>();
        teamIDs.addAll(participant.participantIDs(userDetails));
        if(teamIDs.size() == 6)
        {
            List<Long> chefIDs = new ArrayList<>();
            chefIDs.addAll(getChefIDs(userDetails));
            if(chefIDs.size() == 3)
            {
                for (long chef : chefIDs) {
                    userDetails.updateOne(eq("ID", chef), new Document("$set", new Document("ID", chef).append("TEAM", getRandIDList(teamIDs))));
                }
                System.out.println("Participants have been assigned. GO check your team score!");
            }
            else
                System.out.println("Not all the Chefs are registered. Try later.");
        }
        else
            System.out.println("Not all the Participants are registered. Try later.");
        backToMainMenu(userDetails);

    }

    public void getTeamAverage(MongoCollection<Document> userDetails){
        List<Long> participantIDs = new ArrayList<>();
        participantIDs.addAll(participant.participantIDs(userDetails));

        if(participantIDs.size() == 6) {
            List<Long> chefIDs = new ArrayList<>();
            chefIDs.addAll(getChefIDs(userDetails));

            if (chefIDs.size() == 3) {
                for(long chefID: chefIDs){
                    Block<Document> printBlock = new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                            String jsonHistory = document.toJson();
                            JsonNode jsonNode = null;
                            final float[] average = {0};
                            try {
                                jsonNode = objectMapper.readTree(jsonHistory).get("TEAM");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            for (final JsonNode objNode : jsonNode) {
                                average[0] += participant.getParticipantGrade(objNode.get("$numberLong").asLong(), userDetails);
                            }
                            average[0] /= 2;
                            userDetails.updateOne(eq("ID", chefID), new Document("$set", new Document("ID", chefID).append("GRADE", average[0])));
                        }
                    };
                    userDetails.find(eq("ID", chefID)).forEach(printBlock);
                }
            }
            else
                System.out.println("Not all the Chefs or participants have been registered. Try later!");
        }
    }

    public void viewTeamScores(MongoCollection<Document> userDetails){
        for(long chefID: getChefIDs(userDetails))
        {
            JsonNode jsonNode = null;
            Document doc = userDetails.find(eq("ID", chefID)).first();
            try {
                jsonNode = objectMapper.readTree(doc.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CHEF: " + jsonNode.get("NAME").asText() + "\tAVERAGE: " + jsonNode.get("GRADE").asDouble());
        }
        backToMainMenu(userDetails);
    }
    public void backToMainMenu(MongoCollection<Document> userDetails){
        Menu menu = new Menu();
        System.out.println();
        menu.startMenu(userDetails);
    }
}
