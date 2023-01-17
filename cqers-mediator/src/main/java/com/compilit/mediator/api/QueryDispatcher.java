package com.compilit.mediator.api;

/**
 * A QueryDispatcher is a dedicated interface for sending Queries to the Mediator. A Mediator should never be interacted
 * with directly because you could never truly know that your code complies with CQERS.
 *
 * @see Query
 */
public interface QueryDispatcher {

  /**
   * Dispatch the Query to the Mediator in order to handle it.
   *
   * @param query The specific Query to send to the Mediator.
   * @param <T>   The return type.
   * @return T The return type value.
   */
  <T> T dispatch(Query<T> query);
}
