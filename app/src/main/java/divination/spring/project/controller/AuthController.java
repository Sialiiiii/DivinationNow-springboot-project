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

    // 註冊邏輯 (保持不變)
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

    /**
     * 🚀 登入邏輯 (Session 模式)
     * 認證成功後，Spring Security 會自動在 Response Header 中設置 JSESSIONID Cookie。
     * 此方法返回 User 資料，供前端 Pinia 儲存會員狀態。
     */
    // @PostMapping("/login")
    // public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
    //     try {
    //         User authenticatedUser = authService.loginUser(
    //             loginRequest.getEmail(),
    //             loginRequest.getPassword()
    //         );
            
    //         Map<String, Object> response = new HashMap<>();
            
    //         response.put("id", authenticatedUser.getId()); 
    //         response.put("username", authenticatedUser.getUsername());
    //         response.put("careerStatusId", authenticatedUser.getCareerStatusId());
    //         response.put("relationshipStatusId", authenticatedUser.getRelationshipStatusId());

    //         return ResponseEntity.ok(response);

    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
    //     }
    // }
}