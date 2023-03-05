package com.compilit.mediator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.Query;
import com.compilit.mediator.api.QueryDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DispatchersTest {

  private static final CommandDispatcher commandDispatcher = Mockito.mock(CommandDispatcher.class);
  private static final QueryDispatcher queryDispatcher = Mockito.mock(QueryDispatcher.class);
  private static final EventEmitter eventEmitter = Mockito.mock(EventEmitter.class);
  private static final Dispatchers dispatchers = new Dispatchers(commandDispatcher, queryDispatcher, eventEmitter);

  @BeforeAll
  static void setup() throws Exception {
    dispatchers.afterPropertiesSet();
  }

  @Test
  void apply_shouldAccessStaticInstance() {
    Dispatchers.apply(new Command<Object>() {});
    verify(commandDispatcher).dispatch(any());
  }

  @Test
  void resolve_shouldAccessStaticInstance() {
    Dispatchers.resolve(new Query<Object>() {});
    verify(queryDispatcher).dispatch(any());
  }

  @Test
  void emit_shouldAccessStaticInstance() {
    Dispatchers.emit(new Event() {});
    verify(eventEmitter).emit(any());
  }
}
