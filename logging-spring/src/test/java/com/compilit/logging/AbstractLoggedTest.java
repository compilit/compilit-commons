package com.compilit.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
abstract class AbstractLoggedTest {

  protected final Logger logger = (Logger) LoggerFactory.getLogger(LoggedTestClass.class);
  @Autowired
  LoggedTestClass testClass;
  protected MemoryAppender memoryAppender;

  @BeforeEach
  void setup() {
    memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(Level.DEBUG);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
  }
}
