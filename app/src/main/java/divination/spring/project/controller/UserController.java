package divination.spring.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import divination.spring.project.dto.StatusOptionDTO;

import divination.spring.project.model.User;
import divination.spring.project.dto.UserResponse;
import divination.spring.project.dto.UserUpdateRequest;
import divination.spring.project.dto.StatusOptionDTO;
import divination.spring.project.service.UserService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /user/profile
     * 獲取當前登入使用者的會員資料 (包含整合後狀態名稱)
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal User userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse response = userService.getUserProfile(userPrincipal.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /user/profile
     * 更新會員資料（暱稱、性別、事業/感情狀態 ID）
     */
    @PatchMapping("/profile") 
    public ResponseEntity<UserResponse> updateProfile(
        @RequestBody @Valid UserUpdateRequest request, 
        @AuthenticationPrincipal User userPrincipal) {
        
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }

        UserResponse updatedProfile = userService.updateProfile(userPrincipal.getId(), request);
        return ResponseEntity.ok(updatedProfile);
    }
    
    /**
     * GET /user/statuses
     * 獲取所有事業和感情狀態選項，給前端下拉選單使用
     */
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusOptionDTO>> getAllStatuses() {
        List<StatusOptionDTO> statuses = userService.getAllStatusOptions();
        return ResponseEntity.ok(statuses);
    }
}