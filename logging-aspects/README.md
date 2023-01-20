# AOP-logging package by Compilit

The first version of this package only adds a few simple annotations which, if you are using Spring AOP, will add a few
default or custom logging statements to each annotated method.
The necessary spring-boot-starter-aop dependency has scope "provided", since I want to make sure nobody actually blindly
takes in
this package without realizing you'd pull in the entire spring-boot-starter-aop dependency. So you are only able to use
this library of this dependency is already on your classpath.

# Installation

Get this dependency with the latest version

```xml

<dependency>
  <artifactId>domain-primitives</artifactId>
  <groupId>com.compilit</groupId>
</dependency>
```

# Usage

Annotate the desired method with the following annotations:

```java
import com.compilit.logging.api.Log;
import com.compilit.logging.api.LogAfter;
import com.compilit.logging.api.LogBefore;
import com.compilit.logging.api.LogOnException;

class Example {

  @Log //does everything the other annotations do combined
  @LogBefore //logs a message before the method is executed
  @LogAfter // logs a message after the method has executed successfully
  @LogOnException
    // logs a message if an exception occurred
  void someMethod() {
    (...)
  }
}
```

Each annotation provides a default message a log level, which can be altered as desired:

```java
import com.compilit.logging.api.LogAfter;
import com.compilit.logging.api.LogBefore;
import org.slf4j.event.Level;

class Example {

  @LogBefore(message = "This is a message which will be logged before method execution with log level WARN", level = Level.WARN)
  void someMethod() {
    (...)
  }
}
```

# Exception handling

The @LogOnException annotation will catch any uncaught exception thrown by your methods and log the desired, if you wish
to handle the exception yourself after logging the exception, you can add the "rethrow = true" params to the annotation:

```java
import com.compilit.logging.api.LogAfter;
import com.compilit.logging.api.LogBefore;

class Example {

  @LogOnException(message = "This message will be logged and then the exception will be rethrown", rethrow = true)
  void someMethod() {
    throw new RuntimeException();
  }
}
```
