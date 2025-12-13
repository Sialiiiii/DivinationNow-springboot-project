package divination.spring.project.dto;

// 假設您使用的是 Jakarta EE 規範，這是 Spring Boot 3.x 的標準
import jakarta.validation.constraints.Size;

/**
 * 處理會員資料 PATCH/PUT 請求的 DTO
 */
public class UserUpdateRequest {
    
    // 允許更新暱稱 (對應 User.username 欄位)
    @Size(min = 2, max = 50, message = "暱稱長度必須在 2 到 50 個字元之間")
    private String username; 
    
    // 允許更新性別 (對應 User.gender 欄位)
    private String gender; 

    // 允許更新事業狀態 ID (對應 User.careerStatusId)
    private Integer careerStatusId;
    
    // 允許更新感情狀態 ID (對應 User.relationshipStatusId)
    private Integer relationshipStatusId;

    // --- Getters and Setters ---
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCareerStatusId() {
        return careerStatusId;
    }

    public void setCareerStatusId(Integer careerStatusId) {
        this.careerStatusId = careerStatusId;
    }

    public Integer getRelationshipStatusId() {
        return relationshipStatusId;
    }

    public void setRelationshipStatusId(Integer relationshipStatusId) {
        this.relationshipStatusId = relationshipStatusId;
    }
}