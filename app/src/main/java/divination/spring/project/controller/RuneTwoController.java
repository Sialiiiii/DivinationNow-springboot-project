package divination.spring.project.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import divination.spring.project.model.SpecificRuneReading;
import divination.spring.project.service.RuneTwoService;

/**
 * 盧恩符文雙顆占卜 API
 */
@RestController
@RequestMapping("/divination")
public class RuneTwoController {

    private final RuneTwoService service;

    public RuneTwoController(RuneTwoService service) {
        this.service = service;
    }

    // --- 1. GET: 獲取特定解讀 (由 fetchRuneReading 呼叫) ---

    /**
     * GET /divination/runes/reading
     * 根據三個參數查詢 specific_rune_readings 表格
     */
    @GetMapping("/runes/reading")
    public ResponseEntity<SpecificRuneReading> getSpecificRuneReading(
            @RequestParam("orientation_id") Integer orientationId,
            @RequestParam("status_id") Integer statusId,
            @RequestParam("is_current_status_position") Integer position) { // 牌位: 1=現況/基礎, 0=建議/指引

        Optional<SpecificRuneReading> reading = service.getSpecificRuneReading(orientationId, statusId, position);

        // 如果找到特定解讀，回傳 200 和解讀內容
        if (reading.isPresent()) {
            return ResponseEntity.ok(reading.get());
        } else {
            // 如果找不到特定解讀，回傳 404，前端會 fallback 到通用解讀
            return ResponseEntity.notFound().build();
        }
    }

}