package divination.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuneOneLogRequest {
    
    @JsonProperty("orientation_id")
    private Long orientationId; 

    public Long getOrientationId() {
        return orientationId;
    }

    public void setOrientationId(Long orientationId) {
        this.orientationId = orientationId;
    }

    public RuneOneLogRequest() {}
}