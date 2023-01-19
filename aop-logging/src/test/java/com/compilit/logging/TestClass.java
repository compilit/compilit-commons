package com.compilit.logging;

import org.springframework.stereotype.Component;

@Component
public class TestClass {

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

}
