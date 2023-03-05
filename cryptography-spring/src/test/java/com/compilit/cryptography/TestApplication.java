package com.compilit.cryptography;

import com.compilit.cryptography.api.KeyLength;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableCryptography
@SpringBootApplication
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class);
  }

  @Bean
  EncryptionTestHelper createEncryptionTestHelper() {
    return new EncryptionTestHelper();
  }

  @Bean
  CryptographerConfiguration cryptographerConfiguration() {
    return new CryptographerConfiguration("IAMTHESECRET", KeyLength.BITS_192, 1000);
  }

}
