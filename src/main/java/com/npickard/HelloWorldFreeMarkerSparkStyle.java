package com.npickard;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by npickard on 6/1/2015.
 */
public class HelloWorldFreeMarkerSparkStyle {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldFreeMarkerSparkStyle.class, "/");

        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                StringWriter writer = new StringWriter();

                try{
                    Template helloWorldTemplate = configuration.getTemplate("HelloWorld.ftl");
                    Map<String, Object> helloWorldMap = new HashMap<String, Object>();
                    helloWorldMap.put("name", "Bob");
                    helloWorldTemplate.process(helloWorldMap, writer);
                    System.out.println(writer);
                } catch (TemplateException e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer.toString();
            }
        });
    }
}
