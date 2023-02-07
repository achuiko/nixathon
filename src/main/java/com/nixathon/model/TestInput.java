package com.nixathon.model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

public class TestInput {

  private String id;

  private Date created;

  private String input;

  public String getId() {
    return id;
  }

  public TestInput() {
  }

  public TestInput(String input) {
    this.input = input;
  }

  public TestInput(String id, Date created, String input) {
    this.id = id;
    this.created = created;
    this.input = input;
  }

  public static TestInput toTestInput(Document document) {
    var testInput = new TestInput();
    testInput.setId(String.valueOf(document.getObjectId("_id")));
    testInput.setInput(document.getString("input"));
    testInput.setCreated(document.getDate("created"));
    return testInput;
  }

  public static Document toDocument(TestInput testInput) {
    var document = new Document();
    Optional.ofNullable(testInput.getId()).ifPresent(id -> document.put("_id", new ObjectId(id)));
    document.put("input", testInput.getInput());
    document.put("created", testInput.getCreated());
    return document;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TestInput testInput = (TestInput) o;
    return id.equals(testInput.id) && created.equals(testInput.created) && input.equals(testInput.input);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, created, input);
  }

  @Override
  public String toString() {
    return "{" +
        "id :'" + id + '\'' +
        ", created :" + created +
        ", input :'" + input + '\'' +
        '}';
  }
}
