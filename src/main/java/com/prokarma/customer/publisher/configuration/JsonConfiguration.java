package com.prokarma.customer.publisher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.gson.Gson;

@Configuration
public class JsonConfiguration {

  @Bean
  public Gson jsonConverter() {
    return new Gson();
  }
}
