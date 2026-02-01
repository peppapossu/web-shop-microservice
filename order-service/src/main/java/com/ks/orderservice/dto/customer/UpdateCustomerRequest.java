package com.ks.orderservice.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Wrong format, should be xxx@xxx.xx")
        String email,

        @NotBlank(message = "Role is mandatory")
        String role
) {
}
