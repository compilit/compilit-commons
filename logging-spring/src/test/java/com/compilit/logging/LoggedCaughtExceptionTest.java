package com.compilit.logging;

import static com.compilit.logging.LoggedTestClass.ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test2")
@EnableAspectJAutoProxy
@SpringBootTest
public class LoggedCaughtExceptionTest extends AbstractLoggedTest {
  @Test
  void log_rethrownException_shouldLogExceptionAndRethrowException() {
    testClass.testLogException();
    assertThat(memoryAppender.contains(ERROR_MESSAGE, Level.ERROR)).isTrue();
    assertThat(memoryAppender.countEventsForLogger(logger.getName())).isEqualTo(1);
  }
}
