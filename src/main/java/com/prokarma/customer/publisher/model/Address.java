package com.prokarma.customer.publisher.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Address
 */
@Validated
@Data
public class Address {

  @ApiModelProperty(required = true, value = "")
  @NotEmpty(message = "{address.addressLine1.notempty}")
  @Size(max = 50, message = "{address.addressLine1.size}")
  @JsonProperty("addressLine1")
  private String addressLine1 = null;

  @ApiModelProperty(value = "")
  @JsonProperty("addressLine2")
  private String addressLine2 = null;

  @ApiModelProperty(value = "")
  @JsonProperty("street")
  private String street = null;

  @ApiModelProperty(required = true, value = "")
  @NotEmpty(message = "{address.postalCode.notempty}")
  @Size(max = 5, message = "{address.postalCode.size}")
  @JsonProperty("postalCode")
  private String postalCode = null;


}

