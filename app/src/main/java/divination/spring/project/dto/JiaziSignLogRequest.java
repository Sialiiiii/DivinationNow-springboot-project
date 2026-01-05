package divination.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JiaziSignLogRequest {
    
    // 使用 @JsonProperty 註解來匹配前端的 snake_case 命名；就算前端傳送 "sign_id" 也能正確 mapping 到 signId
    @JsonProperty("sign_id") 
    private Long signId; 
    
    public Long getSignId() {
        return signId;
    }
    
    public void setSignId(Long signId) {
        this.signId = signId;
    }
    
    public JiaziSignLogRequest() {}
}