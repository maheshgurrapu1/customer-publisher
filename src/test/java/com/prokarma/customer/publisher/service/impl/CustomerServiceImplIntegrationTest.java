package com.prokarma.customer.publisher.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import com.prokarma.customer.publisher.model.Address;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"customer_topic"})
class CustomerServiceImplIntegrationTest {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private Receiver receiver;

  @Test
  void testPublishToKafka() throws InterruptedException {

    SecurityContextImpl contextImpl =
        new SecurityContextImpl(new TestingAuthenticationToken("Mahesh", "Test"));
    SecurityContextHolder.setContext(contextImpl);
    // securityContext.setAuthentication(new TestingAuthenticationToken("Mahesh", "Test"));
    Customer customer = new Customer();
    customer.setAddress(new Address());

    customerService.publishToKafka(customer);

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }



}
