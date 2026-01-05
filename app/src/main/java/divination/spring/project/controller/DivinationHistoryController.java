// divination/spring/project/controller/DivinationHistoryController.java

package divination.spring.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public DivinationHistoryController(DivinationHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * GET /divination/history
     * 獲取當前使用者的所有占卜紀錄
     */
    @GetMapping
    public ResponseEntity<Map<String, List<DivinationHistoryDTO>>> getHistory(
            @AuthenticationPrincipal User userPrincipal) {
        
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build(); 
        }
        
        List<DivinationHistoryDTO> records = historyService.getHistoryRecords(userPrincipal.getId());
        
        return ResponseEntity.ok(Collections.singletonMap("records", records));
    }

    /**
     * PATCH /divination/history/{logId}
     * 接收的 Payload 格式：{"question": "新問題"}
     */
    @PatchMapping("/{logId}")
    public ResponseEntity<Void> updateQuestion(
        @PathVariable Long logId,
        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal User userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }
        
        String newQuestion = body.get("question");
        
        if (newQuestion == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean success = historyService.updateQuestion(logId, userPrincipal.getId(), newQuestion);
        
        if (success) {
            // 200 OK (更新成功)
            return ResponseEntity.ok().build();
        } else {
            // 404 Not Found (Log ID 不存在或不屬於該使用者)
            return ResponseEntity.notFound().build(); 
        }
    }
}