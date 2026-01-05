package divination.spring.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map; 

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {
    
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> loginDto) {
    return ResponseEntity.ok().build();
  }
}