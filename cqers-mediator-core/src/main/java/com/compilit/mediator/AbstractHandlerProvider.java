package com.compilit.mediator;

import static com.compilit.mediator.ExceptionMessages.handlerNotFoundMessage;
import static com.compilit.mediator.ExceptionMessages.multipleHandlersRegisteredMessage;
import static com.compilit.mediator.HandlerAbilityValidator.handlersMatchingRequest;

import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

abstract class AbstractHandlerProvider {

  protected static final int FIRST_ENTRY = 0;
  protected static final int EXPECTED_NON_EVENT_HANDLERS = 1;
  protected final Map<String, Provider<?>> handlerCache = new HashMap<>();

  protected <T> String getIdFor(T requestClass) {
    return UUID.nameUUIDFromBytes(requestClass.getClass().getName().getBytes()).toString();
  }

  protected <H extends RequestHandler<T, R>, T extends Request, R> List<H> findMatchingHandlers(Class<? extends Request> requestClass,
                                                                                                List<? extends RequestHandler<?, ?>> requestHandlers) {

    return (List<H>) requestHandlers.stream()
                                    .filter(handlersMatchingRequest(requestClass))
                                    .toList();
  }

  protected abstract <T extends RequestHandler<?, ?>> UnaryOperator<List<T>> validateResult(String requestName);

  protected void onNullThrowException(List<?> list, String requestName) {
    if (list == null) {
      throw new MediatorException(handlerNotFoundMessage(requestName));
    }
  }

  protected void onEmptyListThrowException(List<?> list, String requestName) {
    if (list.isEmpty()) {
      throw new MediatorException(handlerNotFoundMessage(requestName));
    }
  }

  protected void onListSizeMoreThanOneThrowException(List<?> list, String requestName) {
    if (list.size() > EXPECTED_NON_EVENT_HANDLERS) {
      throw new MediatorException(multipleHandlersRegisteredMessage(requestName));
    }
  }

}
