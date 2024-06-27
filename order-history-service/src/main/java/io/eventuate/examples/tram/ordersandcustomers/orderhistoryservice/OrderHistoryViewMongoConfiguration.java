package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.OrderViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryViewService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;

@ApplicationScoped
public class OrderHistoryViewMongoConfiguration {

  @Named
  @Produces
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository, OrderViewRepository orderViewRepository) {
    return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
  }
}
