package com.prokarma.customer.publisher.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private Gson jsonConverter;

  @Override
  public void publishToKafka(Customer customer) {
    String customerJson = jsonConverter.toJson(customer);
    kafkaTemplate.send("customer_topic", customerJson).addCallback(
        result -> LOGGER.info(String.format("Sent message=[%s] with offset=[%s]", customerJson,
            result.getRecordMetadata().offset())),
        ex -> LOGGER.error(String.format("Unable to send message=[%s] due to : %s", customerJson,
            ex.getMessage())));
  }

}
