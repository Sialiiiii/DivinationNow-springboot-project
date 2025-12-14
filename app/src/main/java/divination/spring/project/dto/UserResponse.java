package divination.spring.project.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import divination.spring.project.model.User;
import divination.spring.project.service.StatusMapping;

public class UserResponse {
    
    // ⭐ 修正點 1: 正確初始化 DateTimeFormatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private Long id;
    private String email;
    private String username; // 會員暱稱
    private String memberSince;
    
    // ⭐ 新增欄位 1: 性別
    private String gender; 
    
    private Integer careerStatusId;
    // ⭐ 新增欄位 2: 事業狀態名稱 (從 Status Entity 轉換而來)
    private String careerStatusName; 
    
    private Integer relationshipStatusId;
    // ⭐ 新增欄位 3: 感情狀態名稱 (從 Status Entity 轉換而來)
    private String relationshipStatusName; 
    
    
    // --- Getters (必須新增所有新欄位的 Getter) ---
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; } // 這是暱稱的 Getter
    public String getMemberSince() { return memberSince; }
    
    public String getGender() { return gender; } // 性別 Getter
    
    public Integer getCareerStatusId() { return careerStatusId; }
    public String getCareerStatusName() { return careerStatusName; } // 狀態名稱 Getter
    
    public Integer getRelationshipStatusId() { return relationshipStatusId; }
    public String getRelationshipStatusName() { return relationshipStatusName; } // 狀態名稱 Getter
    
    // --- Setters (供 UserService 使用，將狀態名稱寫入 DTO) ---
    public void setUsername(String username) { this.username = username; }
    public void setGender(String gender) { this.gender = gender; }
    public void setCareerStatusId(Integer careerStatusId) { this.careerStatusId = careerStatusId; }
    public void setRelationshipStatusId(Integer relationshipStatusId) { this.relationshipStatusId = relationshipStatusId; }

    // ⭐ 新增 Setters for Status Names (供 UserService 呼叫)
    public void setCareerStatusName(String careerStatusName) { this.careerStatusName = careerStatusName; }
    public void setRelationshipStatusName(String relationshipStatusName) { this.relationshipStatusName = relationshipStatusName; }
    
    // --- 構造函數 (只從 User 實體獲取 ID/Email/ID，名稱則由 Service 注入) ---
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
        
        // **注意：Status Name 在這裡保持 null，由 UserService 查詢後通過 Setter 注入**
        this.careerStatusName = null;
        this.relationshipStatusName = null;
    }
}