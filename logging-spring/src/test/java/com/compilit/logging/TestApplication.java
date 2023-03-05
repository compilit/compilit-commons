package com.compilit.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.compilit.logging")
class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class);
  }

}
