package divination.spring.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.dto.RegisterRequest;
import divination.spring.project.model.User;
import divination.spring.project.service.AuthService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth") 
public class AuthController {

    private final AuthService authService;

    @Autowired 
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 註冊邏輯
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // 1. 構建 User Entity
            User user = new User(
                registerRequest.getEmail(),
                registerRequest.getPassword(), 
                registerRequest.getUsername(),
                registerRequest.getDateOfBirth(), 
                registerRequest.getGender(),
                null, 
                null 
            );

            // 2. 呼叫 Service
            User registeredUser = authService.registerUser(
                user, 
                registerRequest.getCareerStatusValue(), 
                registerRequest.getRelationshipStatusValue()
            );

            // 3. 回傳成功響應
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. ID: " + registeredUser.getId());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}