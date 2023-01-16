package com.compilit.results;

import java.util.List;

public interface ContinuedResultCombiner<T> {

  /**
   * @param result the next result you wish to combine with the previous.
   * @return ResultCombiner to chain the next result.
   */
  ContinuedResultCombiner<T> and(Result<T> result);

  /**
   * Get a list of all contents of the passed results if all results were successful. Returns a
   * SuccessResult if, and only if all other results were successful.
   * In case of a summed up unsuccessful result, the message will contain the error message
   * of each underlying unsuccessful result
   *
   * @return Result containing a List of T.
   */
  Result<List<T>> merge();

  /**
   * Get the combined result without contents of all passed results. Returns a SuccessResult if, and
   * only if all other results were successful.
   * In case of a summed up unsuccessful result, the message will contain the error message
   * of each underlying unsuccessful result
   *
   * @return Result of all others combined.
   */
  Result<T> sum();

}
