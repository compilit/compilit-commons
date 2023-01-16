package com.compilit.results;

final class ResultToListCombiner<T> extends AbstractResultCombiner<T> implements ResultCombiner<T> {

  ResultToListCombiner(Result<T> result) {
    super();
    resolve(result);
  }

  @Override
  public ContinuedResultCombiner<T> with(Result<T> result) {
    resolve(result);
    return new ContinuedResultToListCombiner<>(resultList, messages, isSuccessful);
  }

}
