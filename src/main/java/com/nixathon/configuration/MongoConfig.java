package com.nixathon.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConfig {
  private final static String URI = "mongodb+srv://m001-student:m001-mongodb-basics@sandbox.h6zzuym.mongodb.net/?retryWrites=true&w=majority";
  private static MongoClient mongoClient = null;

  private MongoConfig() {
  }

  public static synchronized MongoClient getMongoClient() {
    if (mongoClient == null) {
      mongoClient = MongoClients.create(URI);
    }
    return mongoClient;
  }
}
