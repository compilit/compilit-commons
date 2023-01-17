package com.compilit.mediator.api;

/**
 * Queries are for all read-only requests. Mutating of resources should not be taking place through a Query.
 *
 * @param <T> The return type.
 */
public interface Query<T> extends Request {
}
