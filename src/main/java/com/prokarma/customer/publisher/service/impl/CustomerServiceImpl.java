package com.prokarma.customer.publisher.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.prokarma.customer.publisher.common.AuthenticationHolder;
import com.prokarma.customer.publisher.common.JsonConverter;
import com.prokarma.customer.publisher.dto.AddressDto;
import com.prokarma.customer.publisher.dto.CustomerDto;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

  private KafkaTemplate<String, String> kafkaTemplate;

  private JsonConverter jsonConverter;

  private String customerTopic;

  private AuthenticationHolder authHoder;

  public CustomerServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
      JsonConverter jsonConverter, @Value("${kafka.customer.topic.name}") String customerTopic,
      AuthenticationHolder authHoder) {
    this.kafkaTemplate = kafkaTemplate;
    this.jsonConverter = jsonConverter;
    this.customerTopic = customerTopic;
    this.authHoder = authHoder;
  }

  @Override
  public String publishToKafka(Customer customer) {



    try {
      String customerJson = jsonConverter.toJson(convertToCustomerDto(customer));
      Message<String> customerMessage =
          MessageBuilder.withPayload(customerJson).setHeader(KafkaHeaders.TOPIC, customerTopic)
              .setHeader("user", authHoder.getUser()).build();

      kafkaTemplate.send(customerMessage).addCallback(this::onMessageSendSuccess,
          this::onException);
    } catch (Exception exception) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Error is processing request data.", exception);
    }

    return "Sending message to queue.";
  }

  private void onMessageSendSuccess(SendResult<String, String> sendResult) {
    LOGGER.info("Message Sent successfully to topic: {}", sendResult);
  }

  private void onException(Throwable throwable) {
    LOGGER.error("Error in sending message: {}", throwable, throwable);
    throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Unable to send message.",
        throwable);
  }

  private CustomerDto convertToCustomerDto(Customer customer) {
    CustomerDto customerDto = new CustomerDto();
    AddressDto addressDto = new AddressDto();
    customerDto.setAddress(addressDto);

    BeanUtils.copyProperties(customer, customerDto);
    BeanUtils.copyProperties(customer.getAddress(), customerDto.getAddress());
    return customerDto;
  }
}
