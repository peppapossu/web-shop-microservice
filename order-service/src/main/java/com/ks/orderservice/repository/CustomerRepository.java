package com.ks.orderservice.repository;

import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findCustomerById(Long id);

    Optional<Customer> findByAppUser(AppUser appUser);

    boolean existsByAppUser_Username(String appUserUsername);
}
