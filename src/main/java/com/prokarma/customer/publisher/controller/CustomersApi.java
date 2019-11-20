package com.prokarma.customer.publisher.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.prokarma.customer.publisher.model.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "customers", description = "the customers API")
public interface CustomersApi {

  @ApiOperation(value = "create a new customer", nickname = "addCustomer",
      notes = "This can only be done by the logged in user", tags = {"customers",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
      @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)})
  @RequestMapping(value = "/customers", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<Void> addCustomer(
      @ApiParam(value = "customer information",
          required = true) @Valid @RequestBody Customer customer,
      @ApiParam(value = "Authorization token that is received from /authenticate endpoint",
          required = true) @RequestHeader(value = "Authorization",
              required = true) String authorization,
      @ApiParam(value = "Application-Id", required = true) @RequestHeader(value = "Application-Id",
          required = true) String applicationId,
      @ApiParam(value = "DESKTOP, MOBILE", required = true) @RequestHeader(
          value = "To identify source activity", required = true) String toIdentifySourceActivity);

}
