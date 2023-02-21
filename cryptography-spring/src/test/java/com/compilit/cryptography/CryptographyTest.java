package com.compilit.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = {TestApplication.class, CryptographyConfiguration.class})
public class CryptographyTest {

  @Autowired
  ClassUnderTest testClass;

  @Autowired
  ApplicationContext applicationContext;

  @Test
  void encryptAndDecrypt_shouldReturnExpectedValues() {
    String valueToEncrypt = "IamSomeSortOfSecretApparently";
    Object encryptedValue = testClass.encrypt(valueToEncrypt);
    assertThat(valueToEncrypt).isNotEqualTo(encryptedValue);
    Object decryptedValue = testClass.decrypt(encryptedValue);
    assertThat(valueToEncrypt).isEqualTo(decryptedValue);
  }

}
