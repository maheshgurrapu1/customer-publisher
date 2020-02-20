package com.prokarma.customer.publisher.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokarma.customer.publisher.common.JsonConverter;
import com.prokarma.customer.publisher.model.Customer;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  private JsonConverter jsonConverter = new JsonConverter(new ObjectMapper());

  @Spy
  SettableListenableFuture<SendResult<String, String>> future;



  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(customerService, "jsonConverter", jsonConverter);
  }

  @Test
  void testPublishToKafka_onSuccess() {

    // KafkaOperations template = mock(KafkaOperations.class);


    TopicPartition topicPartition = new TopicPartition("customer_topic", 1);
    RecordMetadata recordMetadata =
        new RecordMetadata(topicPartition, 1, 1, System.currentTimeMillis(), (long) 1, 1, 1);

    ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("", "");
    SendResult<String, String> sendResult = new SendResult<>(producerRecord, recordMetadata);
    future.set(sendResult);

    BDDMockito.given(kafkaTemplate.send(BDDMockito.anyString(), BDDMockito.anyString()))
        .willReturn(future);


    BDDMockito.doAnswer(invocationOnMock -> {
      SuccessCallback<SendResult<String, String>> listenableFutureCallback =
          invocationOnMock.getArgument(0);
      listenableFutureCallback.onSuccess(sendResult);
      assertEquals(2L, sendResult.getRecordMetadata().offset());
      assertEquals(1, sendResult.getRecordMetadata().partition());
      return null;
    }).when(future).addCallback(BDDMockito.any(SuccessCallback.class),
        BDDMockito.any(FailureCallback.class));

    customerService.publishToKafka(new Customer());

    verify(kafkaTemplate, times(1)).send(BDDMockito.any(), BDDMockito.any());


  }

  @Test
  void testPublishToKafka_onFailure() {

    ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("", "");
    TopicPartition topicPartition = new TopicPartition("customer_topic", 1);

    RecordMetadata recordMetadata =
        new RecordMetadata(topicPartition, 1, 1, System.currentTimeMillis(), (long) 1, 1, 1);
    SendResult<String, String> sendResult = new SendResult<>(producerRecord, recordMetadata);

    future.set(sendResult);
    BDDMockito.given(kafkaTemplate.send(BDDMockito.anyString(), BDDMockito.anyString()))
        .willReturn(future);

    KafkaException kafkaException = new KafkaException("Test Exception");

    BDDMockito.doAnswer(invocationOnMock -> {
      FailureCallback failureCallback = invocationOnMock.getArgument(1);
      failureCallback.onFailure(kafkaException);
      return null;
    }).when(future).addCallback(BDDMockito.any(SuccessCallback.class),
        BDDMockito.any(FailureCallback.class));

    customerService.publishToKafka(new Customer());
    verify(kafkaTemplate, times(1)).send(BDDMockito.any(), BDDMockito.any());



  }


}
