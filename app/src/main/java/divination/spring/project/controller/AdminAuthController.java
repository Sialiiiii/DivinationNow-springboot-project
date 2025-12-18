package divination.spring.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map; // ⭐ 使用內建的 Map 替代未創建的 LoginDTO

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {
    
    // ⭐ 移除對 AdminAuthService 的依賴和構造函數

    // ⭐ 移除 AdminAuthService 的依賴，使用 Map<String, String> 接收登入資料
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> loginDto) {
    // 由於這個接口會被 Spring Security 的 Filter 攔截，正常情況下這段代碼不會被執行。
        // 登入成功與否的響應已經在 SecurityConfig.java 的 Handler 中處理。
    
    // 如果代碼執行到這裡，返回一個中立的響應，讓 Filter 正常工作。
    return ResponseEntity.ok().build();
  }
}