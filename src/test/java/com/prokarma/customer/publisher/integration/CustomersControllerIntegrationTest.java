package com.prokarma.customer.publisher.integration;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@EmbeddedKafka(partitions = 1, topics = {"${kafka.customer.topic.name}"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomersControllerIntegrationTest {

  private HttpHeaders httpHeaders;

  private String applicaitonId = "test_case";


  private String validJSONPayload = "{" + "  \"customerNumber\": \"Abcd123445\","
      + "  \"firstName\": \"Mahesh Test\"," + "  \"lastName\": \"Gurrapu dfdfdgg\","
      + "  \"birthdate\": \"05-08-1990\"," + "  \"country\": \"India\","
      + "  \"countryCode\": \"IN\"," + "  \"mobileNumber\": 97000070082,"
      + "  \"email\": \"abc@gmail.com\"," + "  \"customerStatus\": \"Open\"," + "  \"address\": {"
      + "    \"addressLine1\": \"string1\"," + "    \"addressLine2\": \"string1\","
      + "    \"street\": \"string1\"," + "    \"postalCode\": 50431" + "  }" + "}";

  private String invalidJSONPayload = "{\n" + "  \"customerNumber\": \"0134\",\n"
      + "  \"firstName\": \"1234\",\n" + "  \"lastName\": \"1234\",\n"
      + "  \"birthdate\": \"08-993\",\n" + "  \"country\": \"\",\n" + "  \"countryCode\": \"\",\n"
      + "  \"mobileNumber\": \"9210\",\n" + "  \"email\": \"someoneom\",\n"
      + "  \"customerStatus\": \"Opn\",\n" + "  \"address\": {\n" + "    \"addressLine1\": \"\",\n"
      + "    \"addressLine2\": \"string\",\n" + "    \"street\": \"string\",\n"
      + "    \"postalCode\": \"12345\"\n" + "  }\n" + "}";


  private String authToken;

  @LocalServerPort
  private int port;


  @BeforeEach
  void setupEach() {
    RestAssured.port = port;
    RestAssured.baseURI = "http://localhost";

    httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Basic ZGV2Z2xhbi1jbGllbnQ6ZGV2Z2xhbi1zZWNyZXQ=");
    httpHeaders.add("cache-control", "no-cache");
    httpHeaders.add("Activity-Id", "mobile");
    httpHeaders.add("Application-Id", applicaitonId);

    ExtractableResponse<Response> resp = RestAssured.given().contentType(ContentType.URLENC)
        .formParam("username", "devglan-client").formParam("password", "devglan-secret")
        .formParam("grant_type", "password").headers(httpHeaders).post("oauth/token").then()
        .assertThat().statusCode(HttpStatus.SC_OK).and().extract();

    JwtResponse jwtResponse = resp.as(JwtResponse.class);
    authToken = "Bearer " + jwtResponse.getAccessToken();
  }

  @Test
  void createCustomerUnauthorized() throws Exception {
    httpHeaders.add("Authorization", "Test");

    RestAssured.given().headers(httpHeaders).body(validJSONPayload).post("/customers").then()
        .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).and().body(CoreMatchers.containsString(
            "\"message\":\"An Authentication object was not found in the SecurityContext\""));

  }

  @Test
  void createCustomerAuthorized() throws Exception {
    httpHeaders.add("Authorization", authToken);

    RestAssured.given().headers(httpHeaders).contentType(ContentType.JSON).body(validJSONPayload)
        .post("/customers").then().assertThat().statusCode(HttpStatus.SC_OK);


  }

  @Test
  void createCustomerWithoutSufficentHeaders() throws Exception {
    httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", authToken);

    RestAssured.given().headers(httpHeaders).body(validJSONPayload).post("/customers").then()
        .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).and()
        .body(CoreMatchers.containsString("\"message\":\"Required headers are missing.\""));

  }

  @Test
  void createCustomerWithErrorInBody() throws Exception {
    httpHeaders.add("Authorization", authToken);

    String firstNameInvalidJsonPayload = "{" + "  \"customerNumber\": \"Abcd123445\","
        + "  \"firstName\": \"Mahesh\"," + "  \"birthdate\": \"05-08-1990\","
        + "  \"country\": \"India\"," + "  \"countryCode\": \"IN\","
        + "  \"mobileNumber\": 97000070082," + "  \"email\": \"abcgmail.com\","
        + "  \"customerStatus\": \"Open\"," + "  \"address\": {"
        + "    \"addressLine1\": \"string1\"," + "    \"addressLine2\": \"string1\","
        + "    \"street\": \"string1\"," + "    \"postalCode\": 50431" + "  }" + "}";

    RestAssured.given().headers(httpHeaders).contentType(ContentType.JSON)
        .body(firstNameInvalidJsonPayload).post("/customers").then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST).and()
        .body(CoreMatchers.containsString("First Name length should be in between 10 to 50."),
            CoreMatchers.containsString("Last Name is mandatory."),
            CoreMatchers.containsString("Invalid E-Mail."));
  }

  @Test
  void createCustomerExpiredToken() throws Exception {
    httpHeaders.add("Authorization",
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzQ4NDk4MjIsInVzZXJfbmFtZSI6ImRldmdsYW4tY2xpZW50IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJhZjQ4MzRhMC1jMzQ0LTRkYTAtYWRmMS04YzZjMjE0YTAwNGIiLCJjbGllbnRfaWQiOiJkZXZnbGFuLWNsaWVudCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il19.O9J266LbClFRODsFwkULxIjqRywakjLivDlsJBIbJZg");

    RestAssured.given().headers(httpHeaders).contentType(ContentType.JSON).body(invalidJSONPayload)
        .post("/customers").then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

  }

  @Test
  void createCustomerInvalidToken() throws Exception {
    httpHeaders.add("Authorization",
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxM2NDE2MDJ9.Yd67ZM3eNyfyanO_N6LX6Ky-BkXETwNogDAf6R6XkOrW5U_I7HTPorjR7nAAB03PYflKBnEHObBPDPmBtuMUqw");

    RestAssured.given().headers(httpHeaders).contentType(ContentType.JSON).body(invalidJSONPayload)
        .post("/customers").then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
  }

  @Test
  void noHandlerFound() throws Exception {
    httpHeaders.add("Authorization", authToken);

    RestAssured.given().headers(httpHeaders).contentType(ContentType.JSON).body(invalidJSONPayload)
        .post("/customer").then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND).and()
        .body(CoreMatchers.containsString("No handler found"));

  }
}
