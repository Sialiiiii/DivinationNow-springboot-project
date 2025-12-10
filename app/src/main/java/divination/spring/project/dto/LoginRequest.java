package divination.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email 不能為空")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不能為空")
    private String password;

    // --- Getter 和 Setter (請在你的 IDE 中自動生成) ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
