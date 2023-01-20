package com.compilit.mediator;

import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.QueryDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

@Configuration
public class MediatorConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(MediatorConfiguration.class);

  @Bean
  CommandHandlerProvider createCommandHandlerProvider(GenericApplicationContext genericApplicationContext) {
    logger.info("Registered CommandHandlers");
    return new CommandHandlerProvider(genericApplicationContext);
  }

  @Bean
  QueryHandlerProvider createQueryHandlerProvider(GenericApplicationContext genericApplicationContext) {
    logger.info("Registered QueryHandlers");
    return new QueryHandlerProvider(genericApplicationContext);
  }

  @Bean
  EventHandlerProvider createEventHandlerProvider(GenericApplicationContext genericApplicationContext) {
    logger.info("Registered EventHandlers");
    return new EventHandlerProvider(genericApplicationContext);
  }

//  @Bean
//  AnnotationBasedEventHandler annotationBasedEventHandler(EventHandlerProvider eventEmitter,
//                                                          GenericApplicationContext genericApplicationContext) {
//    logger.info("Registered AnnotationBasedEventHandler");
//    var x = new AnnotationBasedEventHandler(eventEmitter);
//    x.resolveEventHandlers(genericApplicationContext);
//    return x;
//  }

  @Bean
  Mediator createMediator(
    CommandHandlerProvider commandHandlerProvider,
    QueryHandlerProvider queryHandlerProvider,
    EventHandlerProvider eventHandlerProvider
//    AnnotationBasedEventHandler annotationBasedEventHandler
  ) {
    return new RequestMediator(commandHandlerProvider, queryHandlerProvider, eventHandlerProvider
//                               annotationBasedEventHandler
    );
  }

  @Bean
  CommandDispatcher createCommandDispatcher(Mediator mediator) {
    return new MediatingCommandDispatcher(mediator);
  }

  @Bean
  QueryDispatcher createQueryDispatcher(Mediator mediator) {
    return new MediatingQueryDispatcher(mediator);
  }

  @Bean
  EventEmitter createEventEmitter(Mediator mediator) {
    return new MediatingEventEmitter(mediator);
  }

}