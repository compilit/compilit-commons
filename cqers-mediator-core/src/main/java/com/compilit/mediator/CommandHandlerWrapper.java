package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandHandler;

final class CommandHandlerWrapper<C extends Command<R>, R> implements Provider<CommandHandler<C, R>> {

  private final CommandHandler<?, ?> handler;

  public CommandHandlerWrapper(CommandHandler<C, R> handler) {
    this.handler = handler;
  }

  @Override
  public CommandHandler<C, R> provide() {
    return (CommandHandler<C, R>) handler;
  }
}
