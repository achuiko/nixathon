package com.nixathon.configuration.codec;

import java.util.Date;

import com.nixathon.model.TestInput;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public class TestInputCodec implements Codec<TestInput> {

  public TestInputCodec() {
    super();
  }

  @Override
  public TestInput decode(BsonReader bsonReader, DecoderContext decoderContext) {
    var testInput = new TestInput();
    bsonReader.readStartDocument();
    while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      String fieldName = bsonReader.readName();
      switch (fieldName) {
        case "_id":
          testInput.setId(String.valueOf(bsonReader.readObjectId()));
          break;
        case "created":
          testInput.setCreated(new Date(bsonReader.readDateTime()));
          break;
        case "input":
          testInput.setInput(bsonReader.readString());
          break;
      }
    }
    bsonReader.readEndDocument();
    return testInput;
  }

  @Override
  public void encode(BsonWriter bsonWriter, TestInput testInput, EncoderContext encoderContext) {
    bsonWriter.writeStartDocument();
    if (testInput.getId() != null) {
      bsonWriter.writeObjectId("_id", new ObjectId(testInput.getId()));
    }
    if (testInput.getCreated() != null) {
      bsonWriter.writeDateTime("created", testInput.getCreated().getTime());
    }
    if (testInput.getInput() != null) {
      bsonWriter.writeString("input", testInput.getInput());
    }
    bsonWriter.writeEndDocument();
  }

  @Override
  public Class<TestInput> getEncoderClass() {
    return TestInput.class;
  }
}
