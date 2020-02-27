package com.prokarma.customer.publisher.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@RestController
public class CustomersController {

  private CustomerService customerService;

  public CustomersController(CustomerService customerService) {
    super();
    this.customerService = customerService;
  }

  @PostMapping(value = "/customers")
  public ResponseEntity<Void> addCustomer(
      @RequestHeader(value = "Authorization", required = true) String authorization,
      @RequestHeader(value = "Application-Id", required = true) String applicationId,
      @RequestHeader(value = "Activity-Id", required = true) String toIdentifySourceActivity,
      @Valid @RequestBody Customer customer) {

    customerService.publishToKafka(customer);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
