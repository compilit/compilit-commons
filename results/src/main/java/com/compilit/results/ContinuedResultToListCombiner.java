package com.compilit.results;

import java.util.List;

final class ContinuedResultToListCombiner<T> extends AbstractResultCombiner<T> implements ContinuedResultCombiner<T> {

  ContinuedResultToListCombiner(List<T> resultList, List<String> messages, boolean isSuccessful) {
    super(resultList, messages, isSuccessful);
  }

  @Override
  public ContinuedResultCombiner<T> and(Result<T> result) {
    resolve(result);
    return this;
  }

  public Result<List<T>> merge() {
    var finalMessage = prepareMessage();
    return isSuccessful
           ? Result.success(resultList)
           : Result.unprocessable(finalMessage);
  }

  public Result<T> sum() {
    var finalMessage = prepareMessage();
    return isSuccessful
           ? Result.success()
           : Result.unprocessable(finalMessage);
  }

  private String prepareMessage() {
    var stringBuilder = new StringBuilder(Messages.UNSUCCESSFUL_RESULT);
    for (int index = 0; index < messages.size(); index++) {
      var message = messages.get(index);
      stringBuilder.append("message-").append(index + 1).append(": ").append(message);
      if (index == messages.size() - 1) {
        return stringBuilder.toString();
      }
      stringBuilder.append(", ");
    }
    return stringBuilder.toString();
  }

}
