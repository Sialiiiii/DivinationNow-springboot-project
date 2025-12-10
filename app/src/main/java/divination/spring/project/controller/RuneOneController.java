package divination.spring.project.controller;

import divination.spring.project.model.RuneOrientation;
import divination.spring.project.service.RuneOneService; // 引用新的 Service
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 盧恩符文單顆占卜 API
 */
@RestController
@RequestMapping("/divination")
public class RuneOneController {

    private final RuneOneService service;

    public RuneOneController(RuneOneService service) {
        this.service = service;
    }

    /**
     * GET /divination/runesone
     * 獲取所有盧恩符文的狀態資料 (正位和逆位)
     */
    @GetMapping("/runesone")
    public ResponseEntity<List<RuneOrientation>> getAllRuneData() {
        List<RuneOrientation> runes = service.getAllRuneOrientations();
        return ResponseEntity.ok(runes);
    }
}