package divination.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 接收盧恩符文雙顆占卜結果的請求資料物件 (包含兩張牌的 orientation ID 及使用者狀態 ID)
 */
public class RuneDoubleLogRequest {

    @JsonProperty("rune1_specific_reading_id")
    private Integer rune1SpecificReadingId; // 第一張牌(現況) 

    @JsonProperty("rune2_specific_reading_id")
    private Integer rune2SpecificReadingId; // 第二張牌 建議) 

    @JsonProperty("user_career_status_id")
    private Integer userCareerStatusId; // 事業狀態ID (Nullable)

    @JsonProperty("user_relationship_status_id")
    private Integer userRelationshipStatusId; // 感情狀態ID (Nullable)

    
    public Integer getRune1SpecificReadingId() {
        return rune1SpecificReadingId;
    }

    public void setRune1SpecificReadingId(Integer rune1SpecificReadingId) {
        this.rune1SpecificReadingId = rune1SpecificReadingId;
    }

    public Integer getRune2SpecificReadingId() {
        return rune2SpecificReadingId;
    }

    public void setRune2SpecificReadingId(Integer rune2SpecificReadingId) {
        this.rune2SpecificReadingId = rune2SpecificReadingId;
    }

    public Integer getUserCareerStatusId() {
        return userCareerStatusId;
    }

    public void setUserCareerStatusId(Integer userCareerStatusId) {
        this.userCareerStatusId = userCareerStatusId;
    }

    public Integer getUserRelationshipStatusId() {
        return userRelationshipStatusId;
    }

    public void setUserRelationshipStatusId(Integer userRelationshipStatusId) {
        this.userRelationshipStatusId = userRelationshipStatusId;
    }

    public RuneDoubleLogRequest() {}
}