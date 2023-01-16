package com.compilit.mediator.testutil;

import com.compilit.mediator.api.QueryHandler;

public class TestSameQueryHandler implements QueryHandler<TestQuery, TestObject> {

  @Override
  public TestObject handle(TestQuery query) {
    SideEffectContext.invoke();
    return null;
  }

}
