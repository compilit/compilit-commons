package com.compilit.mediator.api;

/**
 * Commands are for all mutating requests. A SimpleCommand could be considered a 'fire and forget'
 * kind of request.
 */
public interface SimpleCommand extends Command<Void> {
}