package com.compilit.mediator;

import java.util.List;
import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;

final class RequestMediator implements Mediator {

  private final CommandHandlerProvider commandHandlerProvider;
  private final QueryHandlerProvider queryHandlerProvider;
  private final EventHandlerProvider eventHandlerProvider;

  public RequestMediator(
    CommandHandlerProvider commandHandlerProvider,
    QueryHandlerProvider queryHandlerProvider,
    EventHandlerProvider eventHandlerProvider) {
    this.commandHandlerProvider = commandHandlerProvider;
    this.queryHandlerProvider = queryHandlerProvider;
    this.eventHandlerProvider = eventHandlerProvider;
  }

  @Override
  public <T extends Command<R>, R> R mediateCommand(T command) {
    CommandHandler<Command<R>, R> handler = commandHandlerProvider.getCommandHandler(command);
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
