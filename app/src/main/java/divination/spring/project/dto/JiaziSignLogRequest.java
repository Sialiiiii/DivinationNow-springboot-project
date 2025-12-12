package divination.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JiaziSignLogRequest {
    
    // 🚀 關鍵：使用 @JsonProperty 註解來匹配前端的 snake_case 命名
    // 這樣即使前端傳送 "sign_id"，Java 內部也能正確 mapping 到 signId
    @JsonProperty("sign_id") 
    private Long signId; 
    
    // Getter and Setter
    public Long getSignId() {
        return signId;
    }
    
    public void setSignId(Long signId) {
        this.signId = signId;
    }
    
    // 建議新增一個無參數建構函式供 Jackson 使用
    public JiaziSignLogRequest() {}
}