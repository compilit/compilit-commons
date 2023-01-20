package com.compilit.mediator.api;

/**
 * A CommandHandler is a managed bean that is dedicated to handling a specific Command, represented by T.
 *
 * @param <T> The specific Command implementation.
 * @param <R> The return type of the Command.
 */
public interface CommandHandler<T extends Command<R>, R> extends RequestHandler<T, R> {

  R handle(T command);

}
