package divination.spring.project.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.dto.AdminPostDTO;
import divination.spring.project.model.Admin;
import divination.spring.project.model.User;
import divination.spring.project.model.UserBlacklist;
import divination.spring.project.service.AdminService;

@RestController
@RequestMapping("/admin") 
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /admin/posts (取所有貼文)
     * 統一用 hasAuthority('ROLE_ADMIN')，與 Admin.java 的字串匹配
     */
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AdminPostDTO>> getPostsForAdmin(@AuthenticationPrincipal Admin adminPrincipal) {
        
        // --- 排查日誌 ---
        System.out.println("=== 儀表板訪問排查 ===");
        if (adminPrincipal != null) {
            System.out.println("登入管理員: " + adminPrincipal.getUsername());
            System.out.println("目前擁有的權限: " + adminPrincipal.getAuthorities());
        } else {
            System.out.println("🚨 警告: Principal 為空，請檢查 Cookie 是否正確發送 (withCredentials)");
        }
        System.out.println("====================");

        List<AdminPostDTO> posts = adminService.getAllPostsForAdmin();
        return ResponseEntity.ok(posts);
    }

    /**
     * DELETE /admin/posts/{postId} (刪除貼文)
     */
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boolean success = adminService.deletePostByAdmin(postId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * POST /admin/blacklist (加入黑名單)
     */
    @PostMapping("/blacklist")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> blacklistUser(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal Admin adminPrincipal) {

        if (adminPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "請先登入。"));
        }
        
        Long userId;
        try {
            userId = ((Number) payload.get("userId")).longValue();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "無效的 userId"));
        }
        
        String reason = (String) payload.get("reason");
        boolean success = adminService.blacklistUser(userId, adminPrincipal.getId(), reason);

        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "已加入黑名單"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "用戶已在黑名單中"));
        }
    }

    /**
     * DELETE /admin/blacklist/{userId} (移出黑名單)
     */
    @DeleteMapping("/blacklist/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> unblacklistUser(@PathVariable Long userId) {
        boolean success = adminService.unblacklistUser(userId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    /**
     * GET /admin/users (獲取所有會員列表)
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsersForAdmin() {
        // findAllUsers 方法寫在 AdminService
        List<User> users = adminService.findAllUsers(); 
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /admin/blacklist/detail/{userId} - (獲取用戶的黑名單詳細資訊)
     */
    @GetMapping("/blacklist/detail/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getBlacklistDetail(@PathVariable Long userId) {
        // getBlacklistDetail 方法在 AdminService 
        Optional<UserBlacklist> detail = adminService.getBlacklistDetail(userId);
        
        if (detail.isPresent()) {
            return ResponseEntity.ok(detail.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}