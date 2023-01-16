package com.compilit.mediator.api;

/**
 * Commands are for all mutating requests. The return type should not come from a query, but instead
 * be some sort of result of the command.
 *
 * @param <T> The return type.
 */
public interface Command<T> extends Request {
}
