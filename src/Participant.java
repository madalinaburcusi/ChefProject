import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Participant {
    Plate plate = new Plate();
    Grade grade = new Grade();

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

}
