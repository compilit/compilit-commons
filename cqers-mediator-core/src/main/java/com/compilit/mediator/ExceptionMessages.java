package com.compilit.mediator;

final class ExceptionMessages {

  private ExceptionMessages() {
  }

  public static String handlerNotFoundMessage(String handlerName) {
    return String.format("Failed to find handler for %s.", handlerName);
  }

  public static String multipleHandlersRegisteredMessage(String requestName) {
    return String.format(
      "Multiple handlers for %s registered, only Events can have multiple handlers.",
      requestName
    );
  }

}
