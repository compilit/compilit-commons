package com.compilit.mediator;

import com.compilit.mediator.api.CommandDispatcher;
import com.compilit.mediator.testutil.TestCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MediatorConfiguration.class)
@SpringBootTest
public class MediatorComponentTest {

  @Autowired
  CommandDispatcher commandDispatcher;

  @Test
  void emit_annotationBased_shouldBeHandledByAnnotationBasedEventHandler() {

    commandDispatcher.dispatch(new TestCommand());
  }
}
