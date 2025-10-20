package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyUserResponseDTO {
    private boolean success;
    private boolean verified;
    private boolean userAlreadyExists;
    private String message;
    private String cnic;
}
