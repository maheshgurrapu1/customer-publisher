package com.prokarma.customer.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.prokarma.jwt.JwtServiceApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Import(JwtServiceApplication.class)
public class CustomerPublisherApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerPublisherApplication.class, args);
  }

}
