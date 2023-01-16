package com.compilit.domainprimitives;

import com.compilit.domainprimitives.testutil.BookId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BookIdTest {

  @ParameterizedTest
  @ValueSource(strings = {
      "1234567890:abcde",
      "9999999999:defga",
      "0000000000:pihsa"
  })
  void ctor_validBookId_shouldNotThrowException(String value) {
    Assertions.assertThatNoException().isThrownBy(() -> new BookId(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "123456789:abcde",
      "someweirdId:123455",
      ""
  })
  void ctor_invalidBookId_shouldThrowException(String value) {
    Assertions.assertThatThrownBy(() -> new BookId(value))
              .isInstanceOf(DomainPrimitiveException.class);
  }

}
