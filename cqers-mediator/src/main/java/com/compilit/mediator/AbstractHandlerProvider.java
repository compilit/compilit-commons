package com.compilit.mediator;

import static com.compilit.mediator.ExceptionMessages.handlerNotFoundMessage;
import static com.compilit.mediator.ExceptionMessages.multipleHandlersRegisteredMessage;

import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.EventHandler;
import com.compilit.mediator.api.QueryHandler;
import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.context.support.GenericApplicationContext;

abstract class AbstractHandlerProvider {

  protected static final int FIRST_ENTRY = 0;
  protected final GenericApplicationContext genericApplicationContext;
  protected final Map<String, Provider> handlerCache = new HashMap<>();

  protected AbstractHandlerProvider(GenericApplicationContext genericApplicationContext) {
    this.genericApplicationContext = genericApplicationContext;
  }

  protected <T> String getIdFor(T requestClass) {
    return UUID.nameUUIDFromBytes(requestClass.getClass().getName().getBytes()).toString();
  }

  protected List<? extends RequestHandler> findMatchingHandlers(Class<? extends Request> requestClass,
                                                                Class<? extends RequestHandler> handlerClass) {

    return genericApplicationContext.getBeanFactory()
                                    .getBeansOfType(handlerClass)
                                    .entrySet()
                                    .stream()
                                    .filter(handlersMatchingGenericSignature(requestClass, handlerClass))
                                    .map(Entry::getValue)
                                    .toList();
  }

  private static Predicate<Entry<String, ? extends RequestHandler>> handlersMatchingGenericSignature(Class<? extends Request> requestClass,
                                                                                                     Class<? extends RequestHandler> handlerClass) {
    return (handlerEntry) -> {
      var handler = handlerEntry.getValue();
      return handler.canHandle(requestClass);
    };
  }

  private static <H> boolean handlerCanHandleRequest(
    H handler,
    Class<?> requestClass
  ) {
    var commandHandlerName = CommandHandler.class.getName();
    var queryHandlerName = QueryHandler.class.getName();
    var eventHandlerName = EventHandler.class.getName();
    return Arrays.stream(handler.getClass().getGenericInterfaces())
                 .map(Type::getTypeName)
                 .filter(interfaceName -> interfaceName.contains(commandHandlerName)
                   || interfaceName.contains(queryHandlerName)
                   || interfaceName.contains(eventHandlerName))
                 .map(handlerInterfaceName -> handlerInterfaceName.split("<")[1])
                 .map(handlerInterfaceName -> handlerInterfaceName.split(",")[0])
                 .anyMatch(requestName -> requestName.contains(requestClass.getName()));
  }

  protected void assertValidResult(List<? extends RequestHandler> handlers, String requestName) {
    if (handlers.isEmpty()) {
      throw new MediatorException(handlerNotFoundMessage(requestName));
    }
    if (handlers.size() > 1) {
      throw new MediatorException(multipleHandlersRegisteredMessage(requestName));
    }
  }

}
