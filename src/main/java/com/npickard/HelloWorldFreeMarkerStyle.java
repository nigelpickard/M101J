package com.npickard;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by npickard on 6/1/2015.
 */
public class HelloWorldFreeMarkerStyle {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class, "/");
        try {
            Template helloWorldTemplate = configuration.getTemplate("HelloWorld.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> helloWorldMap = new HashMap<String, Object>();
            helloWorldMap.put("name","Bob");

            try {
                helloWorldTemplate.process(helloWorldMap, writer);
                System.out.println(writer);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
