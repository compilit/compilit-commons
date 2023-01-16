package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;

final class CommandHandlerWrapper {

  private final CommandHandler<?, ?> handler;

  public <C extends Command<R>, R> CommandHandlerWrapper(CommandHandler<C, R> handler) {
    this.handler = handler;
  }

  public <C extends Command<R>, R> CommandHandler<C, R> provideHandler() {
    return (CommandHandler<C, R>) handler;
  }

}
