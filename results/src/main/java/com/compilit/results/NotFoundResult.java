package com.compilit.results;

class NotFoundResult<T> extends AbstractResult<T> {

  NotFoundResult() {
    super(ResultStatus.NOT_FOUND);
  }

  NotFoundResult(String message) {
    super(ResultStatus.NOT_FOUND, message);
  }

}

