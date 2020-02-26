package com.prokarma.customer.publisher.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.prokarma.customer.publisher.exception.InvalidUserSessionException;

@Component
public class AuthenticationHolder {

  public String getUser() {
    try {
      return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    } catch (Exception e) {
      throw new InvalidUserSessionException();
    }
  }

}
