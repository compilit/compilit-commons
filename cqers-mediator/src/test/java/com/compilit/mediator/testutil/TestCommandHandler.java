package com.compilit.mediator.testutil;

import com.compilit.mediator.api.CommandHandler;

public class TestCommandHandler implements CommandHandler<TestCommand, TestObject> {

  @Override
  public TestObject handle(TestCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}
