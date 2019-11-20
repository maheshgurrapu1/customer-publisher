package com.prokarma.customer.publisher.model;

import java.time.LocalDate;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Error
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen",
    date = "2019-11-20T09:19:52.536Z")

public class Error {
  @JsonProperty("timestamp")
  private LocalDate timestamp = null;

  @JsonProperty("statusCode")
  private Integer statusCode = null;

  @JsonProperty("error")
  private String error = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("path")
  private String path = null;

  public Error timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * exception occurred time
   * 
   * @return timestamp
   **/
  @ApiModelProperty(value = "exception occurred time")

  @Valid

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public Error statusCode(Integer statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * status code of the error
   * 
   * @return statusCode
   **/
  @ApiModelProperty(value = "status code of the error")


  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public Error error(String error) {
    this.error = error;
    return this;
  }

  /**
   * details of the exception
   * 
   * @return error
   **/
  @ApiModelProperty(value = "details of the exception")


  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Error message(String message) {
    this.message = message;
    return this;
  }

  /**
   * reason for the exception
   * 
   * @return message
   **/
  @ApiModelProperty(value = "reason for the exception")


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error path(String path) {
    this.path = path;
    return this;
  }

  /**
   * path/api endpoint
   * 
   * @return path
   **/
  @ApiModelProperty(value = "path/api endpoint")


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(this.timestamp, error.timestamp)
        && Objects.equals(this.statusCode, error.statusCode)
        && Objects.equals(this.error, error.error) && Objects.equals(this.message, error.message)
        && Objects.equals(this.path, error.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, statusCode, error, message, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");

    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

