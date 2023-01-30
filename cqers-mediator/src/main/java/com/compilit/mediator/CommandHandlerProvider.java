package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;
import java.util.List;
import org.springframework.context.support.GenericApplicationContext;

final class CommandHandlerProvider extends AbstractHandlerProvider {

  CommandHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
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
    var handlerClass = CommandHandler.class;
    List<CommandHandler> handlers = (List<CommandHandler>) findMatchingHandlers(
      requestClass,
      handlerClass
    );
    assertValidResult(handlers, requestName);
    return handlers.get(FIRST_ENTRY);
  }


}
