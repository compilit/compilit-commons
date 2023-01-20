package com.compilit.mediator;

import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.QueryDispatcher;
import java.util.Objects;
import org.springframework.context.support.GenericApplicationContext;

public class TestApplicationContext {

  private static final String IOC_CONTAINER_NOT_AVAILABLE = "genericApplicationContext not available";

  public static GenericApplicationContext registerCqersModule(GenericApplicationContext genericApplicationContext) {
    Objects.requireNonNull(genericApplicationContext, IOC_CONTAINER_NOT_AVAILABLE);
    registerHandlerProviders(genericApplicationContext);
    registerAnnotationBasedEventHandler(
      genericApplicationContext.getBean(EventHandlerProvider.class),
      genericApplicationContext
    );
    registerMediator(genericApplicationContext);
    registerDispatchers(genericApplicationContext);
    return genericApplicationContext;
  }

  private static void registerAnnotationBasedEventHandler(EventHandlerProvider eventHandlerProvider,
                                                          GenericApplicationContext genericApplicationContext) {
    var annotationBasedEventHandler = new AnnotationBasedEventHandler(eventHandlerProvider);
    annotationBasedEventHandler.resolveEventHandlers(genericApplicationContext);
    genericApplicationContext.registerBean(
      AnnotationBasedEventHandler.class,
      () -> annotationBasedEventHandler
    );
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
    var annotationBasedEventHandler = genericApplicationContext.getBean(AnnotationBasedEventHandler.class);
    genericApplicationContext.registerBean(
      Mediator.class,
      () -> new RequestMediator(
        commandHandlerProvider,
        queryHandlerProvider,
        eventHandlerProvider,
        annotationBasedEventHandler
      )
    );
  }

  private static void registerHandlerProviders(GenericApplicationContext genericApplicationContext) {
    genericApplicationContext.registerBean(
      CommandHandlerProvider.class,
      () -> new CommandHandlerProvider(genericApplicationContext)
    );
    genericApplicationContext.registerBean(
      QueryHandlerProvider.class,
      () -> new QueryHandlerProvider(genericApplicationContext)
    );
    genericApplicationContext.registerBean(
      EventHandlerProvider.class,
      () -> new EventHandlerProvider(genericApplicationContext)
    );
  }

}
