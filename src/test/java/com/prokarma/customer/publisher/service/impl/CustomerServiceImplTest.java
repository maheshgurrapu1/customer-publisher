package com.prokarma.customer.publisher.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokarma.customer.publisher.common.AuthenticationHolder;
import com.prokarma.customer.publisher.common.JsonConverter;
import com.prokarma.customer.publisher.exception.InvalidUserSessionException;
import com.prokarma.customer.publisher.model.Address;
import com.prokarma.customer.publisher.model.Customer;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Spy
  private ObjectMapper objectMapper;

  @InjectMocks
  private JsonConverter jsonConverter; // = new JsonConverter(new ObjectMapper());

  @Spy
  ListenableFuture<SendResult<String, String>> responseFuture;

  @Mock
  private AuthenticationHolder authHolder;

  private String customerTopic = "customer_topic";


  @BeforeEach
  public void setUp() {

    ReflectionTestUtils.setField(customerService, "jsonConverter", jsonConverter);
    ReflectionTestUtils.setField(customerService, "customerTopic", customerTopic);
  }

  @SuppressWarnings("unchecked")
  @Test
  void testPublishToKafka_onSuccess() {


    when(jsonConverter.toJson(Mockito.any())).thenReturn("Some String val");
    when(authHolder.getUser()).thenReturn("Mahesh");


    SendResult<String, String> sendResult = Mockito.mock(SendResult.class);

    when(kafkaTemplate.send(Mockito.any(Message.class))).thenReturn(responseFuture);
    doAnswer(invocationOnMock -> {
      SuccessCallback<SendResult<String, String>> successCallback =
          invocationOnMock.getArgument(0, SuccessCallback.class);
      successCallback.onSuccess(sendResult);
      return null;
    }).when(responseFuture).addCallback(Mockito.any(SuccessCallback.class),
        Mockito.any(FailureCallback.class));



    String message = this.sendMessage();
    assertEquals("Sending message to queue.", message);

    verify(kafkaTemplate, times(1)).send(Mockito.any(Message.class));

  }

  @Test
  @SuppressWarnings("unchecked")
  void sendMessageFailure() throws JsonProcessingException {
    when(jsonConverter.toJson(Mockito.any())).thenReturn("Some String val");
    when(authHolder.getUser()).thenReturn("Mahesh");

    Throwable error = Mockito.mock(Throwable.class);

    when(kafkaTemplate.send(Mockito.any(Message.class))).thenReturn(responseFuture);
    assertThrows(ResponseStatusException.class, () -> {

      doAnswer(invocationOnMock -> {
        FailureCallback faulureCallback = invocationOnMock.getArgument(1);
        faulureCallback.onFailure(error);
        return null;
      }).when(responseFuture).addCallback(Mockito.any(SuccessCallback.class),
          Mockito.any(FailureCallback.class));
      this.sendMessage();
    });

  }

  @Test
  void expectInvalidSession() throws Exception {

    when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("Some String val");
    when(authHolder.getUser()).thenThrow(InvalidUserSessionException.class);

    assertThrows(ResponseStatusException.class, this::sendMessage);
  }

  @Test
  void expectJsonProcessingException() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(Mockito.any())).thenThrow(JsonProcessingException.class);

    assertThrows(ResponseStatusException.class, this::sendMessage);
  }

  @Test
  void expectJsonProcessingExceptionnullErrors() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(Mockito.any())).thenThrow(JsonProcessingException.class);
    assertThrows(ResponseStatusException.class, this::sendMessage);
  }


  private String sendMessage() {
    Customer customer = new Customer();
    Address address = new Address();
    customer.setAddress(address);
    return customerService.publishToKafka(customer);
  }
}
