package com.compilit.mediator;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventEmitter;


final class EventEmitterImpl implements EventEmitter {

  private final Mediator mediator;

  public EventEmitterImpl(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void emit(Event event, Event... events) {
    mediator.mediateEvent(event);
    if (event != null) {
      for (var e : events) {
        mediator.mediateEvent(e);
      }
    }
  }

}
