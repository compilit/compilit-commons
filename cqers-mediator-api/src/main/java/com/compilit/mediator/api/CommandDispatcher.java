package com.compilit.mediator.api;

/**
 * A CommandDispatcher is a dedicated interface for sending Commands to the Mediator. A Mediator should never be
 * interacted with directly because you could never truly know that your code complies with CQERS.
 *
 * @see Command
 */
public interface CommandDispatcher {

  /**
   * Send the command into the mediator. If a matching handler is found, return the result of this handler.
   *
   * @param command The specific Command you wish to send to the Mediator.
   * @param <T>     The return type of the Command.
   * @return The return type value.
   */
  <T> T dispatch(Command<T> command);

  /**
   * Dispatch multiple SimpleCommands to the Mediator in order to handle it. Since you cannot state which command needs
   * to return what, only SimpleCommands can be used because they return a void.
   *
   * @param command  The specific Command you wish to send to the Mediator.
   * @param commands The extra Commands you wish to send to the Mediator.
   */
  void dispatch(SimpleCommand command, SimpleCommand... commands);
}
