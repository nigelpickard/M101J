package com.npickard;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by npickard on 5/28/2015.
 */
public class HelloWorldSparkStyle {
    public static void main(String[] args) {
        Spark.get(new Route("/"){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "Hello World";
            }
        });
    }
}
