package com.compilit.cryptography;

import com.compilit.cryptography.api.Cryptographer;
import com.compilit.cryptography.api.CryptographyDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptographyConfiguration {

  /**
   * @return a default cryptographer implementation using a key-length of 256 and an iteration-count of 65536
   */
  @Bean
  Cryptographer cryptographer() {
    return new CryptographerService(CryptographyDefaults.KEY_LENGTH, CryptographyDefaults.ITERATION_COUNT);
  }
}
