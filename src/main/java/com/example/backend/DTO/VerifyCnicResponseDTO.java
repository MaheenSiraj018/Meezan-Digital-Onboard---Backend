package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyCnicResponseDTO {
    private boolean success;      
    private boolean canProceed;
    private String message;
}
