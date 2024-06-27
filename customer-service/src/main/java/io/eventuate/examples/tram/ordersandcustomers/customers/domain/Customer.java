package io.eventuate.examples.tram.ordersandcustomers.customers.domain;


import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonList;

@Entity
@Table(name="Customer")
@Access(AccessType.FIELD)
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String name;

  @Embedded
  private Money creditLimit;

  @ElementCollection
  private Map<Long, Money> creditReservations;

  @Version
  private Long version;

  Money availableCredit() {
    return creditLimit.subtract(creditReservations.values().stream().reduce(Money.ZERO, Money::add));
  }

  public Customer() {
  }

  public Customer(String name, Money creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
    this.creditReservations = Collections.emptyMap();
  }

  public static ResultWithEvents<Customer> create(String name, Money creditLimit) {
    Customer customer = new Customer(name, new Money(creditLimit.getAmount()));
    CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent(customer.getName(),
            new Money(creditLimit.getAmount()));
    return new ResultWithEvents<>(customer, singletonList(customerCreatedEvent));
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }

  public void reserveCredit(Long orderId, Money orderTotal) {
    Money order = new Money(orderTotal.getAmount());

    if (availableCredit().isGreaterThanOrEqual(order)) {
      creditReservations.put(orderId, order);
    } else
      throw new CustomerCreditLimitExceededException();
  }
}
