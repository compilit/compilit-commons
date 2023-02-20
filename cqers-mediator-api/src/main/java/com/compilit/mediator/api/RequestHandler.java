package com.compilit.mediator.api;

/**
 * A generic parent for all request handlers.
 *
 * @param <T> The type of request.
 * @param <R> The return type of the request.
 * @see CommandHandler
 * @see QueryHandler
 * @see EventHandler
 */
public interface RequestHandler<T, R> {

  /**
   * @param request The specific implementation of either a Command, Query or Event.
   * @return R The return type of the request.
   */
  R handle(T request);

}
