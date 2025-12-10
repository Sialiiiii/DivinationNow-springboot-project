package divination.spring.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    /**
     * 這是受保護的接口，只有攜帶有效 JWT 的使用者才能訪問
     */
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from protected endpoint! Your divination journey begins.");
    }
}