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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by npickard on 6/5/2015.
 */
public class MongoDBDriverFind {
    public static void main(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("inserttest");
        mongoCollection.drop();

        //insert some docs
        mongoCollection.insertOne(new Document("name", "Brown").append("age", 25).append("profession", "student"));
        mongoCollection.insertOne(new Document("name", "Smith").append("age", 35).append("profession", "teacher"));
        mongoCollection.insertOne(new Document("name", "Jackson").append("age", 40).append("profession", "facilitator"));
        mongoCollection.insertOne(new Document("name", "Johnson").append("age", 55).append("profession", "janitor"));
        mongoCollection.insertOne(new Document("name", "Walsh").append("age", 88).append("profession", "retired"));
        mongoCollection.insertOne(new Document("name", "James").append("age", 9).append("profession", "student"));
        mongoCollection.insertOne(new Document("name", "Reilly").append("age", 12).append("profession", "student"));
        mongoCollection.insertOne(new Document("name", "Stevens").append("age", 15).append("profession", "student"));
        mongoCollection.insertOne(new Document("name", "Hornsby").append("age", 18).append("profession", "student"));
        mongoCollection.insertOne(new Document("name", "King").append("age", 29).append("profession", "facilitator"));
        mongoCollection.insertOne(new Document("name", "Turner").append("age", 41).append("profession", "retired"));
        mongoCollection.insertOne(new Document("name", "Kennedy").append("age", 15).append("profession", "student"));


        //runFind(mongoCollection);
        runFind_With_SimpleFilter(mongoCollection);


    }

     private static void runFind(MongoCollection<Document> mongoCollection){


        System.out.print("Find one:");
        Document first = mongoCollection.find().first();
        Helpers.printJson(first);

        System.out.print("Find all:");
        List<Document> docs = mongoCollection.find().into(new ArrayList<Document>());
        for (Document doc : docs){
            Helpers.printJson(doc);
        }
    }

    private static void runFind_With_SimpleFilter(MongoCollection<Document> mongoCollection) {



        System.out.println("\n\n\nFind with simple one condition filter:");
        Bson filter = new Document("profession","student");
        List<Document> docs = mongoCollection.find(filter).into(new ArrayList<Document>());
        for (Document doc : docs) {
            Helpers.printJson(doc);
        }

        System.out.println("\n\n\nFind with simple multiple condition filter of student  > 20 years old:");
        filter = new Document("profession","student").append("age", new Document("$gt", 20));
        List<Document> docs2 = mongoCollection.find(filter).into(new ArrayList<Document>());
        for (Document doc : docs2) {
            Helpers.printJson(doc);
        }

        System.out.println("\n\n\nFind with simple multiple condition filter of student and 15 years old:");
        filter = new Document("profession","student").append("age", 15);
        List<Document> docs3 = mongoCollection.find(filter).into(new ArrayList<Document>());
        for (Document doc : docs3) {
            Helpers.printJson(doc);
        }

        System.out.println("\n\n\nFind with simple multiple condition filter of student and 15 years old and name contains 'dy':");
        filter = new Document("profession","student").append("age", 15).append("name", new Document("$regex","dy"));
        List<Document> docs4 = mongoCollection.find(filter).into(new ArrayList<Document>());
        for (Document doc : docs4) {
            Helpers.printJson(doc);
        }
    }



    //        System.out.print("Find all with interation:");
//        MongoCursor<Document> cursor = mongoCollection.find().iterator();
//        try {
//            while(cursor.hasNext()){
//                Document doc = cursor.next();
//                Helpers.printJson(doc);
//            }
//        } finally {
//            cursor.close();
//        }
//
//        System.out.print("Count:");
//        Long count = mongoCollection.count();
//        System.out.println("Count: " + count);

//        Document brown = new Document("name", "Brown").append("age", 15).append("profession", "student");
//        Helpers.printJson(brown);
//        mongoCollection.insertOne(brown);
//        Helpers.printJson(brown);
//
//        //insert many
//        Document smith = new Document("name", "Smith").append("age", 30).append("profession", "programmer");
//        Document jones = new Document("name", "Jones").append("age", 50).append("profession", "hacker");
//        Helpers.printJson(smith);
//        Helpers.printJson(jones);
//        mongoCollection.insertMany(asList(smith, jones));
//        Helpers.printJson(smith);
//        Helpers.printJson(jones);
}
