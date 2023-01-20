package com.compilit.mediator;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import java.util.ArrayList;
import java.util.List;

final class EventHandlerWrapper implements Provider<List<EventHandler<Event>>> {

  private final List<EventHandler<Event>> handlers = new ArrayList<>();

  public EventHandlerWrapper(List<EventHandler<Event>> handlers) {
    this.handlers.addAll(handlers);
  }

  @Override
  public List<EventHandler<Event>> provide() {
    return handlers;
  }
}
