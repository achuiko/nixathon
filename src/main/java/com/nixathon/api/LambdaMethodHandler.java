package com.nixathon.api;

import java.util.Date;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class LambdaMethodHandler implements RequestHandler<Object, String> {
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        String uri = "mongodb+srv://m001-student:<password>@sandbox.h6zzuym.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            var inputDoc = new Document("input", input);
            inputDoc.append("created", new Date());
            MongoDatabase database = mongoClient.getDatabase("nixathon");
            database.getCollection("nixathon").insertOne(inputDoc);
            try {
                System.out.println("Connected successfully to server.");
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to write in mongo");
            }
        }
        return "Input saved: " + input;
    }
}