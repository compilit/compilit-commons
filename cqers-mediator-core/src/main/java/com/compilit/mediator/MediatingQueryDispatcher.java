package com.compilit.mediator;

import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryDispatcher;


final class MediatingQueryDispatcher implements QueryDispatcher {

  private final Mediator mediator;

  public MediatingQueryDispatcher(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public <T> T dispatch(Query<T> query) {
    return mediator.mediateQuery(query);
  }

}
