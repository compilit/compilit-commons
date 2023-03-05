package com.compilit.results;

/**
 * A generic interface for combining multiple results.
 *
 * @param <T> The result contents type
 */
public interface ResultCombiner<T> {

  /**
   * Combine the previously provided result with the one you provide here. Then continue to combine more results as
   * desired.
   *
   * @param result the next result you wish to combine with the previous.
   * @return ResultCombiner to chain the next result.
   */
  ContinuedResultCombiner<T> with(Result<T> result);

}
