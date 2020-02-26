package com.prokarma.customer.publisher.integration;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@EmbeddedKafka(partitions = 1, topics = {"${kafka.customer.topic.name}"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerAuthTest {

  Map<String, String> headers = new HashMap<>();

  @LocalServerPort
  private int port;


  @BeforeEach
  void setupEach() {
    RestAssured.port = port;
    RestAssured.baseURI = "http://localhost";

    headers.put("Authorization", "Basic ZGV2Z2xhbi1jbGllbnQ6ZGV2Z2xhbi1zZWNyZXQ=");
    headers.put("cache-control", "no-cache");
  }

  @Test
  void loginSuccess() {
    RestAssured.given().contentType(ContentType.URLENC).formParam("username", "devglan-client")
        .formParam("password", "devglan-secret").formParam("grant_type", "password")
        .headers(headers).post("oauth/token").then().assertThat().statusCode(HttpStatus.SC_OK);

  }

  @Test
  void missingGrantType() {
    RestAssured.given().contentType(ContentType.URLENC).formParam("username", "devglan-client")
        .formParam("password", "devglan-secret").headers(headers).post("oauth/token").then()
        .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).and()
        .body(CoreMatchers.containsString("Missing grant type"));
  }

  @Test
  void missinPassword() {
    RestAssured.given().contentType(ContentType.URLENC).formParam("username", "devglan-client")
        .formParam("grant_type", "password").headers(headers).post("oauth/token").then()
        .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).and()
        .body(CoreMatchers.containsString("Bad credentials"));
  }
}
