package com.prokarma.customer.publisher.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokarma.customer.publisher.exception.JsonParseException;
import com.prokarma.customer.publisher.model.Customer;

@ExtendWith(MockitoExtension.class)
class JsonConverterTest {

  @Spy
  private ObjectMapper objectMapper;

  @Mock
  JsonProcessingException jsonProcessingException;

  @InjectMocks
  private JsonConverter jsonConverter;

  @Test
  void testToJson() {
    Customer customer = new Customer();
    customer.setCustomerNumber("ABCD1234553");
    customer.setCountry("IN");
    String customerJson = jsonConverter.toJson(customer);
    assertEquals(customer, jsonConverter.toObjct(customerJson, Customer.class));
  }


  @Test
  void testToJsonParseException() throws JsonProcessingException {

    Customer customer = new Customer();
    customer.setCustomerNumber("ABCD1234553");
    customer.setCountry("IN");

    BDDMockito.given(objectMapper.writeValueAsString(customer)).willThrow(jsonProcessingException);
    assertThrows(JsonParseException.class, () -> {
      jsonConverter.toJson(customer);
    });
  }

  @Test
  void testToObjct() {
    String validJSONPayload = "{\r\n" + "  \"customerNumber\": \"Abcd123445\",\r\n"
        + "  \"firstName\": \"Mahesh Test\",\r\n" + "  \"lastName\": \"Gurrapu dfdfdgg\",\r\n"
        + "  \"birthdate\": \"05-08-1990\",\r\n" + "  \"country\": \"India\",\r\n"
        + "  \"countryCode\": \"IN\",\r\n" + "  \"mobileNumber\": 97000070082,\r\n"
        + "  \"email\": \"abc@gmail.com\",\r\n" + "  \"customerStatus\": \"Open\",\r\n"
        + "  \"address\": {\r\n" + "    \"addressLine1\": \"string1\",\r\n"
        + "    \"addressLine2\": \"string1\",\r\n" + "    \"street\": \"string1\",\r\n"
        + "    \"postalCode\": 50431\r\n" + "  }\r\n" + "}";

    Customer customer = jsonConverter.toObjct(validJSONPayload, Customer.class);
    assertEquals("Abcd123445", customer.getCustomerNumber());
  }

  @Test
  void testToObjctParseException() throws JsonProcessingException {
    String validJSONPayload = "{\r\n" + "  \"customerNumber\": \"Abcd123445\",\r\n"
        + "  \"firstName\": \"Mahesh Test\",\r\n" + "  \"lastName\": \"Gurrapu dfdfdgg\",\r\n"
        + "  \"birthdate\": \"05-08-1990\",\r\n" + "  \"country\": \"India\",\r\n"
        + "  \"countryCode\": \"IN\",\r\n" + "  \"mobileNumber\": 97000070082,\r\n"
        + "  \"email\": \"abc@gmail.com\",\r\n" + "  \"customerStatus\": \"Open\",\r\n"
        + "  \"address\": {\r\n" + "    \"addressLine1\": \"string1\",\r\n"
        + "    \"addressLine2\": \"string1\",\r\n" + "    \"street\": \"string1\",\r\n"
        + "    \"postalCode\": 50431\r\n" + "  }\r\n" + "}";

    BDDMockito.given(objectMapper.readValue(validJSONPayload, Customer.class))
        .willThrow(jsonProcessingException);

    assertThrows(JsonParseException.class, () -> {
      jsonConverter.toObjct(validJSONPayload, Customer.class);
    });
  }


}
