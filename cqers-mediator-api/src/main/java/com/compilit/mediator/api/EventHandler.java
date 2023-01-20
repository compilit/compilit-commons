package com.compilit.mediator.api;

/**
 * EventHandlers are for all event based action. An event can be the result of Commands or Queries.
 *
 * @param <T> The Event type this EventHandler can handle.
 */
public interface EventHandler<T extends Event> extends RequestHandler<T, Void> {

  Void handle(T command);

}
