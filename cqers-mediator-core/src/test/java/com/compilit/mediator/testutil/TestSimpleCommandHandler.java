package com.compilit.mediator.testutil;

import com.compilit.mediator.api.CommandHandler;

public class TestSimpleCommandHandler implements CommandHandler<TestSimpleCommand, Void> {

  @Override
  public Void handle(TestSimpleCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}