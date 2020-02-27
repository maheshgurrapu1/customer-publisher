package com.prokarma.customer.publisher.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.prokarma.customer.publisher.exception.InvalidUserSessionException;

@ExtendWith(MockitoExtension.class)
class AuthenticationHolderTest {

  @Mock
  AuthenticationHolder authHolder;

  @Mock
  SecurityContext context;

  @Mock
  Authentication authentication;

  @Test
  void test() {
    when(authHolder.getUser()).thenCallRealMethod();
    assertThrows(InvalidUserSessionException.class, () -> {
      authHolder.getUser();
    });
  }

  @Test
  void testGetUser() {
    SecurityContextHolder.setContext(context);
    context.setAuthentication(authentication);
    // BDDMockito.given(context.getAuthentication()).willReturn(authentication);
    // BDDMockito.given(authentication.getPrincipal()).willReturn("Mahesh");


    assertEquals(null, authHolder.getUser());
  }
}
