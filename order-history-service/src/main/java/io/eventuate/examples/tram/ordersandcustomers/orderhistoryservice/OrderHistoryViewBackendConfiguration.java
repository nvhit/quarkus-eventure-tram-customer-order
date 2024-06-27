package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.CustomerHistoryEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

//@Singleton
@ApplicationScoped
public class OrderHistoryViewBackendConfiguration {

  @Named("orderHistoryDomainEventDispatcher")
  @ApplicationScoped
  public DomainEventDispatcher orderHistoryDomainEventDispatcher(OrderHistoryEventConsumer orderHistoryEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderHistoryServiceEvents", orderHistoryEventConsumer.domainEventHandlers());
  }

  @Named("customerHistoryDomainEventDispatcher")
  @ApplicationScoped
  public DomainEventDispatcher customerHistoryDomainEventDispatcher(CustomerHistoryEventConsumer customerHistoryEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerHistoryServiceEvents", customerHistoryEventConsumer.domainEventHandlers());
  }
}
