package divination.spring.project.dto;

import divination.spring.project.model.Status;

/**
 * 用於將 Status Entity 轉換為前端下拉選單選項的 DTO。
 */
public class StatusOptionDTO {
    
    // 狀態 ID (value 傳輸給後端用於更新)
    private Integer id; 
    
    // 狀態類型 (用於前端過濾：'Career' 或 'Relationship')
    private String type; 
    
    // 狀態的值 (用於前端顯示給使用者看的名稱：'EMPLOYED', 'SINGLE' 等)
    private String value; 

    // 構造函數：從 Status 實體創建 DTO
    public StatusOptionDTO(Status status) {
        this.id = status.getStatusId();
        this.type = status.getStatusType();
        this.value = status.getStatusValue();
    }
    
    // --- Getters (供 JSON 序列化使用) ---
    
    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    // (通常 DTO 不需 Setter，除非有特殊解構需求，這裡省略 Setter)
}
