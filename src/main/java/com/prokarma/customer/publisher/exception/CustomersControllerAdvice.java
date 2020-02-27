package com.prokarma.customer.publisher.exception;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.prokarma.customer.publisher.model.ErrorResponse;

@RestControllerAdvice
public class CustomersControllerAdvice {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    String errorMessage = exception.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
    ErrorResponse exceptionResponse = createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);

    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  private ErrorResponse createErrorResponse(HttpStatus httpStatus, String errorMessage) {
    ErrorResponse exceptionResponse = new ErrorResponse();
    exceptionResponse.setCode(httpStatus.name());
    exceptionResponse.setMessage(errorMessage);
    return exceptionResponse;
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  protected ResponseEntity<Object> handleHeadersIssue(WebRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.BAD_REQUEST, "Required headers are missing."),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Object> unhandledPath(final NoHandlerFoundException e) {

    return new ResponseEntity<>(createErrorResponse(HttpStatus.NOT_FOUND, "No handler found"),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception in processing data."),
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }


}
