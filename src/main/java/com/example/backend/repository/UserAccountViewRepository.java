package com.example.backend.repository;
import java.util.Optional;
import com.example.backend.entity.UserAccountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountViewRepository extends JpaRepository<UserAccountView, String> {
    Optional<UserAccountView> findByCnic(String cnic);
}
