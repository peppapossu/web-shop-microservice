package com.ks.orderservice.service;


import com.ks.orderservice.dto.customer.CreateCustomerRequest;
import com.ks.orderservice.dto.customer.UpdateCustomerRequest;
import com.ks.orderservice.entity.customer.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer createNewCustomer(CreateCustomerRequest request);

    Customer updateCustomer(UpdateCustomerRequest request, Long id);

    void deleteById(Long id);
}
