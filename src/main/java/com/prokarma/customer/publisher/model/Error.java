package com.prokarma.customer.publisher.model;

import java.util.Date;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Error
 */
@Validated
@Data
public class Error {

  @ApiModelProperty(value = "exception occurred time")
  @JsonProperty("timestamp")
  private Date timestamp = null;

  @ApiModelProperty(value = "status code of the error")
  @JsonProperty("statusCode")
  private Integer statusCode = null;

  @ApiModelProperty(value = "details of the exception")
  @JsonProperty("error")
  private String error = null;

  @ApiModelProperty(value = "reason for the exception")
  @JsonProperty("message")
  private String message = null;

  @ApiModelProperty(value = "path/api endpoint")
  @JsonProperty("path")
  private String path = null;

}

