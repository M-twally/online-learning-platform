//package DB_Connection;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//
//public class DB {
//    private static  MongoClient client;
//    private static  MongoDatabase db;
//    public static void initializeDatabaseConnection() {
//        client = MongoClients.create("mongodb://localhost:27017");
//        // Access the database
//        db = client.getDatabase("Mono");
//    }
//
//    public static MongoCollection<Document> getEnrollmentsCollection(){
//        return db.getCollection("enrollments");
//    }
//
//
//    public static void closeDatabaseConnection() {
//        if (client != null) {
//            client.close();
//        }
//    }
//}


package DB_Connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

@Singleton
public class DB {
    private static DB instance;
    private static MongoClient client;
    private static MongoDatabase db;

    private DB() {
    }

    public static DB getInstance() {
        if (instance == null) {
            instance = new DB();
            initializeDatabaseConnection();
        }
        return instance;
    }

    public static void initializeDatabaseConnection() {
        client = MongoClients.create("mongodb://localhost:27017");
        // Access the database
        db = client.getDatabase("Mono");
    }

    public MongoCollection<Document> getEnrollmentsCollection() {
        return db.getCollection("enrollments");
    }

    public void closeDatabaseConnection() {
        if (client != null) {
            client.close();
        }
    }
}

