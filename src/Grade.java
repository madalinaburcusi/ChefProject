import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class Grade {
    public int finalScore;
    public static HashMap<Integer, Integer> ingredientsScoreMap;

    static {
        ingredientsScoreMap = new HashMap<>();
        ingredientsScoreMap.put(1, 1);
        ingredientsScoreMap.put(2, 1);
        ingredientsScoreMap.put(3, 1);
        ingredientsScoreMap.put(4, 1);
        ingredientsScoreMap.put(5, 2);
        ingredientsScoreMap.put(6, 2);
        ingredientsScoreMap.put(7, 2);
        ingredientsScoreMap.put(8, 3);
        ingredientsScoreMap.put(9, 3);
        ingredientsScoreMap.put(10, 3);
        ingredientsScoreMap.put(11, 4);
        ingredientsScoreMap.put(12, 4);
        ingredientsScoreMap.put(13, 5);
        ingredientsScoreMap.put(14, 6);
        ingredientsScoreMap.put(15, 6);
    }

    public int calculateIngredientScore(Plate plate) {
        for (Ingredient ingredient : plate.recipe) {
            finalScore += ingredientsScoreMap.get(ingredient.ingredientCode);
        }
        return finalScore;
    }

    public void setParticipantGrade(long userID, MongoCollection<Document> userDetails, int grade) {
        userDetails.updateOne(eq("ID", userID),
                new Document("$set", new Document("ID", userID).append("GRADE", grade)));
    }

}
