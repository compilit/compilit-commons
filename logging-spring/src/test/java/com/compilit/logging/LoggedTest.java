package com.compilit.logging;

import static com.compilit.logging.LoggedTestClass.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootTest
class LoggedTest extends AbstractLoggedTest {


  @Test
  void log_all_shouldLogAllPoints() {
    testClass.testLogAll("test1", "test2");
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(4);
  }

  @Test
  void log_before_shouldLogBeforeExecution() {
    testClass.testLogBefore();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }

  @Test
  void log_after_shouldLogAfterExecution() {
    testClass.testLogAfter();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }

  @Test
  void log_arguments_shouldLogArguments() {
    testClass.testLogArguments("test1", 1);
    assertThat(memoryAppender.contains(x -> x.contains("test1"), Level.INFO)).isTrue();
    assertThat(memoryAppender.contains(x -> x.contains("1"), Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }

  @Test
  void log_result_shouldLogMethodResult() {
    testClass.testLogResult();
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }

  @Test
  void log_allWithDefaultMessages_shouldLogMethodResult() {
    testClass.testLogAllWithDefaultMessages("test1", "test2");
    assertThat(memoryAppender.contains(MESSAGE, Level.INFO)).isTrue();
    assertThat(memoryAppender.contains(MESSAGE, Level.DEBUG)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(4);
  }
}
