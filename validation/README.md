# compilit-validation

A small package that enables validation of (business) rules through a fluent API.

# Installation

Get this dependency with the latest version.

```xml

<dependency>
    <artifactId>compilit-validation</artifactId>
    <groupId>com.compilit</groupId>
</dependency>
```

# Usage

The package offers a set of Predicates to define (business) rules which can then be validated through the Verifications
class. The idea is that a rule should be a separate entity that can either be part of an object or passed throughout
your application. An individual value or object can then be tested against these rules when needed. A Rule is in fact an
extension of the Predicate class. And a Rule.WithDualInput is an extension of the BiPredicate class. These extensions
allow you to add a message that is associated with the potential failure of this rule.

You first define a Rule using the Definitions entrypoint for the fluent API. Then, whenever needed, you test a value
against the rule using the Verifications entrypoint. The fluent API helps you define what needs to happen after
successful validation or after a failed validation.

Here are some examples:

```java
class Example {
  // Normally you would rarely use the 'validate()' method yourself. Instead, you should use the strength of the fluent API to determine what needs to be returned and when.

  Rule<String> rule = defineThatIt(contains("test")).otherwiseReport("It does not contain 'test'");
  boolean result = verifyThat("test").compliesWith(rule).validate(); //A basic Predicate validation.

  Rule.WithDualInput<String> rule = defineThatIt(isA(String.class).where((it, argument) -> it.equals(argument))).otherwiseReport("It does not equal given argument");
  boolean result = verifyThat("test").compliesWith(rule).whileApplying("some argument").validate(); //A basic BiPredicate validation, which is why the 'whileApplying' method is inserted.

}
```

Other examples of rule definitions:

```java
class Example {

  Rule<String> rule = defineThatIt(isEqualTo("test")).otherwiseReport("It does not equal to 'test'");

  Rule<String> rule = defineThatIt(hasALengthOf(4)).otherwiseReport("It does not have a length of '4'");

  Rule<String> rule = defineThatIt(isA(String.class).where(it -> it.contains("test"))).otherwiseReport("It does not contain 'test'");

  Rule<TestObject> rule = defineThatIt(isA(TestObject.class).where(it -> it.hasSomeProperty())).otherwiseReport("It doesn't have some property");

  Rule<TestObject> rule = defineThatIt(isA(TestObject.class).that(TestObject::isAwesome)).otherwiseReport("It is not awesome");

  Rule<String> rule = defineThatIt(isAlphabetic()).otherwiseReport("It is not alphabetic");

  Rule<Integer> rule = defineThatIt(isEqualTo(2)).otherwiseReport("It is not equal to '2'");

  Rule<Integer> rule = defineThatIt(isBetween(1).and(5)).otherwiseReport("It is not between '1' and '5'");

  Rule<Double> rule = defineThatIt(isBetween(.1).and(.5)).otherwiseReport("It is not between '.1' and '.5'");

  Rule<Integer> rule = defineThatIt(hasAmountOfDigits(10)).otherwiseReport("It does not have exactly '10' digits");

}
```

You can define what Exception should be thrown if a rule is broken:

```java
class Example {

  boolean result = verifyThat("test").compliesWith(rule).orElseThrow(message -> new RuntimeException(message));

}
```

You can state that the internal message should be logged. These messages will all be logged at Error level:

```java
class Example {

  boolean result = verifyThat("test").compliesWith(rule).orElseLogMessage();

  //if you wish to provide your own slf4j instance:
  boolean result = verifyThat("test").compliesWith(rule).orElseLogMessage(logger);

}
```

You can define what values should be returned if a rule is broken:

```java
class Example {

  String result = verifyThat("test").compliesWith(rule).orElseReturn("Some other String");

}
```

You can chain multiple rules for one value:

```java
class Example {

  boolean verify() {
    return verifyThat("test")
            .compliesWith(rule1)
            .and(rule2)
            .and(etc)
            .orElseThrow(message -> new RuntimeException(message));
  }
}
```

Finally, after validating, it's also possible to chain a process by passing a supplier. Because it returns an actual
value, the orElse clause is required to finish the statement:

```java
class Example {

  boolean verify() {
    return verifyThat("test")
            .compliesWith(rule)
            .andThen(() -> someAction())
            .orElseThrow(message -> new RuntimeException(message));
  }

}
```

Currently, the package offers string Predicates, number Predicates, decimal number predicates and custom object
predicates.

### Custom Predicate<T> implementations

Just to be clear, here is an example of how one would define custom, more complex Predicates about some object.

```java
class Example {

  private final Rule<TestObject> rule = defineThatIt(isA(CustomObject.class).that(hasSomeAmazingProperties())).otherwiseReport("It doesn't have some amazing properties");
  //or even simpler (but maybe not as clear), because in the end, it's just a Predicate or BiPredicate...
  private final Rule<TestObject> rule = defineThatIt(hasSomeAmazingProperties()).otherwiseReport("It doesn't have some amazing properties");

  private Predicate<CustomObject> hasSomeAmazingProperties() {
    return input -> {
      // (...some boolean returning validation about 'input'...)
    };
  }

  boolean verify() {
    return verifyThat(value)
            .compliesWith(rule)
            .andThen(() -> performThisAction())
            .orElseThrow(new Exception());
  }

}
```

### When validation takes place

It should be noted that no validation will be taking place until requested by the validate() or the orElse*() methods.
Requesting the message before validation has taken place will always result in a placeholder message like "Nothing to
report". If you would like to get the message of a validation. You could do it like this:

```java
class Example {

  String verify() {
    var validator = verifyThat(value).compliesWith(rule);
    validator.validate();
    return validator.getMessage();
  }

}
```

