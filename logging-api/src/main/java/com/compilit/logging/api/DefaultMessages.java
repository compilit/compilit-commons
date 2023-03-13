package com.compilit.logging.api;

final class DefaultMessages {

  static final String BEFORE_EXECUTION_MESSAGE = "Starting method {}";
  static final String AFTER_EXECUTION_MESSAGE = "Finished method {}";
  static final String ON_EXCEPTION_MESSAGE = "Method {} threw an exception: %s";
  static final String ON_CALL_MESSAGE = "Calling method {} with args:";
  static final String ON_RESULT_MESSAGE = "Result of method {}: {}";
  private DefaultMessages() {}
}
