package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderRejectedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

import static java.util.Collections.singletonList;

@ApplicationScoped
public class OrderService {

  @Inject
  private DomainEventPublisher domainEventPublisher;

  @PersistenceContext
  EntityManager entityManager;

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.result;
    System.out.println("KQ: ID=" + order.getId() + " CustomerId: "+order.getOrderDetails().getCustomerId() + " OrderTotal: "+
            order.getOrderDetails().getOrderTotal());
    entityManager.persist(order);
    domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = Optional.ofNullable(entityManager.find(Order.class, orderId))
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReserved();
    OrderDetails orderDetails = new OrderDetails(order.getOrderDetails().getCustomerId(), new Money(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(orderDetails)));
  }

  public void rejectOrder(Long orderId) {
    Order order = Optional
            .ofNullable(entityManager.find(Order.class, orderId))
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReservationFailed();
    OrderDetails orderDetails = new OrderDetails(order.getOrderDetails().getCustomerId(), new Money(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(orderDetails)));
  }
}
