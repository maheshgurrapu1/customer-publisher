package com.prokarma.customer.publisher.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class CustomerDto {

  private String customerNumber = null;

  private String firstName = null;

  private String lastName = null;

  private String birthdate = null;

  private String country = null;

  private String countryCode = null;

  private BigDecimal mobileNumber = null;

  private String email = null;

  /**
   * In database these values are stored as R,S,O & C R = Restored S = Suspended O = Open C = Closed
   */
  public enum CustomerStatusEnum {
    OPEN("Open"),

    CLOSE("Close"),

    SUSPENDED("Suspended"),

    RESTORED("Restored");

    private String value;

    CustomerStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CustomerStatusEnum fromValue(String text) {
      for (CustomerStatusEnum b : CustomerStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  private CustomerStatusEnum customerStatus = null;

  private AddressDto address = null;



}

