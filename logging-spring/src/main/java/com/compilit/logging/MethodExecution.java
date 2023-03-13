package com.compilit.logging;

class MethodExecution {

  private Object executionResult;
  private Throwable exception;

  public Object getExecutionResult() {
    return executionResult;
  }

  public void setExecutionResult(Object executionResult) {
    this.executionResult = executionResult;
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }

  public boolean isExceptionThrown() {
    return getException() != null;
  }
}
