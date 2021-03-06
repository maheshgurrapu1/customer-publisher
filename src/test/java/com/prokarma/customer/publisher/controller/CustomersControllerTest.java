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
import com.prokarma.customer.publisher.common.AuthenticationHolder;
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

  @Mock
  private AuthenticationHolder authHolder;

  private final String empty = "";


  @Test
  void addCustomerWithSuccess() {
    // when(authHolder.getUser()).thenReturn("Mahesh");
    BDDMockito.given(customerService.publishToKafka(customer)).willReturn("Hello");
    ResponseEntity<Void> response =
        customersApiController.addCustomer(empty, empty, empty, customer);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCodeValue()).isEqualTo(200);

  }

  @Test
  void addCustomerWithFailure() {
    BDDMockito.doThrow(new RuntimeException("Something Went Wrong")).when(customerService)
        .publishToKafka(customer);
    assertThrows(RuntimeException.class,
        () -> customersApiController.addCustomer(empty, empty, empty, customer));

  }

}
