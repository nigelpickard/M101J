package com.npickard;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
        for (int i =0; i <10; i++){
            mongoCollection.insertOne(new Document("x", i));
        }

        System.out.print("Find one:");
        Document first = mongoCollection.find().first();
        Helpers.printJson(first);

        System.out.print("Find all with intro:");
        List<Document> docs = mongoCollection.find().into(new ArrayList<Document>());
        for (Document doc : docs){
            Helpers.printJson(doc);
        }

        System.out.print("Find all with interation:");
        MongoCursor<Document> cursor = mongoCollection.find().iterator();
        try {
            while(cursor.hasNext()){
                Document doc = cursor.next();
                Helpers.printJson(doc);
            }
        } finally {
            cursor.close();
        }

        System.out.print("Count:");
        Long count = mongoCollection.count();
        System.out.println("Count: " + count);

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
}
