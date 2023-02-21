package com.compilit.cryptography;

import com.compilit.cryptography.api.Cryptographer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptographyConfiguration {

  /**
   * @return a cryptographer implementation using the registered CryptographerConfiguration
   */
  @Bean
  Cryptographer defaultCryptographer(CryptographerConfiguration cryptographerConfiguration) {
    return new CryptographerService(cryptographerConfiguration);
  }

}
