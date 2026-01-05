package divination.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class RegisterRequest {
    
    @NotBlank(message = "Email 不能為空")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, message = "密碼長度至少為 6 位")
    private String password;

    @NotBlank(message = "暱稱不能為空")
    private String username; 

    @NotNull(message = "生日不能為空")
    private LocalDate dateOfBirth;

    @NotBlank(message = "性別不能為空")
    private String gender;
    
    @NotBlank(message = "事業狀態不能為空")
    private String careerStatusValue; // 事業狀態的字串 (EMPLOYED, STUDENT...)

    @NotBlank(message = "感情狀態不能為空")
    private String relationshipStatusValue; // 感情狀態的字串 (SINGLE, MARRIED...)
    

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCareerStatusValue() { return careerStatusValue; }
    public void setCareerStatusValue(String careerStatusValue) { this.careerStatusValue = careerStatusValue; }

    public String getRelationshipStatusValue() { return relationshipStatusValue; }
    public void setRelationshipStatusValue(String relationshipStatusValue) { this.relationshipStatusValue = relationshipStatusValue; }
}