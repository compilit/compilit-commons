package com.compilit.logging;

import com.compilit.logging.api.Log;
import com.compilit.logging.api.LogAfter;
import com.compilit.logging.api.LogArg;
import com.compilit.logging.api.LogBefore;
import com.compilit.logging.api.LogOnException;
import org.springframework.stereotype.Component;

@Component
class LogTestHelper {

  static final String MESSAGE = "Message";
  static final String ERROR_MESSAGE = "Error message";

  @Log(before = MESSAGE, after = MESSAGE)
  public void testBasicLogging() {
  }

  @LogBefore(message = MESSAGE)
  public void testBeforeLogging() {
  }

  @LogAfter(message = MESSAGE)
  public void testAfterLogging() {
  }

  @Log(before = MESSAGE, onException = ERROR_MESSAGE)
  public void testExceptionLogging() {
    throw new RuntimeException("Oeps");
  }

  @LogOnException(message = MESSAGE)
  public void testExceptionOnlyLogging() {
    throw new RuntimeException("Oeps");
  }

  @Log(before = MESSAGE, onException = ERROR_MESSAGE)
  public void testArgLogger(@LogArg(message = "Using arg: %s") String arg1, @LogArg(message = "Using arg: %s") String arg2) {
  }
}
