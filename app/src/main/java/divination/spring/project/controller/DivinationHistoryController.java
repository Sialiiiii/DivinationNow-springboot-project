package divination.spring.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.dto.DivinationHistoryDTO;
import divination.spring.project.model.User;
import divination.spring.project.service.DivinationHistoryService;

@RestController
@RequestMapping("/divination/history")
public class DivinationHistoryController {

    private final DivinationHistoryService historyService;

    // 依賴注入 (Constructor Injection)
    public DivinationHistoryController(DivinationHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * GET /divination/history
     * 獲取當前使用者的所有占卜紀錄 (DTOs 包含詳細結果)
     */
    @GetMapping
    public ResponseEntity<Map<String, List<DivinationHistoryDTO>>> getHistory(
            @AuthenticationPrincipal User userPrincipal) {
        
        // Spring Security 應該在進入 Controller 前就檢查認證，但仍進行安全檢查
        if (userPrincipal == null) {
            // 返回 401 Unauthorized (雖然 Security Config 通常會先行處理)
            return ResponseEntity.status(401).build(); 
        }
        
        // 呼叫 Service 獲取詳細的歷史紀錄列表
        List<DivinationHistoryDTO> records = historyService.getHistoryRecords(userPrincipal.getId());
        
        // 前端 (Vue) 期待 { records: [...] } 結構
        return ResponseEntity.ok(Collections.singletonMap("records", records));
    }

    /**
     * PUT /divination/history/{logId}
     * 更新占卜紀錄的問題敘述 (question)
     * 接收的 Payload 格式：{"question": "新問題"}
     */
    @PutMapping("/{logId}")
    public ResponseEntity<Void> updateQuestion(
        @PathVariable Long logId,
        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal User userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }
        
        String newQuestion = body.get("question");
        
        if (newQuestion == null) {
             // 請求體缺少 'question' 欄位
             return ResponseEntity.badRequest().build();
        }

        // 呼叫 Service 進行更新，並確保只有該紀錄的擁有者可以修改
        boolean success = historyService.updateQuestion(logId, userPrincipal.getId(), newQuestion);
        
        if (success) {
            // 200 OK，更新成功
            return ResponseEntity.ok().build();
        } else {
            // 404 Not Found，該 Log ID 不存在或不屬於該使用者
            return ResponseEntity.notFound().build(); 
        }
    }
}