package divination.spring.project.model;

import jakarta.persistence.*;

/**
 * 對應資料表: rune_double_logs (盧恩符文雙顆占卜結果紀錄 仲介表)
 */
@Entity
@Table(name = "rune_double_logs")
public class RuneDoubleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "double_log_id")
    private Long logId; // PK (匹配資料表divination_logs的欄位double_log_id)

    @Column(name = "rune1_specific_reading_id", nullable = false)
    private Integer rune1SpecificReadingId; 

    @Column(name = "rune2_specific_reading_id", nullable = false)
    private Integer rune2SpecificReadingId;

    @Column(name = "user_career_status_id")
    private Integer userCareerStatusId; // 事業狀態 ID (nullable)

    @Column(name = "user_relationship_status_id")
    private Integer userRelationshipStatusId; // 感情狀態 ID (nullable)

    // --- Getters and Setters (省略建構子) ---
    
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public Integer getRune1SpecificReadingId() { return rune1SpecificReadingId; }
    public void setRune1SpecificReadingId(Integer rune1SpecificReadingId) { this.rune1SpecificReadingId = rune1SpecificReadingId; }
    
    public Integer getRune2SpecificReadingId() { return rune2SpecificReadingId; }
    public void setRune2SpecificReadingId(Integer rune2SpecificReadingId) { this.rune2SpecificReadingId = rune2SpecificReadingId; }
    
    public Integer getUserCareerStatusId() { return userCareerStatusId; }
    public void setUserCareerStatusId(Integer userCareerStatusId) { this.userCareerStatusId = userCareerStatusId; }

    public Integer getUserRelationshipStatusId() { return userRelationshipStatusId; }
    public void setUserRelationshipStatusId(Integer userRelationshipStatusId) { this.userRelationshipStatusId = userRelationshipStatusId; }
}