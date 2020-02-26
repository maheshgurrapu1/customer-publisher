package com.prokarma.customer.publisher.model;

import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Error
 */
@Validated
@Data
public class ErrorResponse {

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

}

