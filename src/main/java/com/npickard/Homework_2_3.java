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
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

/**
 * Created by npickard on 6/7/2015.
 */
public class Homework_2_3 {

    public static void main(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("students").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("grades");
        Bson filter = new Document("type", "homework");

        List<Document> docs = mongoCollection.find(filter).into(new ArrayList<Document>());
        Map<Double, Document> m = new HashMap<Double, Document>();


        if (docs!=null && docs.size()>0) {
            for (Document doc : docs) {
                try {
                    String t = doc.getString("type");
                    if ("homework".equals(t)) {
                        //Helpers.printJson(doc);
                        //first, does it exist in the map?
                        Double key = doc.getDouble("student_id");

                        if (m.containsKey(key)){
                            Double score = doc.getDouble("score");
                            Double savedScore = m.get(key).getDouble("score");
                            if (score < savedScore){
                                m.put(key, doc);
                            }
                        }else{
                            //add it
                            m.put(key, doc);
                        }
                    } else {
                        System.out.println("Grade type is not 'homework'");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
           }
        }

        if (m!=null && m.keySet()!=null){
            System.out.println("The map has a keyset size of " + m.keySet().size());

            //now we can delete each entry
            Set<Double> keys = m.keySet();
            for (Double key : keys){
                Document d = m.get(key);
                //we can now delete the document
                System.out.println("We will delete this document:");
                Helpers.printJson(d);
                mongoCollection.deleteOne(d);
            }
        }else{
            System.out.println("The map is null!!!!");
        }

    }



    private void removeLowest(){
            MongoClient client = new MongoClient();
            MongoDatabase numbersDB = client.getDatabase("students");
            MongoCollection<Document> grades = numbersDB.getCollection("grades");

            MongoCursor<Document> cursor = grades.find(eq("type", "homework"))
                    .sort(ascending("student_id", "score")).iterator();

            Object studentId = -1;
            try {
                while (cursor.hasNext()) {
                    Document entry = cursor.next();
                    if (!entry.get("student_id").equals(studentId)) {
                        System.out.println("Removing: " + entry);
                        Object id = entry.get("_id");
                        grades.deleteOne(eq("_id", id));

                    }
                    studentId = entry.get("student_id");
                }
            } finally {
                cursor.close();
            }
        }
}
