package com.example.backend.repository;

import com.example.backend.entity.User;
import com.example.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByCnic(String cnic);

    Optional<User> findByCnic(String cnic);

    Optional<User> findByAccount(Account account);

}

