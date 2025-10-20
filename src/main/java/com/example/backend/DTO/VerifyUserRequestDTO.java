package com.example.backend.DTO;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUserRequestDTO {
    @NotBlank(message = "CNIC is required")
    @Pattern(regexp = "^[1-9][0-9]{12}$", message = "CNIC must be 13 digits and cannot start with 0")
    private String cnic;

    @NotBlank(message = "Account number is required")
    @Pattern(
        regexp = "^[0-9]{14}$",
        message = "Account number must contain exactly 14 digits"
    )
    private String accountNumber;
}
