package com.compilit.mediator.testutil;

import com.compilit.mediator.api.EventHandler;


public class TestEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent event) {
    SideEffectContext.invoke();
    return null;
  }
}
