package com.prokarma.customer.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.prokarma"})
@EnableSwagger2
public class CustomerPublisherApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerPublisherApplication.class, args);
  }

}
