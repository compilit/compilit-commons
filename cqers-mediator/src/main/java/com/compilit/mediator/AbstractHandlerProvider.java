package com.compilit.mediator;

import static com.compilit.mediator.ExceptionMessages.handlerNotFoundMessage;
import static com.compilit.mediator.ExceptionMessages.multipleHandlersRegisteredMessage;
import static com.compilit.mediator.GenericTypeParameterResolver.resolveGenericParameters;

import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;
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

  private static Predicate<Entry<String, ? extends RequestHandler>> handlersMatchingGenericSignature(Class<? extends Request> requestClass,
                                                                                                     Class<? extends RequestHandler> handlerClass) {
    return (handlerEntry) -> {
      var handlerName = handlerEntry.getKey();
      var handler = handlerEntry.getValue();
      List<Class<?>> generics = resolveGenericParameters(handler, handlerClass, handlerName);
      var requestTypeClass = generics.get(0);
      return requestTypeClass.equals(requestClass);
    };
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

  protected void assertValidResult(List<? extends RequestHandler> handlers, String requestName) {
    if (handlers.isEmpty()) {
      throw new MediatorException(handlerNotFoundMessage(requestName));
    }
    if (handlers.size() > 1) {
      throw new MediatorException(multipleHandlersRegisteredMessage(requestName));
    }
  }

}
