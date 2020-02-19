package com.prokarma.customer.publisher.common;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokarma.customer.publisher.exception.JsonParseException;

@Component
public class JsonConverter {

  private ObjectMapper objectMapper;

  public JsonConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      throw new JsonParseException(exception);
    }
  }

  public <T> T toObjct(String json, Class<T> valueType) {
    try {
      return objectMapper.readValue(json, valueType);
    } catch (JsonProcessingException exception) {
      throw new JsonParseException(exception);
    }
  }



}
