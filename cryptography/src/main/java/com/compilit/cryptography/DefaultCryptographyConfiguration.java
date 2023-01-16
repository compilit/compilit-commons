package com.compilit.cryptography;

import static com.compilit.cryptography.CryptographyDefaults.ITERATION_COUNT;
import static com.compilit.cryptography.CryptographyDefaults.KEY_LENGTH;

import com.compilit.core.api.cryptography.Cryptographer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultCryptographyConfiguration {

    /**
     * @return a default cryptographer implementation using a key-length of 256 and an iteration-count of 65536
     */
    @Bean
    Cryptographer cryptographer() {
        return new CryptographerService(KEY_LENGTH, ITERATION_COUNT);
    }
}
