package com.prokarma.customer.publisher.service.impl;

import org.apache.kafka.common.KafkaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.prokarma.customer.publisher.common.JsonConverter;
import com.prokarma.customer.publisher.dto.CustomerDto;
import com.prokarma.customer.publisher.model.Customer;
import com.prokarma.customer.publisher.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

  private KafkaTemplate<String, String> kafkaTemplate;

  private JsonConverter jsonConverter;

  private String customerTopic;

  public CustomerServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
      JsonConverter jsonConverter, @Value("${kafka.customer.topic.name}") String customerTopic) {
    super();
    this.kafkaTemplate = kafkaTemplate;
    this.jsonConverter = jsonConverter;
    this.customerTopic = customerTopic;
  }

  @Override
  public void publishToKafka(Customer customer) {

    String customerJson = jsonConverter.toJson(convertToCustomerDto(customer));

    kafkaTemplate.send(customerTopic, customerJson)
        .addCallback(result -> LOGGER.info(String.format("Sent message=[%s] with offset=[%s]",
            customerJson, result.getRecordMetadata().offset())), ex -> {
              LOGGER.error(String.format("Unable to send message=[%s] due to : %s", customerJson,
                  ex.getMessage()));
              throw new KafkaException(ex); // TODO: Do we really need to handle?
            });
  }

  private CustomerDto convertToCustomerDto(Customer customer) {
    CustomerDto customerDto = new CustomerDto();
    BeanUtils.copyProperties(customer, customerDto);
    return customerDto;
  }
}
