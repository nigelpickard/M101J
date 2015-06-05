package com.npickard;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by npickard on 6/5/2015.
 */
public class MongoDBDriverApp2 {
    public static void main(String[] args) {
       Document document = new Document().append("str", "MongoDB, Hello")
               .append("int", 42)
               .append("l", 1L)
               .append("double", 1.23)
               .append("b", false)
               .append("date", new Date())
               .append("objectId", new ObjectId())
               .append("null", null)
               .append("embeddedDoc", new Document("x", 0))
               .append("list", Arrays.asList(1,2,3,4,5,6,7,8,9));

        String str = document.getString("str");
        int i = document.getInteger("int");
        long l = document.getLong("l");
        double d = document.getDouble("double");
        Date date = document.getDate("date");
        ObjectId objectId = document.getObjectId("objectId");

        Helpers.printJson(document);
    }


}
