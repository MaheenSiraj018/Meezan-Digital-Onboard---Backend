package com.example.backend.controller;
import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.DTO.CreateAccountRequestDTO;
import com.example.backend.DTO.CreateUserRequestDTO;
import com.example.backend.DTO.UserResponseDTO;
import com.example.backend.DTO.VerifyAccountResponseDTO;
import com.example.backend.DTO.VerifyCnicResponseDTO;
import com.example.backend.DTO.VerifyUserRequestDTO;
import com.example.backend.DTO.VerifyUserResponseDTO;
import com.example.backend.service.OnboardingService;
import com.example.backend.DTO.UserInfoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OnboardingController {

    private final OnboardingService service;

    @PostMapping("/verify-cnic")
public ResponseEntity<VerifyCnicResponseDTO> verifyCnic(@Valid @RequestBody Map<String, String> req) {
    String cnic = req.get("cnic");

    VerifyCnicResponseDTO response = service.verifyCnic(cnic);

    return ResponseEntity.ok(response);
}


    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserRequestDTO req) {
        var accountOpt = service.verifyUser(req.getCnic(), req.getAccountNumber());

        if (accountOpt.isPresent()) {
            var account = accountOpt.get();
            boolean userAlreadyExists = service.hasAccount(account.getCnic());

            String message = userAlreadyExists
                    ? "User verified but account already exists."
                    : "User verified successfully.";

            return ResponseEntity.ok(
                    new VerifyUserResponseDTO(true, true, userAlreadyExists, message, account.getCnic()));
        } else {
            return ResponseEntity.ok(
                    new VerifyUserResponseDTO(true,false, false, "CNIC and Account Number do not match.", null));
        }
    }

    @GetMapping("/user-info/{cnic}")
    public ResponseEntity<?> getUserInfo(@PathVariable String cnic) {
        var userInfoOpt = service.getUserInfo(cnic);

        if (userInfoOpt.isPresent()) {
            return ResponseEntity.ok(userInfoOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found."));
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = service.isUsernameAvailable(username);
        return ResponseEntity.ok(Map.of("available", available));
    }

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@Valid @RequestBody CreateAccountRequestDTO request) {
        String result = service.createAccount(request);

        if (result.contains("already exists")) {
            return ResponseEntity.status(409).body(result);
        } else if (result.contains("unexpected error") || result.contains("Database constraint")) {
            return ResponseEntity.internalServerError().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        String result = service.createUser(request);

        if (result.contains("already exists")) {
            return ResponseEntity.status(409).body(result);
        } else if (result.contains("Account not found")) {
            return ResponseEntity.badRequest().body(result);
        } else if (result.contains("unexpected error") || result.contains("Database constraint")) {
            return ResponseEntity.internalServerError().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/review/{cnic}")
    public ResponseEntity<?> review(@PathVariable String cnic) {
        UserResponseDTO dto = service.getReview(cnic);

        if (dto == null) {
            String msg = "Digital Onboarding failed. Proceed to register again.";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(msg);
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/view-user-accounts")
    public ResponseEntity<?> getUserAccountViewData() {
        try {
            var viewData = service.getAllUserAccountViewData();
            if (viewData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(Map.of("message", "No user-account records found."));
            }
            return ResponseEntity.ok(viewData);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch data from user-account view."));
        }
    }

    @GetMapping("/view-user-accounts/{cnic}")
    public ResponseEntity<?> getUserAccountByCnic(@PathVariable String cnic) {
        try {
            var record = service.getUserAccountByCnic(cnic);
            if (record.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No record found for CNIC: " + cnic));
            }
            return ResponseEntity.ok(record.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch record for CNIC: " + cnic));
        }
    }

}
