package com.compilit.mediator;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import java.util.List;
import org.springframework.context.support.GenericApplicationContext;


final class EventHandlerProvider extends AbstractHandlerProvider {

  public EventHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
  }

  public List<EventHandler<Event>> getEventHandlers(Event event) {
    var id = getIdFor(event);
    if (!handlerCache.containsKey(id)) {
      var eventHandlers = findEventHandler(event);
      handlerCache.put(id, new EventHandlerWrapper(eventHandlers));
    }
    var provider = handlerCache.get(id);
    return (List<EventHandler<Event>>) provider.provide();
  }

  private <T extends Event> List<EventHandler<T>> findEventHandler(T request) {
    var requestClass = request.getClass();
    Class<EventHandler> handlerClass = EventHandler.class;
    return (List<EventHandler<T>>) findMatchingHandlers(
      requestClass,
      handlerClass
    );
  }

}
