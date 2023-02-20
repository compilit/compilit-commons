package com.compilit.mediator;

import static org.assertj.core.api.Assertions.*;
import static com.compilit.mediator.HandlerAbilityValidator.handlersMatchingRequest;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;
import com.compilit.mediator.testutil.TestCommand;
import com.compilit.mediator.testutil.TestCommandHandler;
import com.compilit.mediator.testutil.TestEvent;
import com.compilit.mediator.testutil.TestEventHandler;
import com.compilit.mediator.testutil.TestQuery;
import com.compilit.mediator.testutil.TestQueryHandler;

class HandlerAbilityValidatorTest {

  public static Stream<Arguments> validTestCases() {
    return Stream.of(
      Arguments.arguments(new TestQuery(), new TestQueryHandler()),
      Arguments.arguments(new TestCommand(), new TestCommandHandler()),
      Arguments.arguments(new TestEvent(), new TestEventHandler())
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      Arguments.arguments(new TestQuery(), new TestCommandHandler()),
      Arguments.arguments(new TestCommand(), new TestQueryHandler()),
      Arguments.arguments(new TestEvent(), new TestCommandHandler())
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void handlersMatchingRequest_validMatch_shouldReturnTrue(Request request, RequestHandler requestHandler) {
    assertThat(handlersMatchingRequest(request.getClass()).test(requestHandler)).isTrue();
  }
  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void handlersMatchingRequest_invalidMatch_shouldReturnFalse(Request request, RequestHandler requestHandler) {
    assertThat(handlersMatchingRequest(request.getClass()).test(requestHandler)).isFalse();
  }
}