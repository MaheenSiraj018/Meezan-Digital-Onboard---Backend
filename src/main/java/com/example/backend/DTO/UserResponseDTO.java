package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String cnic;
    private String username;
    private String accountNumber;
    private String contactNumber;
    private String accountTitle;
}
