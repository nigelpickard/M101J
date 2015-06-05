package com.npickard;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

/**
 * Created by npickard on 6/5/2015.
 */
public class MongoDBDriverProjection {
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
                            .append("z", new Random().nextInt(10))
            );
        }

        Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));

        //0 means exclude this field from results, 1 means include field from results
        //NOTE: can not mix include and excludes; they all have to be 0 or all have to be 1
        Bson excludeProjection = new Document("x", 0).append("y", 0);
        Bson includeProjection = new Document("z", 1);


        System.out.println("Find all with filter:");
        List<Document> docs = mongoCollection.find(filter).projection(includeProjection).projection(excludeProjection).into(new ArrayList<Document>());
        for (Document doc : docs){
            Helpers.printJson(doc);
        }


        //or lets use the Projections

        Bson filter2 = and(eq("x", 0));

        Bson projection = fields(include("x", "z"), exclude("_id"));
        System.out.println("Find all with filter:");
        List<Document> docs2 = mongoCollection.find(filter2).projection(projection).into(new ArrayList<Document>());
        for (Document doc : docs2){
            Helpers.printJson(doc);
        }



        System.out.println("Count with filter:");
        Long count = mongoCollection.count(filter);
        System.out.println("Count with filter: " + count);

    }
}
