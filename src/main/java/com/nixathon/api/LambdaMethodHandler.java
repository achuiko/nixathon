package com.nixathon.api;

import java.util.Date;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nixathon.configuration.MongoConfig;
import com.nixathon.model.TestInput;
import org.bson.Document;

public class LambdaMethodHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try {
            context.getLogger().log("Input: " + event);
            var body = new ObjectMapper().readValue(event.getBody(), TestInput.class);
            var savedInput = saveData(body);
            return createResponse(savedInput.toString(), 200);
        } catch (MongoWriteException e) {
            context.getLogger().log("An error occurred while attempting to write in mongo");
            return createResponse("{\"errorMessage\":\"Error occurred while saving\"}", 500);
        } catch (ClassCastException | JsonProcessingException e) {
            return createResponse("{\"errorMessage\":\"invalid json\"}", 400);
        }
    }

    private TestInput saveData(TestInput body) {
        body.setCreated(new Date());
        var testInputDocument = TestInput.toDocument(body);
        MongoDatabase database = MongoConfig.getMongoClient().getDatabase("nixathon");
        MongoCollection<Document> collection = database.getCollection("nixathon");
        collection.insertOne(testInputDocument);
        return TestInput.toTestInput(testInputDocument);
    }

    private APIGatewayProxyResponseEvent createResponse(String message, Integer code) {
        return new APIGatewayProxyResponseEvent().withBody(message).withStatusCode(code);
    }
}