package com.compilit.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import com.compilit.cryptography.api.KeyLength;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CryptographerServiceTest {

  private static final String secret = RandomStringUtils.randomAscii(32);

  public static Stream<Arguments> testCases() {
    return Stream.of(
      Arguments.arguments(new CryptographerConfiguration(
        secret,
        KeyLength.BITS_128,
        1000
      )),
      Arguments.arguments(new CryptographerConfiguration(
        secret,
        KeyLength.BITS_192,
        1000
      )),
      Arguments.arguments(new CryptographerConfiguration(
        secret,
        KeyLength.BITS_256,
        1000
      ))
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void encryptAndDecrypt_shouldEncryptAndDecryptValue(CryptographerConfiguration cryptographerConfiguration) {
    var cryptographerService = new CryptographerService(cryptographerConfiguration);
    String valueToEncrypt = "IamSomeSortOfSecretApparently";
    byte[] encryptedValue = cryptographerService.encrypt(valueToEncrypt);
    assertThat(valueToEncrypt.getBytes()).isNotEqualTo(encryptedValue);
    String decryptedValue = cryptographerService.decrypt(encryptedValue);
    assertThat(valueToEncrypt).isEqualTo(decryptedValue);
  }

}