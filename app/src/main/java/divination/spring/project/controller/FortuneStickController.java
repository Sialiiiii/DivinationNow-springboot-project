package divination.spring.project.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder; // 引入 SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.JiaziSign;
import divination.spring.project.service.FortuneStickService;
import divination.spring.project.service.LogService;

/**
 * 六十甲子籤 API
 */
@RestController
@RequestMapping("/divination")
public class FortuneStickController {

    private final FortuneStickService service;
    private final LogService logService; 

    public FortuneStickController(FortuneStickService service, LogService logService) {
        this.service = service;
        this.logService = logService;
    }

    /**
     * GET /divination/fortunestickjiazi (獲取籤詩列表)
     * 因為在 SecurityConfig 中設置了 .requestMatchers(HttpMethod.GET, "/divination/**").permitAll() 
     * 所以這是公開 API，不需要認證。
     */
    @GetMapping("/fortunestickjiazi")
    public ResponseEntity<List<JiaziSign>> getAllSigns() { 
        List<JiaziSign> signs = service.getAllSigns();
        return ResponseEntity.ok(signs);
    }

    /**
     * POST /divination/fortunestickjiazi/log
     * 紀錄六十甲子籤占卜結果
     * 這是受保護的 API，需要 JWT 認證。
     */
    @PostMapping("/fortunestickjiazi/log")
    public ResponseEntity<Map<String, Object>> saveJiaziSignLog(
        @RequestBody Map<String, Object> payload
    ) {
        
        // 1. 🚨 關鍵修正：直接從 Security Context 獲取 Long userId
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;

        if (principal instanceof Long) {
            userId = (Long) principal;
        } else if (principal instanceof Integer) {
            userId = ((Integer) principal).longValue();
        } 

        // 如果無法取得 Long 類型的 userId，則返回錯誤
        if (userId == null || principal.equals("anonymousUser")) {
            System.err.println("FATAL: Principal is null or wrong type. Cannot cast to Long userId. Class: " + (principal != null ? principal.getClass().getName() : "null"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User ID retrieval failed. Please relog."));
        }
        
        try {
            // 2. 從 Request Body 中讀取籤詩 ID
            Long signId = null;
            Object signIdObj = payload.get("sign_id");
            
            if (signIdObj instanceof Number) {
                signId = ((Number) signIdObj).longValue();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Missing or invalid required field (sign_id)."));
            }

            // 3. 呼叫 LogService 儲存紀錄
            DivinationLog mainLog = logService.saveJiaziSignLog(userId, signId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", "Jiazi Sign Log saved successfully", 
                    "log_id", mainLog.getLogId())
            );

        } catch (Exception e) {
            System.err.println("Error saving Jiazi sign log for user " + userId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Log failed due to server error: " + e.getMessage()));
        }
    }
}