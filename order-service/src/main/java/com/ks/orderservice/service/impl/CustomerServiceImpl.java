package com.ks.orderservice.service.impl;

import com.ks.orderservice.dto.customer.CreateCustomerRequest;
import com.ks.orderservice.dto.customer.UpdateCustomerRequest;
import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.entity.appUser.Role;
import com.ks.orderservice.entity.customer.Customer;
import com.ks.orderservice.mapper.CustomerMapper;
import com.ks.orderservice.repository.AppUserRepository;
import com.ks.orderservice.repository.CustomerRepository;
import com.ks.orderservice.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;


    public Customer createNewCustomer(CreateCustomerRequest request) {
        AppUser user = AppUser.builder()
                .username(request.name())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_USER))
                .build();
        appUserRepository.save(user);

        Customer customer = Customer.builder()
                .email(request.email())
                .appUser(user)
                .build();

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UpdateCustomerRequest request, Long id) {
        Customer customer = getCustomerById(id);
        customer.setEmail(request.email());
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCurrentCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("AppUser not found, username: " + username));
        return customerRepository.findByAppUser(appUser)
                .orElseThrow(()-> new EntityNotFoundException("Customer not found, username: " + username));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Customer not found, id: " + id)) ;
    }

    public boolean isExist (String username) {
       return customerRepository.existsByAppUser_Username(username);
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = getCustomerById(id);
        appUserRepository.deleteById(customer.getAppUser().getId());
        customerRepository.deleteById(id);
    }
}
