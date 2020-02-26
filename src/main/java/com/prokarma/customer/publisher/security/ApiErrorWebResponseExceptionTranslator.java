package com.prokarma.customer.publisher.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import com.prokarma.customer.publisher.model.ErrorResponse;

public class ApiErrorWebResponseExceptionTranslator
    implements WebResponseExceptionTranslator<ErrorResponse> {

  /** The default WebResponseExceptionTranslator. */
  private final WebResponseExceptionTranslator<OAuth2Exception> defaultTranslator =
      new DefaultWebResponseExceptionTranslator();

  // Constructor omitted

  @Override
  public ResponseEntity<ErrorResponse> translate(final Exception e) throws Exception {
    // Translate the exception with the default translator
    ResponseEntity<OAuth2Exception> defaultResponse = this.defaultTranslator.translate(e);
    // Build your own error object
    String errorCode = defaultResponse.getBody().getOAuth2ErrorCode();
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode(errorCode);
    errorResponse.setMessage(defaultResponse.getBody().getMessage());
    // Use the same status code as the default OAuth2 error
    return new ResponseEntity<>(errorResponse, defaultResponse.getStatusCode());
  }
}
