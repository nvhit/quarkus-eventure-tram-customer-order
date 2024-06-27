package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.web.orders;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.OrderViewRepository;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/orders")
public class OrderViewController {

  @Produces
  OrderViewRepository orderViewRepository;

  @Path("/{orderId}")
  @GET
  public OrderView getCustomer(@PathParam("orderId") Long orderId) {
    return Optional
            .ofNullable(orderViewRepository.findById(orderId))
            .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
  }
}
