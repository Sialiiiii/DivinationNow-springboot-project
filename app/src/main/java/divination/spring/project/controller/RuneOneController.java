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

import divination.spring.project.dto.RuneOneLogRequest;
import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.RuneOrientation;
import divination.spring.project.model.User;
import divination.spring.project.service.LogService;
import divination.spring.project.service.RuneOneService;

/**
 * 盧恩符文單顆占卜 API
 */
@RestController
@RequestMapping("/divination")
public class RuneOneController {

    private final RuneOneService service;
    private final LogService logService;

    public RuneOneController(RuneOneService service, LogService logService) {
        this.service = service;
        this.logService = logService;
    }

    /**
     * GET /divination/runesone
     * 獲取所有盧恩符文的狀態資料 (正位+逆位)
     */
    @GetMapping("/runesone")
    public ResponseEntity<List<RuneOrientation>> getAllRuneData() {
        List<RuneOrientation> runes = service.getAllRuneOrientations();
        return ResponseEntity.ok(runes);
    }




/**
     * POST /divination/runesone/log
     * 紀錄盧恩單顆占卜結果
     */
    @PostMapping("/runesone/log")
    public ResponseEntity<Map<String, Object>> saveRuneLog(
        @AuthenticationPrincipal User currentUser, 
        @RequestBody RuneOneLogRequest request 
    ) {
        
        // 檢查使用者是否已認證
        if (currentUser == null || currentUser.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not authenticated. Please log in."));
        }
        
        // 獲取核心參數
        Long userId = currentUser.getId();
        Long orientationIdLong = request.getOrientationId();
        
        if (orientationIdLong == null) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Missing required field: orientationId."));
        }
        
        try {
            Integer signId = orientationIdLong.intValue(); 
            
            DivinationLog mainLog = logService.saveRuneOneLog(userId, signId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", "Rune Log saved successfully", 
                        "log_id", mainLog.getLogId())
            );

        } catch (ClassCastException e) {
             System.err.println("Error casting sign ID: " + e.getMessage());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid sign ID format."));
        } catch (Exception e) {
            System.err.println("Error saving Rune log for user " + userId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Log failed due to server error. " + e.getMessage()));
        }
    }
}