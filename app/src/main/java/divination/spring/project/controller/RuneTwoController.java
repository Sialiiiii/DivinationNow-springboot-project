package divination.spring.project.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.dto.RuneDoubleLogRequest;
import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.SpecificRuneReading;
import divination.spring.project.service.RuneTwoService;
import divination.spring.project.service.LogService;
// import java.security.Principal; // 移除未使用的導入
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import divination.spring.project.model.User; // 確保 User 實體路徑正確

/**
 * 盧恩符文雙顆占卜 API
 */
@RestController
@RequestMapping("/divination")
public class RuneTwoController {

    private final RuneTwoService service;
    private final LogService logService;

    public RuneTwoController(RuneTwoService service, LogService logService) {
        this.service = service;
        this.logService = logService;
    }

    /**
     * GET /divination/runes/reading
     * 根據三個參數查詢 specific_rune_readings 表格
     */
    @GetMapping("/runes/reading")
    public ResponseEntity<SpecificRuneReading> getSpecificRuneReading(
            @RequestParam("orientation_id") Integer orientationId,
            @RequestParam("status_id") Integer statusId,
            @RequestParam("is_current_status_position") Integer position) { 

        Optional<SpecificRuneReading> reading = service.getSpecificRuneReading(orientationId, statusId, position);

        // 如果找到特定解讀，回傳 200 和解讀內容
        if (reading.isPresent()) {
            return ResponseEntity.ok(reading.get());
        } else {
            // 如果找不到特定解讀，回傳 404，前端會 fallback 到通用解讀
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * POST /divination/runes/double-log 
     * ⭐ 修正：確保 User 實體有公開的 getId() 方法
     */
    @PostMapping("/runes/double-log") // 使用您要求的路徑
    public ResponseEntity<DivinationLog> logRuneDoubleResult(
            @RequestBody RuneDoubleLogRequest request,
            @AuthenticationPrincipal User userPrincipal) { 
        
        // 檢查登入狀態：Spring Security 應該已經處理了未認證請求
        if (userPrincipal == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // ⭐ 獲取 User ID (假設 User.getId() 存在)
        Long userId = userPrincipal.getId(); 

        // 基礎檢查
        if (request.getRune1SpecificReadingId() == null) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        try {
            // 將獲取的 userId 作為第一個參數傳遞給 Service
            DivinationLog masterLog = logService.saveRuneDoubleLog(userId, request);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(masterLog);
            
        } catch (Exception e) {
            System.err.println("儲存盧恩雙顆占卜紀錄失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}