package com.compilit.logging;

import static com.compilit.logging.ClassUnderTest.ERROR_MESSAGE;
import static com.compilit.logging.ClassUnderTest.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootTest
public class LogTest {

  private final Logger logger = (Logger) LoggerFactory.getLogger(ClassUnderTest.class);
  @Autowired
  ClassUnderTest testClass;
  private MemoryAppender memoryAppender;

  @BeforeEach
  void setup() {
    memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(Level.DEBUG);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
  }

  @Test
  void log_all_shouldLogAllPoints() {
    testClass.testBasicLogging();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(2);
  }

  @Test
  void log_exception_shouldLogException() {
    testClass.testExceptionLogging();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.contains(ERROR_MESSAGE, Level.ERROR)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(2);
  }

  @Test
  void log_before_shouldLogBeforeExecution() {
    testClass.testBeforeLogging();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }

  @Test
  void log_after_shouldLogAfterExecution() {
    testClass.testAfterLogging();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }
}
