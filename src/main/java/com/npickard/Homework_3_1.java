package com.npickard;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

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
        MongoDatabase mongoDatabase = mongoClient.getDatabase("school"); //.withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("students");
//        //find the data where name is "Verdell Sowinski"
//        Bson queryFilter = eq("name","Verdell Sowinski");
//        List<Document> docs = mongoCollection.find(queryFilter).into(new ArrayList<Document>());

//        //.append is like an AND  e.g. find all data where name is Verdell Sowinski AND a score they have is less than 25
//        Bson queryFilter = new Document("name", "Verdell Sowinski").append("scores.score", new Document("$lt",25));
//        List<Document> docs = mongoCollection.find(queryFilter).into(new ArrayList<Document>());

        //.append is like an AND  e.g. find all data where name is Verdell Sowinski AND a score they have is less than 25
        Bson sortFilter = new Document("name", -1);
        List<Document> students = mongoCollection.find().sort(sortFilter).into(new ArrayList<Document>());

        if (students != null && students.size() > 0) {

            Iterator<Document> it = students.iterator();

            while(it.hasNext()){
                Document studentInfo = it.next();

                //Helpers.printJson(doc);
                Double lowestScore = Double.MAX_VALUE;
                Document lowestScoreInfo = null;

                List<Document> scores = (List<Document>)studentInfo.get("scores");
                if (scores!=null){
                    for (Document score : scores){
                        if ("homework".equalsIgnoreCase(score.getString("type"))){
                            //System.out.println("\tType: " + score.getString("type"));
                            Double scoreValue = score.getDouble("score");

                            //System.out.println("\tScore: " + scoreValue);
                            if (scoreValue!=null){
                                if (lowestScore.doubleValue()> scoreValue.doubleValue()) {
                                    lowestScore = scoreValue;
                                    lowestScoreInfo = score;
                                }
                            }
                        }
                    }

                    if (lowestScoreInfo!=null) {
                        //delete(mongoCollection, studentInfo.getObjectId("_id"), lowestScoreInfo.getObjectId("score"));
                        delete(mongoCollection, studentInfo, lowestScoreInfo);
                    }
                }

                Helpers.printJson(studentInfo);
            }
        }
        System.out.println("\n\nCount: " + students.size());
    }

    private static void delete(MongoCollection<Document> collection, Document studentInfo, Document scoreInfo ) {

        //Helpers.printJson(studentInfo);
        BasicDBObject match = new BasicDBObject("_id", studentInfo.getDouble("_id")); //to match your direct app document
        //Helpers.printJson(studentInfo);
        BasicDBObject update = new BasicDBObject("scores", scoreInfo);
        //Helpers.printJson(update);
        collection.updateOne(match, new BasicDBObject("$pull", update));
    }












//
//    private static void delete(MongoCollection<Document> collection, Document studentInfo, Document scoreInfo ) {
//
//        Document match = studentInfo; //new BasicDBObject();
//        //match.put( "_id", studentOid );
//        Document scoreSpec = scoreInfo;
////        BasicDBObject scoreSpec = new BasicDBObject();
////        scoreSpec.put( "score", scoreOid );
////
//        BasicDBObject update = new BasicDBObject();
//        update.put( "$pull", new BasicDBObject( "scores", scoreSpec ) );
//        collection.updateOne( match, update );
//    }


//    private static void delete(MongoCollection<Document> collection, Document studentInfo, Document scoreInfo ) {
//        Document match = new Document();
//        match.put( "_id", studentInfo.getObjectId("_id"));
//        Document scoreSpec = new Document();
//        scoreSpec.put("score", 5.0);
//        Document update = new Document();
//        update.put( "$pull", new Document( "addresses", scoreSpec ) );
//        collection.updateOne(match, update);
//
//        Bson filter =
//        UpdateResult result = collection.updateOne()
//    }

