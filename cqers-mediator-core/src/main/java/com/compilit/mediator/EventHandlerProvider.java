package com.compilit.mediator;

import java.util.function.UnaryOperator;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import java.util.List;
import com.compilit.mediator.api.RequestHandler;


final class EventHandlerProvider extends AbstractHandlerProvider {

  private final List<? extends EventHandler<?>> eventHandlers;

  EventHandlerProvider(List<? extends EventHandler<?>> eventHandlers) {
    super();
    this.eventHandlers = eventHandlers;
  }

  List<EventHandler<Event>> getEventHandlers(Event event) {
    var id = getIdFor(event);
    if (!handlerCache.containsKey(id)) {
      var eventHandlers = findEventHandlers(event);
      handlerCache.put(id, new EventHandlerWrapper(eventHandlers));
    }
    var provider = handlerCache.get(id);
    return (List<EventHandler<Event>>) provider.provide();
  }

  private <T extends Event> List<EventHandler<T>> findEventHandlers(T request) {
    var requestClass = request.getClass();
    var requestName = requestClass.getName();
    List<EventHandler<T>> handlers = findMatchingHandlers(
      requestClass,
      eventHandlers
    );
    return this.<EventHandler<T>>validateResult(requestName).apply(handlers);
  }

  @Override
  protected <T extends RequestHandler<?,?>> UnaryOperator<List<T>> validateResult(String requestName) {
   return list -> {
     onEmptyListThrowException(list, requestName);
     return list;
   };
  }
}
