package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryHandler;

final class QueryHandlerWrapper {

  private final QueryHandler<?, ?> handler;

  public <R> QueryHandlerWrapper(QueryHandler<? extends Query<R>, R> handler) {
    this.handler = handler;
  }

  public <C extends Query<R>, R> QueryHandler<C, R> provideHandler() {
    return (QueryHandler<C, R>) handler;
  }

}
