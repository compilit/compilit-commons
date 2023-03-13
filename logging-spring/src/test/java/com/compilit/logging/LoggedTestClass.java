package com.compilit.logging;

import com.compilit.logging.api.Event;
import com.compilit.logging.api.Log;
import com.compilit.logging.api.Logged;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Component
class LoggedTestClass {

  static final String MESSAGE = "Message";
  static final String ERROR_MESSAGE = "Error message";

  @Logged(logs = {
    @Log(event = Event.ON_CALLED, message = MESSAGE)
  })
  public void testLogArguments(String arg1, int arg2) {
  }
  @Logged(logs = {
    @Log(event = Event.ON_STARTED, message = MESSAGE)
  })
  public void testLogBefore() {
  }

  @Logged(logs = {
    @Log(event = Event.ON_FINISHED, message = MESSAGE)
  })
  public void testLogAfter() {
  }

  @Logged(logs = {
    @Log(event = Event.ON_EXCEPTION, message = ERROR_MESSAGE)
  })
  public void testLogException() {
    throw new RuntimeException("Oeps");
  }

  @Logged(logs = {
    @Log(event = Event.ON_RESULT, message = MESSAGE)
  })
  public String testLogResult() {
    return MESSAGE;
  }

  @Logged(logs = {
    @Log(event = Event.ON_CALLED, message = MESSAGE),
    @Log(event = Event.ON_STARTED, message = MESSAGE),
    @Log(event = Event.ON_FINISHED, message = MESSAGE),
    @Log(event = Event.ON_RESULT, message = MESSAGE),
    @Log(event = Event.ON_EXCEPTION, message = ERROR_MESSAGE)
  })
  public String testLogAll(String arg1, String arg2) {
    return MESSAGE;
  }

  @Logged(logs = {
    @Log(event = Event.ON_CALLED, level = Level.DEBUG),
    @Log(event = Event.ON_STARTED),
    @Log(event = Event.ON_FINISHED),
    @Log(event = Event.ON_RESULT),
    @Log(event = Event.ON_EXCEPTION)
  })
  public String testLogAllWithDefaultMessages(String arg1, String arg2) {
    return MESSAGE;
  }
}
