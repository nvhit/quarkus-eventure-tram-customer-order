package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.service.OrderEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;



@ApplicationScoped
public class CustomerConfiguration {
  @Inject
  @Default
  OrderEventConsumer orderEventConsumer;
  @Produces
  DomainEventDispatcherFactory domainEventDispatcherFactory1;


  @Singleton
  public DomainEventDispatcher domainEventDispatcher() {
    return domainEventDispatcherFactory1.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }


}
