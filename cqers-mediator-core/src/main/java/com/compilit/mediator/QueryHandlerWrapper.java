package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;

final class QueryHandlerWrapper<C extends Query<R>, R> implements Provider<QueryHandler<C, R>> {

  private final QueryHandler<?, ?> handler;

  public QueryHandlerWrapper(QueryHandler<? extends Query<R>, R> handler) {
    this.handler = handler;
  }

  @Override
  public QueryHandler<C, R> provide() {
    return (QueryHandler<C, R>) handler;
  }
}
