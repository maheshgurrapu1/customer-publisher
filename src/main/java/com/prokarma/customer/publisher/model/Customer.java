package com.prokarma.customer.publisher.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Customer
 */
@Validated
@Data
public class Customer {

  @ApiModelProperty(required = true, value = "")
  @Size(min = 5, max = 10, message = "{customer.customerNumber.size}")
  @Pattern(regexp = "^[a-zA-Z0-9]+$")
  @JsonProperty("customerNumber")
  private String customerNumber = null;

  @ApiModelProperty(required = true, value = "")
  @NotEmpty(message = "{customer.firstName.notempty}")
  @Size(min = 10, max = 50, message = "{customer.firstName.size}")
  @JsonProperty("firstName")
  private String firstName = null;

  @ApiModelProperty(required = true, value = "")
  @NotEmpty(message = "{customer.lastName.notempty}")
  @Size(min = 10, max = 50, message = "{customer.lastName.size}")
  @JsonProperty("lastName")
  private String lastName = null;

  @ApiModelProperty(example = "DD-MM-YYYY", required = true, value = "")
  @NotNull(message = "{customer.birthdate.notnull}")
  @Valid
  @JsonProperty("birthdate")
  @JsonFormat(pattern = "DD-MM-YYYY")
  private Date birthdate = null;

  @ApiModelProperty(example = "India", required = true, value = "")
  @NotEmpty(message = "{customer.country.notempty}")
  @JsonProperty("country")
  private String country = null;

  @ApiModelProperty(example = "IN", required = true, value = "")
  @NotEmpty(message = "{customer.countryCode.notempty}")
  @Size(min = 2, max = 2, message = "{customer.countrycode.size}")
  @JsonProperty("countryCode")
  private String countryCode = null;

  @ApiModelProperty(required = true, value = "")
  @NotNull(message = "{customer.mobileNumber.notnull}")
  @Valid
  @JsonProperty("mobileNumber")
  private BigDecimal mobileNumber = null;

  @ApiModelProperty(example = "abc@gmail.com", required = true, value = "User Status")
  @NotEmpty(message = "{customer.email.notempty}")
  @Email(message = "{customer.email.invalid}")
  @Size(max = 50, message = "{customer.email.size}")
  @JsonProperty("email")
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

  @ApiModelProperty(required = true,
      value = "In database these values are stored as R,S,O & C R = Restored S = Suspended O = Open C = Closed")
  @NotNull(message = "{customer.customerStatus.notnull}")
  @JsonProperty("customerStatus")
  private CustomerStatusEnum customerStatus = null;

  @ApiModelProperty(required = true, value = "")
  @NotNull(message = "{customer.address.notnull}")
  @Valid
  @JsonProperty("address")
  private Address address = null;



}

