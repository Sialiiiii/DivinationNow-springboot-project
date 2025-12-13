package divination.spring.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.dto.JiaziSignLogRequest;
import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.JiaziSign;
import divination.spring.project.model.User;
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
     */
    @PostMapping("/fortunestickjiazi/log")
    public ResponseEntity<Map<String, Object>> saveJiaziSignLog(
        @AuthenticationPrincipal User currentUser, 
        @RequestBody JiaziSignLogRequest request 
    ) {
        
        // 檢查使用者是否已認證
        if (currentUser == null || currentUser.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not authenticated. Please log in."));
        }
        
        Long userId = currentUser.getId(); 
        Long signIdLong = request.getSignId(); // 從 DTO 獲取 Long 型別的 ID
        
        if (signIdLong == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Missing required field: signId."));
        }
        
        try {
            // 將 Long 轉換為 Integer，以匹配 LogService 的方法簽名
            Integer signId = signIdLong.intValue(); 
            
            DivinationLog mainLog = logService.saveJiaziSignLog(userId, signId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", "Jiazi Sign Log saved successfully", 
                        "log_id", mainLog.getLogId())
            );

        } catch (ClassCastException e) {
             // 處理 Long 轉 Integer 溢位（雖然籤詩 ID 不可能溢位，但這是好習慣）
             System.err.println("Error casting sign ID: " + e.getMessage());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid sign ID format."));
        } catch (Exception e) {
            System.err.println("Error saving Jiazi sign log for user " + userId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Log failed due to server error. " + e.getMessage()));
        }
    }
}