//    private static void delete(MongoCollection<Document> collection, ObjectId studentoid, ObjectId scoreoid ) {
//        BasicDBObject match = new BasicDBObject();
//        match.put( "_id", studentoid );
//        BasicDBObject scoreSpec = new BasicDBObject();
//        scoreSpec.put( "_id", scoreoid );
//        BasicDBObject update = new BasicDBObject();
//        update.put( "$pull", new BasicDBObject( "addresses", scoreSpec ) );
//        collection.updateOne(match, update);
//    }


//    private static void delete( MongoCollection<Document> collection, ObjectId accountoid, ObjectId addressoid ){
//        BasicDBObject match = new BasicDBObject();
//        match.put( "_id", accountoid );
//        BasicDBObject addressSpec = new BasicDBObject();
//        addressSpec.put( "_id", addressoid );
//        BasicDBObject update = new BasicDBObject();
//        update.put( "$pull", new BasicDBObject( "addresses", addressSpec ) );
//        collection.updateOne(match, update);
//    }









    public static void main2(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("school").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("students");

//        //find the data where name is "Verdell Sowinski"
//        Bson queryFilter = eq("name","Verdell Sowinski");
//        List<Document> docs = mongoCollection.find(queryFilter).into(new ArrayList<Document>());

//        //.append is like an AND  e.g. find all data where name is Verdell Sowinski AND a score they have is less than 25
//        Bson queryFilter = new Document("name", "Verdell Sowinski").append("scores.score", new Document("$lt",25));
//        List<Document> docs = mongoCollection.find(queryFilter).into(new ArrayList<Document>());

        //.append is like an AND  e.g. find all data where name is Verdell Sowinski AND a score they have is less than 25
        Bson sortFilter = new Document("name", -1);
        List<Document> docs = mongoCollection.find().sort(sortFilter).into(new ArrayList<Document>());
        Map<Double, Document>  users = new HashMap<Double, Document>();


        if (docs != null && docs.size() > 0) {

            int maxCount = 100;
            int count = 0;

            for (Document doc : docs) {
                Document retrievedDocument = null;

                if (count >= maxCount){
                    break;
                }

                Helpers.printJson(doc);
                Double id = doc.getDouble("_id");
                if (!users.keySet().contains(id)){
                    users.put(id, doc);
                }else{
                    retrievedDocument = users.get(id);
                }

                if (retrievedDocument!=null){
                    //is this document the lowest score?
                    Double retrievedValue = getValue("homework", "score", (List<Document>) retrievedDocument.get("scores"));
                    Double currentValue = getValue("homework", "score", (List<Document>) doc.get("scores"));
                    if (retrievedValue!=null && currentValue!=null){
                        if (retrievedValue.doubleValue()> currentValue.doubleValue()){
                            //replace in retrieved values
                            users.put(id, doc);
                        }
                    }

                    if (retrievedValue==null && currentValue!=null){
                        users.put(id, doc);
                    }
                }
                count++;
            }
        }

        //
        if (users.size()>0){
            for (Double id : users.keySet()){
                Document doc = users.get(id);
                if (doc!=null){
                    Helpers.printJson(doc);
                }

            }
        }


        System.out.println("\n\nCount: " + docs.size());
    }

    private static Double getValue(String filter, String key, List<Document> documents){
        Double val = null;

        if (documents!=null){
            for (Document document : documents){
                if (document.containsKey(filter)) {
                    try{
                        val = document.getDouble(key);
                    } catch(Exception e){
                    }
                }
            }
        }
        return val;
    }

}


//                try {
//                    System.out.println("_id: " + doc.getDouble("_id"));
//                    System.out.println("Name: " + doc.getString("name"));
//                    List<Document> scores = (List<Document>)doc.get("scores");
//                    if (scores!=null){
//                        for (Document score : scores){
//                            System.out.println("\tScore: " + score.getDouble("score"));
//                            System.out.println("\tType: " + score.getString("type"));
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                }

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
