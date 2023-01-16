package com.compilit.results;

import java.util.Optional;

abstract class AbstractResult<T> implements Result<T> {

  private final ResultStatus resultStatus;
  private final T contents;
  private final String message;

  AbstractResult(ResultStatus resultStatus) {
    this.resultStatus = resultStatus;
    this.contents = null;
    this.message = Messages.NOTHING_TO_REPORT;
  }

  AbstractResult(ResultStatus resultStatus, String message) {
    this.resultStatus = resultStatus;
    this.contents = null;
    this.message = message;
  }

  AbstractResult(ResultStatus resultStatus, T contents) {
    this.resultStatus = resultStatus;
    this.contents = contents;
    this.message = Messages.NOTHING_TO_REPORT;
  }

  @Override
  public ResultStatus getResultStatus() {
    return resultStatus;
  }

  @Override
  public Optional<T> getContents() {
    return Optional.ofNullable(contents);
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Result) {
      var result = (Result) obj;
      return resultStatus.equals(result.getResultStatus())
          && getMessage().equals(result.getMessage())
          && getContents().equals(getContents());
    }
    return false;
  }

}
