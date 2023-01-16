package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.support.GenericApplicationContext;


final class QueryHandlerProvider extends AbstractHandlerProvider {

  private final Map<String, QueryHandlerWrapper> queryHandlerMap = new HashMap<>();

  public QueryHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
  }

  public <R> QueryHandler<Query<R>, R> getQueryHandler(Query<R> requestClass) {
    try {
      var hash = getHashFor(requestClass);
      if (!queryHandlerMap.containsKey(hash)) {
        var handler = findQueryHandler(requestClass);
        queryHandlerMap.put(hash, new QueryHandlerWrapper(handler));
      }
      return queryHandlerMap.get(hash).provideHandler();
    } catch (Exception exception) {
      throw new MediatorException(exception.getMessage());
    }
  }

  private <T extends Query<R>, R> QueryHandler<T, R> findQueryHandler(T requestClass) {
    var atomicReference = new AtomicReference<QueryHandler<T, R>>();
    setAtomicReference(atomicReference, QueryHandler.class, requestClass);
    makeSureAtomicReferenceIsNotNull(atomicReference, requestClass.getClass().getName());
    return atomicReference.get();
  }

}
