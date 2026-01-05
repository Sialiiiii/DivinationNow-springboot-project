package divination.spring.project.dto;

import jakarta.validation.constraints.Size;

/**
 * 處理會員資料 PATCH 請求的 DTO
 */
public class UserUpdateRequest {
    
    // 更新暱稱 (User.username )
    @Size(min = 2, max = 50, message = "暱稱長度必須在 2 到 50 個字元之間")
    private String username; 
    
    // 更新性別 (User.gender)
    private String gender; 

    // 更新事業狀態 ID (User.careerStatusId)
    private Integer careerStatusId;
    
    // 更新感情狀態 ID (User.relationshipStatusId)
    private Integer relationshipStatusId;

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getCareerStatusId() { return careerStatusId; }
    public void setCareerStatusId(Integer careerStatusId) { this.careerStatusId = careerStatusId; }

    public Integer getRelationshipStatusId() { return relationshipStatusId; }
    public void setRelationshipStatusId(Integer relationshipStatusId) { this.relationshipStatusId = relationshipStatusId; }
}