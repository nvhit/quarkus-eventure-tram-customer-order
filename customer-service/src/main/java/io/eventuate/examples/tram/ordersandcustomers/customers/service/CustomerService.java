package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditLimitExceededException;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerValidationFailedEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

//@Singleton
@ApplicationScoped
public class CustomerService {
  private Logger logger = LoggerFactory.getLogger(getClass());


   @Inject
  private DomainEventPublisher domainEventPublisher;

  @PersistenceContext
  private EntityManager entityManager;


  @Transactional
  public Customer createCustomer(String name, Money creditLimit) {
    ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
    Customer customer = customerWithEvents.result;
    entityManager.persist(customer);
    if (Objects.isNull(domainEventPublisher)) {
      System.out.println("HungNull: ");
    }
    domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
    return customer;
  }

  public void reserveCredit(Long orderId, OrderCreatedEvent orderCreatedEvent) {

    Long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

    Customer customer = entityManager.find(Customer.class, customerId);

    if (customer == null) {
      logger.info("Non-existent customer: {}", customerId);
      domainEventPublisher.publish(Customer.class,
              customerId,
              Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
      return;
    }

    try {
      customer.reserveCredit(orderId, orderCreatedEvent.getOrderDetails().getOrderTotal());

      CustomerCreditReservedEvent customerCreditReservedEvent =
              new CustomerCreditReservedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservedEvent));

    } catch (CustomerCreditLimitExceededException e) {

      CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
              new CustomerCreditReservationFailedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservationFailedEvent));
    }
  }
}
