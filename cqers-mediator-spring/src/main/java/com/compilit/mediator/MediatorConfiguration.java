package com.compilit.mediator;

import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.QueryDispatcher;
import com.compilit.mediator.api.QueryHandler;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MediatorConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(MediatorConfiguration.class);

  @Bean
  CommandHandlerProvider createCommandHandlerProvider(List<CommandHandler<?, ?>> commandHandlers) {
    String message = createMessage(commandHandlers, "CommandHandlers");
    logger.info(message);
    return new CommandHandlerProvider(commandHandlers);
  }

  @Bean
  QueryHandlerProvider createQueryHandlerProvider(List<QueryHandler<?, ?>> queryHandlers) {
    String message = createMessage(queryHandlers, "QueryHandlers");
    logger.info(message);
    return new QueryHandlerProvider(queryHandlers);
  }

  @Bean
  EventHandlerProvider createEventHandlerProvider(List<EventHandler<?>> eventHandlers) {
    String message = createMessage(eventHandlers, "EventHandlers");
    logger.info(message);
    return new EventHandlerProvider(eventHandlers);
  }

  @Bean
  Mediator createMediator(
    CommandHandlerProvider commandHandlerProvider,
    QueryHandlerProvider queryHandlerProvider,
    EventHandlerProvider eventHandlerProvider
  ) {
    return new RequestMediator(commandHandlerProvider, queryHandlerProvider, eventHandlerProvider);
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

  @Bean
  InitializingBean createInstanceProvider(CommandDispatcher commandDispatcher,
                                          QueryDispatcher queryDispatcher,
                                          EventEmitter eventEmitter) {
    return new Dispatchers(commandDispatcher, queryDispatcher, eventEmitter);
  }

  private static String createMessage(List<?> requestHandlers, String name) {
    var handlers = toString(requestHandlers);
    var messageBuilder = new StringJoiner("\n");
    messageBuilder.add(String.format("Registered %s:", name));
    handlers.forEach(handler -> messageBuilder.add(" - " + handler));
    return messageBuilder.toString();
  }

  private static List<String> toString(List<?> objects) {
    return objects.stream().map(o -> o.getClass().getName()).toList();
  }
}