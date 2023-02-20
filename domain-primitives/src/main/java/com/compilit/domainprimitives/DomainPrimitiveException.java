package com.compilit.domainprimitives;

public final class DomainPrimitiveException extends RuntimeException {

  <T> DomainPrimitiveException(String domainPrimitiveName, T value) {
    super(String.format("Provided value '%s' is not a valid %s", value, domainPrimitiveName));
  }

  <T> DomainPrimitiveException(String domainPrimitiveName, T value, String message) {
    super(String.format("Provided value '%s' is not a valid %s: %s", value, domainPrimitiveName, message));
  }

}
