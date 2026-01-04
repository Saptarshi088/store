package com.saptarshi.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterReqiest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "name length must be between 3 and 100")
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank
    @Size(min = 6, message = "password must be longer that 6 characters")
    private String password;
}
