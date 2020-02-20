package com.prokarma.customer.publisher.dto;

import java.math.BigDecimal;
import com.prokarma.customer.publisher.common.CustomerStatusEnum;
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

  private CustomerStatusEnum customerStatus = null;

  private AddressDto address = null;



}

