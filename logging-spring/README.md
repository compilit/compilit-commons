# AOP-logging package

The first version of this package only adds a few simple annotations which, if you are using Spring AOP, will add a few
default or custom logging statements to each annotated method.
The necessary spring-boot-starter-aop dependency has scope "provided", since I want to make sure nobody actually blindly
takes in this package without realizing you'd pull in the entire spring-boot-starter-aop dependency. So you are only
able to use this library of this dependency is already on your classpath.

# Installation

Get this dependency with the latest version

```xml

<dependency>
  <artifactId>logging-spring</artifactId>
  <groupId>com.compilit</groupId>
</dependency>
```

# Usage

Annotate the desired method with the @Logged annotation. For every @Log annotation in the logs array of the @Logged
annotation, a log message will be written according to the arguments. There are 5 event hooks to choose from:

- Event.ON_CALL: will log all method arguments
- Event.ON_STARTED: will log right before starting the method
- Event.ON_FINISHED: will log right after finishing the method
- Event.ON_RESULT: will log the optional result of the method
- Event.ON_EXCEPTION: will log the optional exception

The rethrow flag in the @Logged annotation indicates whether any caught exception should be rethrown.

```java
import api.com.compilit.logging.Log;
import api.com.compilit.logging.LogAfter;
import api.com.compilit.logging.LogBefore;
import api.com.compilit.logging.Log;
import com.compilit.logging.api.Log;
import com.compilit.logging.api.LogOnException;
import org.slf4j.event.Level;

class Example {

  @Logged(logs = {
    @Log(event = Event.ON_CALL),
    @Log(event = Event.ON_STARTED),
    @Log(event = Event.ON_FINISHED),
    @Log(event = Event.ON_RESULT),
    @Log(event = Event.ON_EXCEPTION)
  },
    rethrow = true
  )
  void someMethod() {
    //(...)
  }
}
```

Each annotation provides a default message and a log level, which can be altered as desired:

```java

import api.com.compilit.logging.LogBefore;
import org.slf4j.event.Level;

class Example {

  @LogBefore(message = "This is a message which will be logged before method execution with log level WARN", level = Level.WARN)
  void someMethod() {
    //(...)
  }
}
```

# Exception handling

This library also essentially handles your exceptions automatically if you like it to. By default, all exceptions will be rethrown.
If, however, you like to consider your exceptions "handled" after logging, you could add an entry to you application
configuration like this:

```yaml
compilit:
  logging:
    rethrow-exceptions: false
```
