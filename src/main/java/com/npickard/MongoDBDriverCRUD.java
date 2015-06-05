package com.npickard;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static java.util.Arrays.asList;

/**
 * Created by npickard on 6/5/2015.
 */
public class MongoDBDriverCRUD {
    public static void main(String[] args) {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("inserttest");
        mongoCollection.drop();

        //just insert one document at a time
        Document brown = new Document("name", "Brown").append("age", 15).append("profession", "student");
        Helpers.printJson(brown);
        mongoCollection.insertOne(brown);
        Helpers.printJson(brown);

        //insert many
        Document smith = new Document("name", "Smith").append("age", 30).append("profession", "programmer");
        Document jones = new Document("name", "Jones").append("age", 50).append("profession", "hacker");
        Helpers.printJson(smith);
        Helpers.printJson(jones);
        mongoCollection.insertMany(asList(smith, jones));
        Helpers.printJson(smith);
        Helpers.printJson(jones);





    }
}
