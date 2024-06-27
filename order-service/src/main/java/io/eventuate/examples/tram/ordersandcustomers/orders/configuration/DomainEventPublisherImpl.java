package io.eventuate.examples.tram.ordersandcustomers.orders.configuration;

import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
@ApplicationScoped
public class DomainEventPublisherImpl implements DomainEventPublisher {
    @Override
    public void publish(String aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {

    }

    @Override
    public void publish(String aggregateType, Object aggregateId, Map<String, String> headers, List<DomainEvent> domainEvents) {

    }

    @Override
    public void publish(Class<?> aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {
        DomainEventPublisher.super.publish(aggregateType, aggregateId, domainEvents);
    }
}
