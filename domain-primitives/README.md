# compilit-domain-primitives

A simple package containing a base class that enables you to create domain primitives.
It enables you to avoid the use of possibly unvalidated primitive values in your domain entities. Essentially a domain
primitive cannot exist if its related business logic fails.
Something like this should be the heart of every domain entity out there.

# Installation

Get this dependency with the latest version

```xml
    <dependency>
      <artifactId>compilit-domain-primitives</artifactId>
      <groupId>com.compilit</groupId>
    </dependency>
```

# Usage

To best explain the usage of a valid domain primitive I'll create a little user story.

- As a librarian
- I want books to all have a book-id in the BookId* format
- So that I know that I never get any invalid book-id's in my library

*BookId format: {10-digit-number}:{5-letter-code}

Let's first write some tests:

```java
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
```

And now I'll implement this domain primitive:

```java
public class BookId extends DomainPrimitive<String> {

  public BookId(String value) {
    super(value, BookId.class.getSimpleName());
  }

  @Override
  protected boolean isValid(String value) {
    if (Objects.isNull(value) || value.isBlank())
      return false;  
    var substrings = value.split(":");
    var digits = substrings[0];
    var letters = substrings[1];
    return digits.length() == 10
        && letters.length() == 5
        && digits.chars().allMatch(Character::isDigit)
        && letters.chars().allMatch(Character::isAlphabetic);
  }

}
```

### Tip

The compilit-validation package works really nice to define and validate the business rules related to domain
primitives. Here is a little example based on the previous one:

```java
public class BookId extends DomainPrimitive<String> {

    private final Rule<String> bookIdBaseRule = defineThatIt(isAStringWithLength(16).containing(":"));
    private final Rule<String> bookIdFirstPartRule = defineThatIt(isNumeric(10));
    private final Rule<String> bookIdSecondPartRule = defineThatIt(isAlphabetic(5));

    public BookId(String value) {
        super(value, BookId.class.getSimpleName());
    }

    @Override
    protected boolean isValid(String value) {
        return verifyThat(value)
                .compliesWith(bookIdBaseRule)
                .compliesWith(bookIdFirstPartRule)
                .compliesWith(bookIdSecondPartRule)
                .orElseThrow(InValidBookIdException::new);
    }

}
```

