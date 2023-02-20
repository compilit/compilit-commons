package com.compilit.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import com.compilit.cryptography.api.Cryptographer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class CryptographerServiceTest {

  private final String secret = RandomStringUtils.randomAscii(32);
  private final Cryptographer cryptographerService = new DefaultCryptographyConfiguration().cryptographer();

  @Test
  void encryptAndDecrypt_shouldEncryptAndDecryptValue() {
    String valueToEncrypt = "IamSomeSortOfSecretApparently";
    byte[] encryptedValue = cryptographerService.encrypt(valueToEncrypt, secret);
    assertThat(valueToEncrypt.getBytes()).isNotEqualTo(encryptedValue);
    String decryptedValue = cryptographerService.decrypt(encryptedValue, secret);
    assertThat(valueToEncrypt).isEqualTo(decryptedValue);
  }

}