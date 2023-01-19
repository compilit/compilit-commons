package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;
import java.util.List;


final class RequestMediator implements Mediator {

  private final CommandHandlerProvider commandHandlerProvider;
  private final QueryHandlerProvider queryHandlerProvider;
  private final EventHandlerProvider eventHandlerProvider;
  private final AnnotationBasedEventHandler annotationBasedEventHandler;

  public RequestMediator(
    CommandHandlerProvider commandHandlerProvider,
    QueryHandlerProvider queryHandlerProvider,
    EventHandlerProvider eventHandlerProvider,
    AnnotationBasedEventHandler annotationBasedEventHandler) {
    this.commandHandlerProvider = commandHandlerProvider;
    this.queryHandlerProvider = queryHandlerProvider;
    this.eventHandlerProvider = eventHandlerProvider;
    this.annotationBasedEventHandler = annotationBasedEventHandler;
  }

  @Override
  public <T extends Command<R>, R> R mediateCommand(T command) {

    CommandHandler<Command<R>, R> handler = commandHandlerProvider.getCommandHandler(command);
    annotationBasedEventHandler.preHandle(handler, command);
    return handler.handle(command);
  }

  @Override
  public <T extends Query<R>, R> R mediateQuery(T query) {
    QueryHandler<Query<R>, R> handler = queryHandlerProvider.getQueryHandler(query);
    return handler.handle(query);
  }

  @Override
  public <T extends Event> void mediateEvent(T event) {
    List<EventHandler<Event>> handlers = eventHandlerProvider.getEventHandlers(event);
    for (var handler : handlers) {
      handler.handle(event);
    }
  }

}
