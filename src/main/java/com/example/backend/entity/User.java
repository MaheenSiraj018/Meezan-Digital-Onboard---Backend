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
@Table(name = "users") // 'user' is reserved in PostgreSQL
public class User {

    @Id
    @Column(length = 15, nullable = false, unique = true)
    private String cnic; // Primary Key (same as Account.cnic)

    @OneToOne
    @MapsId
    @JoinColumn(name = "cnic")
    private Account account;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}