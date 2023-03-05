package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;
import com.compilit.mediator.api.RequestHandler;
import java.util.List;
import java.util.function.UnaryOperator;


final class QueryHandlerProvider extends AbstractHandlerProvider {

  private final List<? extends QueryHandler<?, ?>> queryHandlers;

  QueryHandlerProvider(List<? extends QueryHandler<?, ?>> queryHandlers) {
    super();
    this.queryHandlers = queryHandlers;
  }

  @Override
  protected <T extends RequestHandler<?, ?>> UnaryOperator<List<T>> validateResult(String requestName) {
    return list -> {
      onNullThrowException(list, requestName);
      onEmptyListThrowException(list, requestName);
      onListSizeMoreThanOneThrowException(list, requestName);
      return list;
    };
  }

  <R> QueryHandler<Query<R>, R> getQueryHandler(Query<R> query) {
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
    List<QueryHandler<T, R>> handlers = findMatchingHandlers(
      requestClass,
      queryHandlers
    );
    return this.<QueryHandler<T, R>>validateResult(requestName).apply(handlers).get(FIRST_ENTRY);
  }
}
