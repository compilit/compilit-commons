package com.compilit.jsonpatch;

import com.compilit.jsonpatch.api.Patchable;
import jakarta.json.JsonPatch.Operation;
import java.util.List;

class TestEntity implements PatchableEntity {

  @Patchable(allow = Operation.REPLACE)
  private String testField;

  @Patchable(allow = {Operation.ADD, Operation.REMOVE})
  private List<String> testCollection;

  public TestEntity() {}

  public TestEntity(String testField, List<String> testCollection) {
    this.testField = testField;
    this.testCollection = testCollection;
  }

  public String getTestField() {
    return testField;
  }

  public void setTestField(String testField) {
    this.testField = testField;
  }

  public List<String> getTestCollection() {
    return testCollection;
  }

  public void setTestCollection(List<String> testCollection) {
    this.testCollection = testCollection;
  }
}