package divination.spring.project.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import divination.spring.project.model.User;
import divination.spring.project.service.StatusMapping;

public class UserResponse {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private Long id;
    private String email;
    private String username;
    private String memberSince;
    private String gender; 
    private Integer careerStatusId;
    private String careerStatusName; 
    private Integer relationshipStatusId;
    private String relationshipStatusName; 
    
    
    // --- Getters ---
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getMemberSince() { return memberSince; }
    public String getGender() { return gender; }
    public Integer getCareerStatusId() { return careerStatusId; }
    public String getCareerStatusName() { return careerStatusName; }
    public Integer getRelationshipStatusId() { return relationshipStatusId; }
    public String getRelationshipStatusName() { return relationshipStatusName; }
    
    // Setters - 供 UserService 使用
    public void setUsername(String username) { this.username = username; }
    public void setGender(String gender) { this.gender = gender; }
    public void setCareerStatusId(Integer careerStatusId) { this.careerStatusId = careerStatusId; }
    public void setRelationshipStatusId(Integer relationshipStatusId) { this.relationshipStatusId = relationshipStatusId; }
    public void setCareerStatusName(String careerStatusName) { this.careerStatusName = careerStatusName; }
    public void setRelationshipStatusName(String relationshipStatusName) { this.relationshipStatusName = relationshipStatusName; }
    

    // 構造函數 - 只從 User 實體獲取 ID/Email/ID，名稱則由 Service 注入 ---
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsernameJPA(); 

        if (user.getGender() != null) {
            this.gender = StatusMapping.getChineseName(user.getGender());
        } else {
            this.gender = "未填寫";
        }
        LocalDateTime date = user.getCreatedAt(); 
        this.memberSince = date != null ? date.format(DATE_FORMATTER) : "N/A";
        this.careerStatusId = user.getCareerStatusId();
        this.relationshipStatusId = user.getRelationshipStatusId();
        this.careerStatusName = null;
        this.relationshipStatusName = null;
    }
}