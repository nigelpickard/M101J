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
import java.util.Random;

/**
 * Created by npickard on 6/5/2015.
 */
public class MongoDBDriverFilter {
    public static void main(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("inserttest");
        mongoCollection.drop();

        //insert some docs
        for (int i =0; i <10; i++){
            mongoCollection.insertOne(new Document()
                        .append("x", new Random().nextInt(2))
                        .append("y", new Random().nextInt(100))
                        );
        }

        Bson filter = new Document("x", 0)
                .append("y", new Document("$gt",50));

        System.out.println("Find all with filter:");
        List<Document> docs = mongoCollection.find(filter).into(new ArrayList<Document>());
        for (Document doc : docs){
            Helpers.printJson(doc);
        }


        System.out.println("Count with filter:");
        Long count = mongoCollection.count(filter);
        System.out.println("Count with filter: " + count);

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
