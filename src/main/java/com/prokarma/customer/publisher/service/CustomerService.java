package com.prokarma.customer.publisher.service;

import com.prokarma.customer.publisher.model.Customer;

public interface CustomerService {

  String publishToKafka(Customer customer);
}
