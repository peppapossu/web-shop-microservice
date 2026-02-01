package com.ks.orderservice.mapper;

import com.ks.orderservice.dto.customer.CustomerResponse;
import com.ks.orderservice.entity.customer.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}
