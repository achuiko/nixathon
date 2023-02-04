package com.nixathon.configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.nixathon.configuration.codec.TestInputCodec;

public class MongoConfig {
  private final static String URI = "mongodb+srv://sandbox.h6zzuym.mongodb.net/?retryWrites=true&w=majority";
  private static MongoClient mongoClient = null;

  private MongoConfig() {
  }

  public static synchronized MongoClient getMongoClient() {
    if (mongoClient == null) {
      var codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
          fromCodecs(new TestInputCodec()));
      var settings = MongoClientSettings.builder()
          .applyConnectionString(new ConnectionString(URI))
          .credential(MongoCredential.createCredential("m001-student", "admin", "<password>".toCharArray()))
          .codecRegistry(codecRegistry)
          .build();
      mongoClient = MongoClients.create(settings);
    }
    return mongoClient;
  }
}
