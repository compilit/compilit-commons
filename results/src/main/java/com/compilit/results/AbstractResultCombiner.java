package com.compilit.results;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractResultCombiner<T> {

  protected final List<T> resultList = new ArrayList<>();
  protected final List<String> messages = new ArrayList<>();
  protected boolean isSuccessful = true;

  AbstractResultCombiner() {}

  AbstractResultCombiner(List<T> resultList, List<String> messages, boolean isSuccessful) {
    this.resultList.addAll(resultList);
    this.messages.addAll(messages);
    this.isSuccessful = isSuccessful;
  }

  protected void resolve(Result<T> result) {
    if (result.hasContents()) {
      resultList.add(result.getContents().get());
    }
    if (result.isUnsuccessful()) {
      isSuccessful = false;
      if (result.getMessage() != null) {
        messages.add(result.getMessage());
      }
    }
  }
}
