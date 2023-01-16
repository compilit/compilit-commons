package com.compilit.results;

public interface ResultCombiner<T> {

  /**
   * @param result the next result you wish to combine with the previous.
   * @return ResultCombiner to chain the next result.
   */
  ContinuedResultCombiner<T> with(Result<T> result);

}
