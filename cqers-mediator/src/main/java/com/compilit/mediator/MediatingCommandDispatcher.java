package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.SimpleCommand;


final class MediatingCommandDispatcher implements CommandDispatcher {

  private final Mediator mediator;

  public MediatingCommandDispatcher(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public <T> T dispatch(Command<T> command) {
    return mediator.mediateCommand(command);
  }

  @Override
  public void dispatch(SimpleCommand command, SimpleCommand... commands) {
    mediator.mediateCommand(command);
    if (commands != null && commands.length > 0) {
      for (var c : commands) {
        mediator.mediateCommand(c);
      }
    }
  }

}
