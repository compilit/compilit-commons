package com.compilit.mediator.api;

/**
 * EventEmitter are the main interactors to emit Events from. Events are 'fire and forget' notifications from actions
 * that have already occurred. They can be used in any way, but they are usually the result of
 *
 * @see Event
 */
public interface EventEmitter {

  /**
   * Send the event into the mediator. If matching handlers are found, apply all these handlers to the event. In a way,
   * all these handlers are 'subscribed' to this event.
   *
   * @param event  The specific Event you wish to emit to the Mediator.
   * @param events The extra Events you wish to emit to the Mediator.
   */
  void emit(Event event, Event... events);
}
