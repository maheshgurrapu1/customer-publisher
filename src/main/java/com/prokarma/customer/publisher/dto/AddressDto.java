package com.prokarma.customer.publisher.dto;

import org.springframework.validation.annotation.Validated;
import lombok.Data;

/**
 * Address
 */
@Validated
@Data
public class AddressDto {

  private String addressLine1 = null;

  private String addressLine2 = null;

  private String street = null;

  private String postalCode = null;


}

