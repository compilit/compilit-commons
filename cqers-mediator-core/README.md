# CQ(E)RS Mediator Core

This library contains all core logic and implementations for the CQ(E)RS Mediator API. This library can be used in
combination with any framework as long as you bootstrap all necessary objects yourself. For your convenience, however,
there are specific implementations for a few of the most popular frameworks out there.

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

These are the necessary dependencies:

- CommandHandlerProvider
- QueryHandlerProvider
- EventHandlerProvider
- Mediator
- CommandDispatcher
- QueryDispatcher
- EventEmitter

The only way you'll be able to create your own bootstrapper, is by creating your own wrapper for the core and api, and
writing your bootstrapper in the same package as the core (com.compilit.mediator).

It is recommended therefor to use the supplied extensions.


