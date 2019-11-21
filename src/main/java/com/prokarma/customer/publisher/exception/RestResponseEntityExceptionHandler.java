package com.prokarma.customer.publisher.exception;

import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.prokarma.customer.publisher.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleAllExceptions(RuntimeException ex, WebRequest request) {

    logger.error("Exception occurred: ", ex);
    return handleExceptionInternal(ex,
        createErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR,
            "Exception in processing data."),
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(value = ResponseStatusException.class)
  protected ResponseEntity<Object> handleKnownErrors(ResponseStatusException ex,
      WebRequest request) {
    logger.error("Exception occurred: ", ex);
    return handleExceptionInternal(ex,
        createErrorResponse(request, ex.getStatus(), ex.getMessage()).toString(), new HttpHeaders(),
        ex.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    logger.error("Exception occurred: ", exception);
    String errorMessage = exception.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
    ErrorResponse exceptionResponse =
        createErrorResponse(request, HttpStatus.BAD_REQUEST, errorMessage);

    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  private ErrorResponse createErrorResponse(WebRequest request, HttpStatus httpStatus,
      String errorMessage) {
    ErrorResponse exceptionResponse = new ErrorResponse();
    exceptionResponse.setStatusCode(httpStatus.value());
    exceptionResponse.setMessage(errorMessage);
    exceptionResponse.setError(httpStatus.getReasonPhrase());
    exceptionResponse.setTimestamp(new Date());
    exceptionResponse.setPath(request.getDescription(false));
    return exceptionResponse;
  }

}
