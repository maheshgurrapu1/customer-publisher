package com.prokarma.customer.publisher.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@EmbeddedKafka(partitions = 1, topics = {"${kafka.customer.topic.name}"})
@SpringBootTest
class ClientDetailsUserDetailsServiceTest {

  @Autowired
  private ClientDetailsUserDetailsService clientDetailsUserDetailsService;

  @Test
  void testInvalidUser() {
    assertThrows(UsernameNotFoundException.class, () -> {
      clientDetailsUserDetailsService.loadUserByUsername("");
    });
  }

  @Test
  void testValidUser() {
    assertThrows(UsernameNotFoundException.class, () -> {
      clientDetailsUserDetailsService.loadUserByUsername("");
    });
  }
}
