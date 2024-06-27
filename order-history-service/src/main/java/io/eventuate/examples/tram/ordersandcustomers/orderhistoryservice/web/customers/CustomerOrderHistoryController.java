package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.web.customers;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.CustomerViewRepository;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/customers")
public class CustomerOrderHistoryController {

  @Produces
  CustomerViewRepository customerViewRepository;

  @Path("/{customerId}")
  @GET
  public CustomerView getCustomer(@PathParam("customerId") Long customerId) {
    return Optional
            .ofNullable(customerViewRepository.findById(customerId))
            .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
  }
}
