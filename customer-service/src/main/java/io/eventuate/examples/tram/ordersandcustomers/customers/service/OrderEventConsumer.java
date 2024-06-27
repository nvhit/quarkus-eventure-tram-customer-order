package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


//@Singleton
@ApplicationScoped
public class OrderEventConsumer {

  @Inject
  CustomerService customerService;


  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .build();
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();

    customerService.reserveCredit(orderId, orderCreatedEvent);
  }
}
