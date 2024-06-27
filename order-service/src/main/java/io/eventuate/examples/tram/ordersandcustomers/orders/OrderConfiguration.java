package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.service.CustomerEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@ApplicationScoped
public class OrderConfiguration {
  @Inject
  @Default
  CustomerEventConsumer customerEventConsumer;
  @Produces
  DomainEventDispatcherFactory domainEventDispatcherFactory;
  @Singleton
  public DomainEventDispatcher domainEventDispatcher() {
    return domainEventDispatcherFactory.make("consumerServiceEvents", customerEventConsumer.domainEventHandlers());
  }
}
