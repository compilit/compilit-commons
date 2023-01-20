package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;
import java.util.List;
import org.springframework.context.support.GenericApplicationContext;


final class QueryHandlerProvider extends AbstractHandlerProvider {

  public QueryHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
  }

  public <R> QueryHandler<Query<R>, R> getQueryHandler(Query<R> query) {
    var id = getIdFor(query);
    if (!handlerCache.containsKey(id)) {
      var handler = findQueryHandler(query);
      handlerCache.put(id, new QueryHandlerWrapper<>(handler));
    }
    return (QueryHandler<Query<R>, R>) handlerCache.get(id).provide();
  }

  private <T extends Query<R>, R> QueryHandler<T, R> findQueryHandler(T query) {
    var requestClass = query.getClass();
    var requestName = requestClass.getName();
    var handlerClass = QueryHandler.class;
    List<QueryHandler> handlers = (List<QueryHandler>) findMatchingHandlers(
      requestClass,
      handlerClass
    );
    assertValidResult(handlers, requestName);
    return handlers.get(FIRST_ENTRY);
  }

}
