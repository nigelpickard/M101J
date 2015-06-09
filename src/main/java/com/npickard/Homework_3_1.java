package com.npickard;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static javax.management.Query.lt;

/**
 * Created by npickard on 6/7/2015.
 */
public class Homework_3_1 {

    public static void main(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("school").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("students");

        Bson filter = eq("scores.type", "homework");
        Bson sortFilter = new Document("name", -1);

        List<Document> docs = mongoCollection.find(filter).sort(sortFilter).into(new ArrayList<Document>());

        if (docs != null && docs.size() > 0) {

            int maxCount = 100;
            int count = 0;

            for (Document doc : docs) {

                if (count >= maxCount){
                    break;
                }

                try {
                    System.out.println("_id: " + doc.getDouble("_id"));
                    System.out.println("Name: " + doc.getString("name"));

                    List<Document> scores = (List<Document>)doc.get("scores");
                    if (scores!=null){
                        for (Document score : scores){
                            System.out.println("\tScore: " + score.getDouble("score"));
                            System.out.println("\tType: " + score.getString("type"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
                count++;
            }
        }

    }
}


//    private void removeLowest(){
//            MongoClient client = new MongoClient();
//            MongoDatabase numbersDB = client.getDatabase("school");
//            MongoCollection<Document> grades = numbersDB.getCollection("students");
//
//            MongoCursor<Document> cursor = grades.find(eq("type", "homework"))
//                    .sort(ascending("student_id", "score")).iterator();
//
//            Object studentId = -1;
//            try {
//                while (cursor.hasNext()) {
//                    Document entry = cursor.next();
//                    if (!entry.get("student_id").equals(studentId)) {
//                        System.out.println("Removing: " + entry);
//                        Object id = entry.get("_id");
//                        grades.deleteOne(eq("_id", id));
//
//                    }
//                    studentId = entry.get("student_id");
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//}
