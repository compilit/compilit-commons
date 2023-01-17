package com.compilit.mediator.api;

/**
 * EventEmitter are the main interactors to emit Events from. Events are 'fire and forget' notifications from actions
 * that have already occurred.
 *
 * @see Event
 */
public interface EventEmitter {

  /**
   * Emit the Event to the Mediator in order to handle it.
   *
   * @param event  The specific Event you wish to emit to the Mediator.
   * @param events The extra Events you wish to emit to the Mediator.
   */
  void emit(Event event, Event... events);
}
