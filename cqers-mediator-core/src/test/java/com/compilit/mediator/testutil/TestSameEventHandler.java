package com.compilit.mediator.testutil;

import com.compilit.mediator.api.EventHandler;

public class TestSameEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent command) {
    SideEffectContext.invoke();
    return null;
  }
}
