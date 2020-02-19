package com.prokarma.customer.publisher.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.google.gson.Gson;
import com.prokarma.jwt.model.AuthenticationResponse;


@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"customer_topic"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CustomersControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  private HttpHeaders httpHeaders;

  private String applicaitonId = "test_case";

  private String validJSONPayload = "{\r\n" + "  \"customerNumber\": \"Abcd123445\",\r\n"
      + "  \"firstName\": \"Mahesh Gurrapu\",\r\n" + "  \"lastName\": \"Gurrapu dfdfdgg\",\r\n"
      + "  \"birthdate\": \"05-08-1990\",\r\n" + "  \"country\": \"India\",\r\n"
      + "  \"countryCode\": \"IN\",\r\n" + "  \"mobileNumber\": 97000070082,\r\n"
      + "  \"email\": \"abc@gmail.com\",\r\n" + "  \"customerStatus\": \"Open\",\r\n"
      + "  \"address\": {\r\n" + "    \"addressLine1\": \"string1\",\r\n"
      + "    \"addressLine2\": \"string1\",\r\n" + "    \"street\": \"string1\",\r\n"
      + "    \"postalCode\": 50431\r\n" + "  }\r\n" + "}";

  private String invalidJSONPayload = "{\n" + "  \"customerNumber\": \"0134\",\n"
      + "  \"firstName\": \"1234\",\n" + "  \"lastName\": \"1234\",\n"
      + "  \"birthdate\": \"08-993\",\n" + "  \"country\": \"\",\n" + "  \"countryCode\": \"\",\n"
      + "  \"mobileNumber\": \"9210\",\n" + "  \"email\": \"someoneom\",\n"
      + "  \"customerStatus\": \"Opn\",\n" + "  \"address\": {\n" + "    \"addressLine1\": \"\",\n"
      + "    \"addressLine2\": \"string\",\n" + "    \"street\": \"string\",\n"
      + "    \"postalCode\": \"12345\"\n" + "  }\n" + "}";

  private String validLoginPayloaf =
      "{\n" + "\"username\": \"mahesh\",\n" + "\"password\": \"mahesh\"\t\n" + "}";

  private String authToken;

  @BeforeEach
  void setup() throws Exception {
    httpHeaders = new HttpHeaders();

    httpHeaders.add("Activity-Id", "mobile");
    httpHeaders.add("Application-Id", applicaitonId);

    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/authenticate").content(validLoginPayloaf)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    String authorizationToken = result.getResponse().getContentAsString();

    AuthenticationResponse jwtResponse =
        gson.fromJson(authorizationToken, AuthenticationResponse.class);
    authToken = "Bearer " + jwtResponse.getJwtToken();
  }

  @Test
  void testAddUserAuthorized_Success() throws Exception {
    httpHeaders.add("Authorization", authToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/customers").content(validJSONPayload)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .headers(httpHeaders)).andExpect(MockMvcResultMatchers.status().isOk());
  }


  @Test
  void testAddUserAuthorized_Failure() throws Exception {
    httpHeaders.add("Authorization", authToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/customers").content(invalidJSONPayload)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .headers(httpHeaders)).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testAddUserAuthorized_Failure_validation() throws Exception {
    httpHeaders.add("Authorization", authToken);

    String firstNameInvalidJsonPayload = "{\r\n" + "  \"customerNumber\": \"Abcd123445\",\r\n"
        + "  \"firstName\": \"Mahesh\",\r\n" + "  \"lastName\": \"Gurrapu dfdfdgg\",\r\n"
        + "  \"birthdate\": \"05-08-1990\",\r\n" + "  \"country\": \"India\",\r\n"
        + "  \"countryCode\": \"IN\",\r\n" + "  \"mobileNumber\": 97000070082,\r\n"
        + "  \"email\": \"abc@gmail.com\",\r\n" + "  \"customerStatus\": \"Open\",\r\n"
        + "  \"address\": {\r\n" + "    \"addressLine1\": \"string1\",\r\n"
        + "    \"addressLine2\": \"string1\",\r\n" + "    \"street\": \"string1\",\r\n"
        + "    \"postalCode\": 50431\r\n" + "  }\r\n" + "}";
    mockMvc
        .perform(MockMvcRequestBuilders.post("/customers").content(firstNameInvalidJsonPayload)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .headers(httpHeaders))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message")
            .value("First Name length should be in between 10 to 50."));
  }

  @Test
  void addUserWithExpiredToken() throws Exception {
    httpHeaders.add("Authorization",
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWhlc2giLCJleHAiOjE1NzQ0MTU0MzYsImlhdCI6MTU3NDQxNDUzNn0.GyB6ErSfZi_KAh7uGzBT4WqNfg8xn9aCXh6H1h0lCV7UCMSS6toXa4yv-CBEQ7YXTtYRUFSdPAZaoQmazEuV3Q");

    mockMvc.perform(MockMvcRequestBuilders.post("/customers").content(invalidJSONPayload)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .headers(httpHeaders)).andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }



}
