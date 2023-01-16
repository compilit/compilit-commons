# compilit-spring-mediator

An implementation of the Mediator/CQERS pattern using Spring.

The purpose of this library is to take away all the boilerplate code that is connected to
implementing this pattern. This library works to achieve several goals:

1. Make your application as loosely coupled as possible.
2. Enforce CQRS.

It does this by providing a simple API consisting of a CommandDispatcher, a QueryDispatcher and an
EventEmitter. These are the only dependencies you'll ever need to inject in any of your
classes/services that wish to interact with others. The internal Mediator will handle all of this
interaction. The interaction takes place through the respecitive Commands Queries and Events which
are internally connected to their CommandHandler, QueryHandler and EventHandler counterparts.

This implementation was inspired by <a href="https://github.com/jkratz55/spring-mediatR">Spring
MediatR</a>
and <a href=https://github.com/jbogard/MediatR>MediatR for .NET</a>.

I do not claim this is a better implementation than the aforementioned JVM implementation. Only the
initial configuration is a bit easier/less verbose.

# Installation

First make sure you are in fact using Spring as your IOC supplier. This project depends on a provided spring-context dependency
Get this dependency with the latest version.

```xml

<dependency>
  <artifactId>compilit-spring-mediator</artifactId>
  <groupId>com.compilit</groupId>
</dependency>
```

Then add an import annotation to your project by annotating a managed bean class with @Import(
MediatorConfiguration.class).

```Java

import com.compilit.mediator.MediatorConfiguration;

@Import(MediatorConfiguration.class)
@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }

}
```

Now all you need to do is register QueryHandler, CommandHandler and/or EventHandler implementations.

# Usage

The Mediator pattern is about making your application loosely
coupled. <a href="https://www.compilit.com/definitions/cqers/">CQRS</a> (or in this case CQERS) is
meant to make the
application robust and predictable by separating reading operations from writing operations.
Combining them is quite a
popular approach. The idea is that a Mediator is in between all requests (requests can be read or
write requests), so
that is no direct interaction with resources. This means that there are only 3 specific dependencies
which connect your
api layer to the domain layer: the CommandDispatcher, the QueryDispatcher and the EventEmitter. Why
3 instead of just
1 'Mediator' class? Because that would introduce the 'Service Locator antipattern' and defeat the
purpose of this
library. By having a separate interface for Commands, Queries and Events, CQ(E)RS is enforced.

In the com.compilit.mediator.api package you'll find all interfaces which you can use to write your
own Commands,
Queries, Events and their respective handlers.

All components which a user of the API can to interact with:

### Command-related

- <b>Command:</b> a writing operation which is handled by a single handler. It provides a return
  value option to return
  an Id of a created entity for example. Or you could return
  a <a href="https://github.com/compilit/compilit-results">Result</a>. This return value should
  never be
  filled by a reading operation.
- <b>CommandHandler:</b> the handler for a specific Command.
- <b>CommandDispatcher:</b> the main interactor for dispatching Commands.

### Query-related

- <b>Query:</b> a reading operation which is handled by a single handler.
- <b>QueryHandler:</b> the handler for a specific Query.
- <b>QueryDispatcher:</b> the main interactor for dispatching Queries.

### Event-related

- <b>Event:</b> something that has happened which other operations (EventHandlers) can subscribe to.
  Can be handled by multiple
  EventHandlers.
- <b>EventHandler:</b> the handler for a specific Event.
- <b>EventEmitter:</b> the main interactor for emitting Events.

Here is an example:

```java
import QueryDispatcher;

public class TestQuery implements Query<String> {

  private final String someData;

  public TestQuery(String someData) {
    this.someData = someData;
  }

  public String getData() {
    return someData;
  }
}


public class TestQueryHandler implements QueryHandler<TestQuery, String> {

  //This class could interact with other systems/clients/hibernate etc.
  @Override
  public String handle(TestQuery query) {
    return query.getData();
  }

}

@RestController
public class ExampleController {

  private final QueryDispatcher queryDispatcher;

  public ExampleController(QueryDispatcher queryDispatcher) {
    this.queryDispatcher = queryDispatcher;
  }

  @GetMapping("/some-example")
  public ResponseEntity<String> interact() {
    return queryDispatcher.dispatch(new TestQuery());
  }

}
```

### For those paying attention

If you've looked at my implementation you might have noticed that there is no difference in
behaviour between the
CommandHandlers and the QueryHandlers. Both have the ability to return values. It is true that
returning a value from a
Command enables the user of this library to still break with CQRS and perform reading operations
inside CommandHandlers.
This, however, is always possible. I considered this and decided that it was more important to
provide an API that is
consistent with other frameworks and libraries. Most writing operations return either the written
object, the ID of the
written object or some other kind of result. The whole point of having a separate Query and Command
class is for reading
purposes only. This way it should be clear to the reader that some operation is only about reading
or about writing.
