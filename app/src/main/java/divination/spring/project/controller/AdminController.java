package divination.spring.project.controller;

import divination.spring.project.dto.AdminPostDTO;
import divination.spring.project.model.Admin; 
import divination.spring.project.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin") 
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /admin/posts - 獲取所有貼文
     * 💡 統一使用 hasAuthority('ROLE_ADMIN')，因為這能與 Admin.java 的字串精確匹配
     */
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AdminPostDTO>> getPostsForAdmin(@AuthenticationPrincipal Admin adminPrincipal) {
        
        // --- 排查日誌：請務必看後端控制台輸出 ---
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
     * DELETE /admin/posts/{postId} - 刪除貼文
     */
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 💡 修正點：加上 ROLE_ 前綴並改用 Authority
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boolean success = adminService.deletePostByAdmin(postId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * POST /admin/blacklist - 將用戶加入黑名單
     */
    @PostMapping("/blacklist")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 💡 修正點：一致化
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
     * DELETE /admin/blacklist/{userId} - 移出黑名單
     */
    @DeleteMapping("/blacklist/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 💡 修正點：一致化
    public ResponseEntity<Void> unblacklistUser(@PathVariable Long userId) {
        boolean success = adminService.unblacklistUser(userId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}