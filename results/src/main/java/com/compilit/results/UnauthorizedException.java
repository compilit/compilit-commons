package com.compilit.results;

public class UnauthorizedException extends RuntimeException {

  UnauthorizedException(String message) {
    super(message);
  }
}
