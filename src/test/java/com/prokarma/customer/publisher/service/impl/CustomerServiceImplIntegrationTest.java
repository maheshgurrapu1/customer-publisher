package com.prokarma.customer.publisher.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
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
    customerService.publishToKafka(new Customer());

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }



}
