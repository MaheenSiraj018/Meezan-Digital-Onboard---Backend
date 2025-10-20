package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(length = 15, nullable = false, unique = true)
    private String cnic; // Primary Key

    @Column(name = "account_title", nullable = false)
    private String accountTitle;

    @Column(name = "account_number", unique = true, length = 14, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "account_status", nullable = false)
    private String accountStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
