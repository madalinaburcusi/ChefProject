import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ContestManager {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("CHEFdb");
        MongoCollection<Document> userDetails = database.getCollection("userDetails");

        System.out.println("Welcome to the Chef Show!\n");
        UserServices user = new UserServices();

        Menu menu = new Menu();

        menu.startMenu(userDetails);

    }

}
