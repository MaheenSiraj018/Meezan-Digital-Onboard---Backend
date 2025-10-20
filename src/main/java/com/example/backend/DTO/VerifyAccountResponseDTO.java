package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyAccountResponseDTO {
    private boolean exists;
    private String message;
}
