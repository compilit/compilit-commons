package testutil;

import java.util.ArrayList;
import java.util.List;

public class TestObject {

  private final String message;

  private final List<String> testList = new ArrayList<>();

  public TestObject() {
    this.message = "Nothing to report";
  }

  public TestObject(String message) {
    this.message = message;
  }

  public void add(String value) {
    testList.add(value);
  }

  public boolean hasValues() {
    return !testList.isEmpty();
  }

  public String getMessage() {
    return message;
  }
}
