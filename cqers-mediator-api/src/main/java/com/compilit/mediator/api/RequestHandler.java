package com.compilit.mediator.api;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * A generic parent for all request handlers.
 *
 * @param <T> The type of request.
 * @param <R> The return type of the request.
 * @see CommandHandler
 * @see QueryHandler
 * @see EventHandler
 */
public interface RequestHandler<T, R> {

  /**
   * @param request The specific implementation of either a Command, Query or Event.
   * @return R The return type of the request.
   */
  R handle(T request);

  /**
   * Filters the
   * @param requestClass the Request class which is sent into the mediator
   * @return true if the current RequestHandler instance is actually able to handle this request class.
   */
  default boolean canHandle(Class<? extends Request>  requestClass) {
    var commandHandlerName = CommandHandler.class.getName();
    var queryHandlerName = QueryHandler.class.getName();
    var eventHandlerName = EventHandler.class.getName();
    return Arrays.stream(getClass().getGenericInterfaces())
                 .map(Type::getTypeName)
                 .filter(interfaceName -> interfaceName.contains(commandHandlerName)
                   || interfaceName.contains(queryHandlerName)
                   || interfaceName.contains(eventHandlerName))
                 .map(handlerInterfaceName -> handlerInterfaceName.split("<")[1])
                 .map(handlerInterfaceName -> handlerInterfaceName.split(",")[0])
                 .anyMatch(requestName -> requestName.contains(requestClass.getName()));
  }
}
