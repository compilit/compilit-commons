package com.compilit.mediator;

import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.QueryDispatcher;
import com.compilit.mediator.api.QueryHandler;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.context.support.GenericApplicationContext;

public class TestApplicationContext {

  private static final String IOC_CONTAINER_NOT_AVAILABLE = "genericApplicationContext not available";

  public static GenericApplicationContext registerCqersModule(GenericApplicationContext genericApplicationContext) {
    Objects.requireNonNull(genericApplicationContext, IOC_CONTAINER_NOT_AVAILABLE);
    registerHandlerProviders(genericApplicationContext);
    registerMediator(genericApplicationContext);
    registerDispatchers(genericApplicationContext);
    return genericApplicationContext;
  }

  private static void registerDispatchers(GenericApplicationContext genericApplicationContext) {
    var mediator = genericApplicationContext.getBean(Mediator.class);
    genericApplicationContext.registerBean(
      CommandDispatcher.class,
      () -> new MediatingCommandDispatcher(mediator)
    );
    genericApplicationContext.registerBean(
      QueryDispatcher.class,
      () -> new MediatingQueryDispatcher(mediator)
    );
    genericApplicationContext.registerBean(
      EventEmitter.class,
      () -> new MediatingEventEmitter(mediator)
    );
  }

  private static void registerMediator(GenericApplicationContext genericApplicationContext) {
    var commandHandlerProvider = genericApplicationContext.getBean(CommandHandlerProvider.class);
    var queryHandlerProvider = genericApplicationContext.getBean(QueryHandlerProvider.class);
    var eventHandlerProvider = genericApplicationContext.getBean(EventHandlerProvider.class);
    genericApplicationContext.registerBean(
      Mediator.class,
      () -> new RequestMediator(
        commandHandlerProvider,
        queryHandlerProvider,
        eventHandlerProvider
      )
    );
  }

  private static void registerHandlerProviders(GenericApplicationContext genericApplicationContext) {
    var commandHandlers = Arrays.stream(genericApplicationContext.getBeanNamesForType(CommandHandler.class)).map(
      genericApplicationContext::getBean).map(x -> (CommandHandler<?,?>)x).toList();
    var queryHandlers = Arrays.stream(genericApplicationContext.getBeanNamesForType(QueryHandler.class)).map(
      genericApplicationContext::getBean).map(x -> (QueryHandler<?,?>)x).toList();
    var eventHandlers = Arrays.stream(genericApplicationContext.getBeanNamesForType(EventHandler.class)).map(
      genericApplicationContext::getBean).map(x -> (EventHandler<?>)x).toList();
    genericApplicationContext.registerBean(
      CommandHandlerProvider.class,
      () -> new CommandHandlerProvider(commandHandlers)
    );
    genericApplicationContext.registerBean(
      QueryHandlerProvider.class,
      () -> new QueryHandlerProvider(queryHandlers)
    );
    genericApplicationContext.registerBean(
      EventHandlerProvider.class,
      () -> new EventHandlerProvider(eventHandlers)
    );
  }

}
