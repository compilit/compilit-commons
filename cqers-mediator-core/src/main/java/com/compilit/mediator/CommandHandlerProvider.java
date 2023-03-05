package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.RequestHandler;
import java.util.List;
import java.util.function.UnaryOperator;

final class CommandHandlerProvider extends AbstractHandlerProvider {

  private final List<? extends CommandHandler<?, ?>> commandHandlers;

  CommandHandlerProvider(List<? extends CommandHandler<?, ?>> commandHandlers) {
    super();
    this.commandHandlers = commandHandlers;
  }

  @Override
  protected <T extends RequestHandler<?, ?>> UnaryOperator<List<T>> validateResult(String requestName) {
    return list -> {
      onEmptyListThrowException(list, requestName);
      onListSizeMoreThanOneThrowException(list, requestName);
      return list;
    };
  }

  <R> CommandHandler<Command<R>, R> getCommandHandler(Command<R> command) {
    var id = getIdFor(command);
    if (!handlerCache.containsKey(id)) {
      var handler = findCommandHandler(command);
      handlerCache.put(id, new CommandHandlerWrapper<>(handler));
    }
    return (CommandHandler<Command<R>, R>) handlerCache.get(id).provide();
  }

  private <T extends Command<R>, R> CommandHandler<T, R> findCommandHandler(T command) {
    var requestClass = command.getClass();
    var requestName = requestClass.getName();
    List<CommandHandler<T, R>> handlers = findMatchingHandlers(
      requestClass,
      commandHandlers
    );
    return this.<CommandHandler<T, R>>validateResult(requestName).apply(handlers).get(FIRST_ENTRY);
  }
}
