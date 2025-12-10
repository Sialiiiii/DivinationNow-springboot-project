package divination.spring.project.controller;

import java.util.HashMap;
import java.util.Map;

import divination.spring.project.dto.RegisterRequest;
import divination.spring.project.dto.LoginRequest;
import divination.spring.project.model.User;
import divination.spring.project.service.AuthService;
import jakarta.validation.Valid; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // 1. 構建 User Entity (暫不帶 Status ID，由 Service 查找)
            User user = new User(
                registerRequest.getEmail(),
                registerRequest.getPassword(), 
                registerRequest.getUsername(),
                
                // 新增的欄位
                registerRequest.getDateOfBirth(), 
                registerRequest.getGender(),
                null, // careerStatusId 暫為 null
                null  // relationshipStatusId 暫為 null
            );

            // 2. 🚀 呼叫 Service，傳遞 Status Value (String)
            User registeredUser = authService.registerUser(
                user, 
                registerRequest.getCareerStatusValue(), 
                registerRequest.getRelationshipStatusValue()
            );

            // 3. 回傳成功響應
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. ID: " + registeredUser.getId());

        } catch (RuntimeException e) {
            // 處理 Status Value 找不到 (RuntimeException) 或 Email 重複等錯誤
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 登入邏輯
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // ... (省略內部邏輯)
        try {
            String jwtToken = authService.loginUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            );
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
        }
    }
}