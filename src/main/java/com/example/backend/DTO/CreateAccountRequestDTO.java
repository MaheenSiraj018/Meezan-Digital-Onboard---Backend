package com.example.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDTO {
    @NotBlank
    private String cnic; 

    @NotBlank(message = "Account title is required")
    private String accountTitle;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotBlank(message = "IBAN is required")
    private String iban;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Account status is required")
    private String accountStatus;
}
