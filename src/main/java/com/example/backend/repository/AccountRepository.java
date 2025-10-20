package com.example.backend.repository;

import com.example.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByCnic(String cnic);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByCnicAndAccountNumber(String cnic, String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

}
