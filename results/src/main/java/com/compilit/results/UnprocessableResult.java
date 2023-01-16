package com.compilit.results;

class UnprocessableResult<T> extends AbstractResult<T> {

  UnprocessableResult() {
    super(ResultStatus.UNPROCESSABLE);
  }

  UnprocessableResult(String message) {
    super(ResultStatus.UNPROCESSABLE, message);
  }

}
