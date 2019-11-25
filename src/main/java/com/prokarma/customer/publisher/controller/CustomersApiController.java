package com.prokarma.customer.publisher.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;
import io.swagger.annotations.ApiParam;

@Controller
public class CustomersApiController implements CustomersApi {

  @Autowired
  private CustomerService customerService;


  public ResponseEntity<Void> addCustomer(
      @ApiParam(value = "customer information",
          required = true) @Valid @RequestBody Customer customer,
      @ApiParam(value = "Authorization token that is received from /authenticate endpoint",
          required = true) @RequestHeader(value = "Authorization",
              required = true) String authorization,
      @ApiParam(value = "Application-Id", required = true) @RequestHeader(value = "Application-Id",
          required = true) String applicationId,
      @ApiParam(value = "To identify source activity", required = true) @RequestHeader(
          value = "Activity-Id", required = true) String toIdentifySourceActivity) {

    customerService.publishToKafka(customer);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
