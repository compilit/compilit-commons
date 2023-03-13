package com.compilit.logging.api;

public enum Event {
  ON_CALLED(DefaultMessages.ON_CALL_MESSAGE),
  ON_STARTED(DefaultMessages.BEFORE_EXECUTION_MESSAGE),
  ON_FINISHED(DefaultMessages.AFTER_EXECUTION_MESSAGE),
  ON_RESULT(DefaultMessages.ON_RESULT_MESSAGE),
  ON_EXCEPTION(DefaultMessages.ON_EXCEPTION_MESSAGE);
  private final String defaultMessage;
  Event(String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }
}
