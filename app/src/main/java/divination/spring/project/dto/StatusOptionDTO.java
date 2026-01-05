package divination.spring.project.dto;
import divination.spring.project.service.StatusMapping;
import divination.spring.project.model.Status;

/**
 * 用於將 Status Entity 轉換為前端下拉選單選項
 */
public class StatusOptionDTO {
    
    private Integer id; 
    private String type; 
    private String value; 

    public StatusOptionDTO(Status status) {
        this.id = status.getStatusId();
        this.type = status.getStatusType();
        this.value = status.getStatusValue();
        this.value = StatusMapping.getChineseName(status.getStatusValue());
    }
    
    // --- Getters，供 JSON 序列化使用 ---
    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
}
