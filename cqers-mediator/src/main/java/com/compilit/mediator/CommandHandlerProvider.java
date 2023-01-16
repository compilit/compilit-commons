package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.support.GenericApplicationContext;


final class CommandHandlerProvider extends AbstractHandlerProvider {

  private final Map<String, CommandHandlerWrapper> commandHandlerMap = new HashMap<>();

  public CommandHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
  }

  public <R> CommandHandler<Command<R>, R> getCommandHandler(Command<R> requestClass) {
    try {
      var hash = getHashFor(requestClass);
      if (!commandHandlerMap.containsKey(hash)) {
        var handler = findCommandHandler(requestClass);
        commandHandlerMap.put(hash, new CommandHandlerWrapper(handler));
      }
      return commandHandlerMap.get(hash).provideHandler();
    } catch (Exception exception) {
      throw new MediatorException(exception.getMessage());
    }
  }

  private <T extends Command<R>, R> CommandHandler<T, R> findCommandHandler(T requestClass) {
    var atomicReference = new AtomicReference<CommandHandler<T, R>>();
    setAtomicReference(atomicReference, CommandHandler.class, requestClass);
    makeSureAtomicReferenceIsNotNull(atomicReference, requestClass.getClass().getName());
    return atomicReference.get();
  }

}
