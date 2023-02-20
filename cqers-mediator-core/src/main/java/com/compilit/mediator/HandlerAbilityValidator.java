package com.compilit.mediator;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Predicate;
import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.QueryHandler;
import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;

final class HandlerAbilityValidator {

  private static final String EVENT_PATTERN = ".*<%s>";
  private static final String REQUEST_PATTERN = ".*<%s,.*>";
  private HandlerAbilityValidator() {}

  public static Predicate<RequestHandler<?,?>> handlersMatchingRequest(Class<? extends Request>  requestClass) {
    return requestHandler -> {
      var requestHandlerClass = requestHandler.getClass();
      var commandHandlerName = CommandHandler.class.getName();
      var queryHandlerName = QueryHandler.class.getName();
      var eventHandlerName = EventHandler.class.getName();
      return Arrays.stream(requestHandlerClass.getGenericInterfaces())
                   .map(Type::getTypeName)
                   .filter(namesContainingOneOf(commandHandlerName, queryHandlerName, eventHandlerName))
                   .anyMatch(matchingRequestPattern(requestClass).or(matchingEventPattern(requestClass)));
    };
  }

  private static Predicate<String> matchingRequestPattern(Class<? extends Request> requestClass) {
    var matcher = String.format(REQUEST_PATTERN, requestClass.getName());
    return requestName -> requestName.matches(matcher);
  }

  private static Predicate<String> matchingEventPattern(Class<? extends Request> requestClass) {
    var matcher = String.format(EVENT_PATTERN, requestClass.getName());
    return requestName -> requestName.matches(matcher);
  }

  private static Predicate<String> namesContainingOneOf(String commandHandlerName,
                                                      String queryHandlerName,
                                                      String eventHandlerName) {
    return interfaceName -> interfaceName.contains(commandHandlerName)
      || interfaceName.contains(queryHandlerName)
      || interfaceName.contains(eventHandlerName);
  }
}
