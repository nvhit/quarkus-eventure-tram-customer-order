package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service;

import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class CustomerHistoryEventConsumer {

  @Produces
  OrderHistoryViewService orderHistoryViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
            .build();
  }

  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(Long.parseLong(domainEventEnvelope.getAggregateId()),
            customerCreatedEvent.getName(), customerCreatedEvent.getCreditLimit());
  }
}
