package com.example.backend.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDTO {

    @NotBlank(message = "CNIC is required")
    @Pattern(regexp = "^[1-9][0-9]{12}$", message = "CNIC must be 13 digits and cannot start with 0")
    private String cnic;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 8, max = 16, message = "Username must be between 8 and 16 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
