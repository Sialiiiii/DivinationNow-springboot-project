package divination.spring.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // 新增：用於部分更新
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import divination.spring.project.dto.StatusOptionDTO;

import divination.spring.project.model.User;
import divination.spring.project.dto.UserResponse;
import divination.spring.project.dto.UserUpdateRequest; // 新增：更新請求 DTO
import divination.spring.project.dto.StatusOptionDTO; // 新增：狀態選項 DTO
import divination.spring.project.service.UserService; // 新增：Service 依賴
import jakarta.validation.Valid; // 新增：用於驗證更新請求

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 依賴注入 UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /user/profile
     * 獲取當前登入使用者的會員資料 (包含整合後的狀態名稱)
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal User userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }
        
        // ⭐ 修正點 1: 調用 UserService 處理查詢邏輯和狀態名稱整合
        UserResponse response = userService.getUserProfile(userPrincipal.getId());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /user/profile
     * ⭐ 新增接口 1: 更新會員資料（暱稱、性別、事業/感情狀態 ID）
     */
    @PatchMapping("/profile") 
    public ResponseEntity<UserResponse> updateProfile(
        // @Valid 觸發 DTO 中的驗證規則 (例如 @Size)
        @RequestBody @Valid UserUpdateRequest request, 
        @AuthenticationPrincipal User userPrincipal) {
        
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }
        
        // 調用 Service 執行更新操作，並返回最新的 Profile DTO
        UserResponse updatedProfile = userService.updateProfile(userPrincipal.getId(), request);
        return ResponseEntity.ok(updatedProfile);
    }
    
    /**
     * GET /user/statuses
     * ⭐ 新增接口 2: 獲取所有事業和感情狀態選項，供前端下拉選單使用
     */
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusOptionDTO>> getAllStatuses() {
        List<StatusOptionDTO> statuses = userService.getAllStatusOptions();
        return ResponseEntity.ok(statuses);
    }
}