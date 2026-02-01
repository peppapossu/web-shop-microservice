package com.ks.orderservice.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Password is mandatory")
        @Min(value = 8, message = "Min 8 symbols and max 12 ")
        @Max(value = 12, message = "Min 8 symbols and max 12 ")
        String password,

        @Email(message = "Email is mandatory")
        @NotBlank
        String email,

        @NotBlank(message = "Role is mandatory")
        String role
) {
}
