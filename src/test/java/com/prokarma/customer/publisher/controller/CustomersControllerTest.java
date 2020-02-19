package com.prokarma.customer.publisher.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;


@ExtendWith(MockitoExtension.class)
class CustomersControllerTest {

  @Spy
  private CustomerService customerService;

  @InjectMocks
  private CustomersController customersApiController;

  @Mock
  private Customer customer;

  private final String empty = "";


  @Test
  void addCustomerWithSuccess() {
    BDDMockito.doNothing().when(customerService).publishToKafka(customer);
    ResponseEntity<Void> response =
        customersApiController.addCustomer(customer, empty, empty, empty);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCodeValue()).isEqualTo(200);

  }

  @Test
  void addCustomerWithFailure() {
    BDDMockito.doThrow(new RuntimeException("Something Went Wrong")).when(customerService)
        .publishToKafka(customer);
    // ResponseEntity<Void> response =
    // customersApiController.addCustomer(customer, empty, empty, empty);
    assertThrows(RuntimeException.class,
        () -> customersApiController.addCustomer(customer, empty, empty, empty));
    // assertThat(response.getStatusCodeValue()).isEqualTo(200);

  }

}
