// divination/spring/project/controller/PostController.java

package divination.spring.project.controller;

import divination.spring.project.dto.PostDTO;
import divination.spring.project.model.User;
import divination.spring.project.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts") // ⭐ 與 Vite proxy 配置匹配
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * POST /api/posts - 發布新貼文
     * Payload: {"content": "文字內容..."}
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal User userPrincipal) {
        
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入。");
        }
        
        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "貼文內容不能為空。"));
        }

        try {
            postService.createPost(userPrincipal.getId(), content.trim());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "貼文發布成功。"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * GET /api/posts - 獲取所有貼文列表
     */
    @GetMapping
    public ResponseEntity<List<PostDTO>> getPosts(@AuthenticationPrincipal User userPrincipal) {
        Long currentUserId = userPrincipal != null ? userPrincipal.getId() : null;
        List<PostDTO> posts = postService.getPostDTOsForUser(currentUserId);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * PATCH /api/posts/{postId}/reaction - 點讚/取消點讚/更換表情符號
     * Payload: {"reactionType": "LOVE" | "EMOTION" | "FUNNY"}
     */
    @PatchMapping("/{postId}/reaction")
    public ResponseEntity<?> reactToPost(
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal User userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入才能互動。");
        }
        
        String reactionType = payload.get("reactionType");
        if (reactionType == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "缺少 reactionType 參數。"));
        }
        
        if (!List.of("LOVE", "EMOTION", "FUNNY").contains(reactionType.toUpperCase())) {
             return ResponseEntity.badRequest().body(Map.of("message", "無效的 reactionType 參數。"));
        }

        try {
            Map<String, Integer> counts = postService.handleReaction(postId, userPrincipal.getId(), reactionType.toUpperCase());
            return ResponseEntity.ok(counts); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}