package com.prokarma.customer.publisher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
