package com.prokarma.customer.publisher.service;

import com.prokarma.customer.publisher.model.Customer;

public interface CustomerService {

  void publishToKafka(Customer customer);
}
