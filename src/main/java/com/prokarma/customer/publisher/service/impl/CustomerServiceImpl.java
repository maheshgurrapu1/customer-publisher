package com.prokarma.customer.publisher.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private Gson jsonConverter;

  @Value("${kafka.customer.topic.name}")
  private String customerTopic;

  @Override
  public void publishToKafka(Customer customer) {
    String customerJson = jsonConverter.toJson(customer);
    kafkaTemplate.send(customerTopic, customerJson).addCallback(
        result -> LOGGER.info(String.format("Sent message=[%s] with offset=[%s]", customerJson,
            result.getRecordMetadata().offset())),
        ex -> LOGGER.error(String.format("Unable to send message=[%s] due to : %s", customerJson,
            ex.getMessage())));
  }

}
