package com.example.backend.service;

import java.util.List;

import com.example.backend.DTO.CreateAccountRequestDTO;
import com.example.backend.DTO.CreateUserRequestDTO;
import com.example.backend.DTO.UserInfoDTO;
import com.example.backend.DTO.UserResponseDTO;
import com.example.backend.DTO.VerifyCnicResponseDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.entity.UserAccountView;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.UserAccountViewRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OnboardingService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserAccountViewRepository userAccountViewRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean hasAccount(String cnic) {
        String formattedCnic = formatCnic(cnic);
        return userRepo.findByCnic(formattedCnic).isPresent();
    }

    public VerifyCnicResponseDTO verifyCnic(String cnic) {
        String formattedCnic = formatCnic(cnic);

        Optional<Account> accountOpt = accountRepo.findByCnic(formattedCnic);

        if (accountOpt.isEmpty()) {
            return new VerifyCnicResponseDTO(true,false, "CNIC not found in records");
        }

        boolean userExists = userRepo.existsByCnic(formattedCnic);
        if (userExists) {
            return new VerifyCnicResponseDTO(true, false, "User already registered, please proceed to login");
        }

        return new VerifyCnicResponseDTO(true, true, "CNIC verified successfully. You may proceed.");
    }

    private String formatCnic(String cnic) {
        if (cnic == null)
            return null;

        String cleanCnic = cnic.replaceAll("\\D", "");

        if (cleanCnic.length() != 13) {
            throw new IllegalArgumentException("Invalid CNIC format. Must contain 13 digits.");
        }

        return cleanCnic;
    }

    public Optional<Account> verifyUser(String cnic, String accountNumber) {
        String formattedCnic = formatCnic(cnic);

        return accountRepo.findByCnicAndAccountNumber(formattedCnic, accountNumber);
    }

    public Optional<UserInfoDTO> getUserInfo(String cnic) {
        return accountRepo.findByCnic(cnic)
                .map(user -> new UserInfoDTO(
                        user.getContactNumber()));
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepo.existsByUsernameIgnoreCase(username);
    }

    public String createUser(CreateUserRequestDTO request) {
        try {
            String formattedCnic = formatCnic(request.getCnic());

            Account account = accountRepo.findByCnic(formattedCnic)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found for CNIC: " + formattedCnic));

            if (userRepo.findByCnic(formattedCnic).isPresent()) {
                throw new IllegalStateException("User already exists for this account.");
            }

            User user = new User();
            user.setAccount(account);
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreatedAt(LocalDateTime.now());

            userRepo.save(user);

            account.setAccountStatus("REGISTERED");
            accountRepo.save(account);

            return "User successfully created and account status updated to REGISTERED!";

        } catch (IllegalStateException | IllegalArgumentException ex) {
            return ex.getMessage();
        } catch (DataIntegrityViolationException ex) {
            return "Database constraint violated. Possibly duplicate CNIC or username.";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "An unexpected error occurred while creating the user.";
        }
    }

    public String createAccount(CreateAccountRequestDTO request) {
        try {
            String formattedCnic = formatCnic(request.getCnic());

            if (accountRepo.findByCnic(formattedCnic).isPresent()) {
                return "Account already exists for this CNIC.";
            }
            if (accountRepo.existsByAccountNumber(request.getAccountNumber())) {
                return "Account already exists with this number.";
            }

            Account account = new Account();
            account.setCnic(formattedCnic);
            account.setAccountTitle(request.getAccountTitle());
            account.setAccountNumber(request.getAccountNumber());
            account.setAccountType(request.getAccountType());
            account.setIban(request.getIban());
            account.setContactNumber(request.getContactNumber());
            account.setAccountStatus(request.getAccountStatus());
            account.setCreatedAt(LocalDateTime.now());

            accountRepo.save(account);

            return "Account created successfully for CNIC: " + formattedCnic;

        } catch (DataIntegrityViolationException ex) {
            return "Database constraint violated. Possibly duplicate CNIC or account number.";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "An unexpected error occurred while creating the account.";
        }
    }

    public UserResponseDTO getReview(String cnic) {
        return accountRepo.findByCnic(cnic)
                .map(this::mapToUserResponseDTO)
                .orElse(null);
    }

    public UserResponseDTO mapToUserResponseDTO(Account account) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setCnic(account.getCnic());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setContactNumber(account.getContactNumber());
        dto.setAccountTitle(account.getAccountTitle());

        var userOpt = userRepo.findByCnic(account.getCnic());
        dto.setUsername(userOpt.map(User::getUsername).orElse(null));

        return dto;
    }

    public List<UserAccountView> getAllUserAccountViewData() {
        return userAccountViewRepo.findAll();
    }

    public Optional<UserAccountView> getUserAccountByCnic(String cnic) {
        return userAccountViewRepo.findByCnic(cnic);
    }

}
