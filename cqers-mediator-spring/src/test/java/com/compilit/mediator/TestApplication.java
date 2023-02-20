package com.compilit.mediator;

import com.compilit.mediator.api.CommandHandler;
import com.compilit.mediator.testutil.TestCommand;
import com.compilit.mediator.testutil.TestCommandHandler;
import com.compilit.mediator.testutil.TestObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.compilit.mediator.testutil")
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class);
  }

  @Bean
  CommandHandler<TestCommand, TestObject> createTestCommandHandler() {
    return new TestCommandHandler();
  }
}
