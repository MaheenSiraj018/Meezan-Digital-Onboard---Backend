package com.example.backend.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vw_user_account_summary")
@Data
@Immutable
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountView {

    @Id
    @Column(name = "cnic")
    private String cnic;

    @Column(name = "account_title")
    private String accountTitle;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "iban")
    private String iban;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "username")
    private String username;

    @Column(name = "user_created_at")
    private String userCreatedAt;
}
