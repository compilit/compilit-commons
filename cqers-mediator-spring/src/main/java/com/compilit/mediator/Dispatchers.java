package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryDispatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Through these static methods you have direct access to the internal Command- and QueryDispatchers and the
 * EventEmitter. You can use them if you do not wish to inject the necessary dependencies.
 */
@Component
public class Dispatchers implements InitializingBean {

  private static Dispatchers instance;
  private final CommandDispatcher commandDispatcher;
  private final QueryDispatcher queryDispatcher;
  private final EventEmitter eventEmitter;

  Dispatchers(CommandDispatcher commandDispatcher,
              QueryDispatcher queryDispatcher,
              EventEmitter eventEmitter) {
    this.commandDispatcher = commandDispatcher;
    this.queryDispatcher = queryDispatcher;
    this.eventEmitter = eventEmitter;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    instance = this;
  }

  /**
   * Send the query into the mediator. If a matching handler is found, return the result of this handler.
   *
   * @param query the query you wish to resolve.
   * @param <T>   the specific type of query.
   * @param <R>   the return type
   * @return the return value in the form of R
   */
  public synchronized static <T extends Query<R>, R> R resolve(T query) {
    return instance.queryDispatcher.dispatch(query);
  }

  /**
   * Send the command into the mediator. If a matching handler is found, return the result of this handler.
   *
   * @param command the query you wish to resolve.
   * @param <T>     the specific type of command.
   * @param <R>     the return type
   * @return the return value in the form of R
   */
  public synchronized static <T extends Command<R>, R> R apply(T command) {
    return instance.commandDispatcher.dispatch(command);
  }

  /**
   * Send the event into the mediator.
   * If matching handlers are found, apply all these handlers to the event. In a way, all these handlers are
   * 'subscribed' to this event.
   *
   * @param event the event you wish to emit.
   * @param <T>   the specific type of event.
   */
  public synchronized static <T extends Event> void emit(T event) {
    instance.eventEmitter.emit(event);
  }

}
