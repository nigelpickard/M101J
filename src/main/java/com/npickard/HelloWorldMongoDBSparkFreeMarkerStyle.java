package com.npickard;

import com.mongodb.*;
import com.mongodb.bulk.*;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by npickard on 6/5/2015.
 */
public class HelloWorldMongoDBSparkFreeMarkerStyle {

    public static void main(String[] args) {
        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldMongoDBSparkFreeMarkerStyle.class, "/");

        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("course");

        final MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("hello");
        mongoCollection.drop();
        mongoCollection.insertOne(new Document("name", "MongoDB"));

        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                StringWriter writer = new StringWriter();

                try{
                    Document document = mongoCollection.find().first();
                    Template helloWorldTemplate = configuration.getTemplate("HelloWorld.ftl");
//                    Map<String, Object> helloWorldMap = new HashMap<String, Object>();
//                    helloWorldMap.put("name", "Bob");
//                    helloWorldTemplate.process(helloWorldMap, writer);
                    helloWorldTemplate.process(document, writer);
                    System.out.println(writer);


                } catch (TemplateException e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer.toString();
            }
        });

    }



//    public static void main(String[] args) {
//        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(10).build();
//        MongoClient mongoClient = new MongoClient(new ServerAddress(), mongoClientOptions);
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("test").withReadPreference(ReadPreference.secondary());
//
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("inserttest");
//        mongoCollection.drop();
//
//        //just insert one document at a time
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
//   }
}
