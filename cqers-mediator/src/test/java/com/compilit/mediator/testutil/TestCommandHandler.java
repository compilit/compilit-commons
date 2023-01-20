package com.compilit.mediator.testutil;

import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.api.annotations.Emittable;
import com.compilit.mediator.api.annotations.OnSuccess;

@Emittable
public class TestCommandHandler implements CommandHandler<TestCommand, TestObject> {

  @Override
  @OnSuccess(emit = TestEvent.class)
  public TestObject handle(TestCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}
