# CQ(E)RS Mediator Core

This library contains all core logic and implementations for the CQ(E)RS Mediator API. This library can be used in
combination with any framework as long as you bootstrap all necessary objects yourself. For your convenience, however,
there are specific implementations for a few of the most popular frameworks out there.

If you don't wish to rely directly on the provided framework implementations, you can register the MediatorConfiguration class as long as your framework supports the jakarta.inject/annotations API.