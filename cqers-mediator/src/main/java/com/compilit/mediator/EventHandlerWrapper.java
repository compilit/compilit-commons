package com.compilit.mediator;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import java.util.ArrayList;
import java.util.List;

final class EventHandlerWrapper {

  private final List<EventHandler<Event>> handlers = new ArrayList<>();

  public EventHandlerWrapper(List<EventHandler<Event>> handlers) {
    this.handlers.addAll(handlers);
  }

  public List<EventHandler<Event>> provideHandler() {
    return handlers;
  }

}
