//package com.ks.orderservice.controller;
//
//import com.ks.orderservice.dto.customer.CreateCustomerRequest;
//import com.ks.orderservice.dto.customer.CustomerResponse;
//import com.ks.orderservice.dto.customer.UpdateCustomerRequest;
//import com.ks.orderservice.mapper.CustomerMapper;
//import com.ks.orderservice.service.CustomerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final CustomerService customerService;
//    private final CustomerMapper customerMapper;
//
//    @GetMapping
//    public ResponseEntity<List<CustomerResponse>> getUsers() {
//        return ResponseEntity.ok(
//                customerMapper.toResponseList(
//                        customerService.getAllCustomers())
//        );
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CustomerResponse> getUserById(@PathVariable Long id) {
//        return ResponseEntity.ok(
//                customerMapper.toResponse(
//                        customerService.getCustomerById(id)));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody CreateCustomerRequest request) {
//        customerService.createNewCustomer(request);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//
//    }
//
//    @PostMapping("/{id}")
//    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
//        return ResponseEntity.ok(
//                customerService.updateCustomer(request, id));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
//        customerService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
