package com.nixathon.api;

import java.util.Date;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nixathon.configuration.MongoConfig;
import com.nixathon.model.TestInput;

public class LambdaMethodHandler implements RequestHandler<Map<String, String>, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(Map<String, String> map, Context context) {
        try {
            var body = new TestInput(map.get("input"));
            context.getLogger().log("Input: " + body);
            var savedInput = saveData(body);
            return createResponse(savedInput.toString(), 200);
        } catch (MongoWriteException e) {
            context.getLogger().log("An error occurred while attempting to write in mongo");
            return createResponse("{\"errorMessage\":\"Error occurred while saving\"}", 500);
        } catch (ClassCastException e) {
            return createResponse("{\"errorMessage\":\"invalid json\"}", 400);
        }
    }

    private TestInput saveData(TestInput testInput) {
        testInput.setCreated(new Date());
        MongoDatabase database = MongoConfig.getMongoClient().getDatabase("nixathon");
        MongoCollection<TestInput> collection = database.getCollection("nixathon", TestInput.class);
        collection.insertOne(testInput);
        MongoConfig.getMongoClient().close();
        return testInput;
    }

    private APIGatewayProxyResponseEvent createResponse(String message, Integer code) {
        return new APIGatewayProxyResponseEvent().withBody(message).withStatusCode(code);
    }
}