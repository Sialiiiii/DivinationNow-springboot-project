package divination.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuneOneLogRequest {
    
    @JsonProperty("orientation_id")
    private Long orientationId; 

    // Getter and Setter
    public Long getOrientationId() {
        return orientationId;
    }

    public void setOrientationId(Long orientationId) {
        this.orientationId = orientationId;
    }

    // 實作無參數建構函式
    public RuneOneLogRequest() {}
}