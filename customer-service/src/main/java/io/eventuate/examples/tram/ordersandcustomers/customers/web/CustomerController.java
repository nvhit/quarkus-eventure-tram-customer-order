package io.eventuate.examples.tram.ordersandcustomers.customers.web;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.Objects;

@Path(value = "/customers")
public class CustomerController {

  @Inject
  CustomerService customerService;


  @POST
  public CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
    if (Objects.isNull(customerService)) {
      System.out.println("Hung3434"+ customerService);
    }
    System.out.println("Hung"+ customerService);
    System.out.println("Hung"+ customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit()));

    Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    return new CreateCustomerResponse(customer.getId());
  }
}
