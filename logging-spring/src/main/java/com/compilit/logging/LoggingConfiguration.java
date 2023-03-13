package com.compilit.logging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("compilit.logging")
class LoggingConfiguration {
  private boolean rethrowExceptions = true;

  public boolean isRethrowExceptions() {
    return rethrowExceptions;
  }

  public void setRethrowExceptions(boolean rethrowExceptions) {
    this.rethrowExceptions = rethrowExceptions;
  }
}
