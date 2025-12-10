package divination.spring.project.model;

import jakarta.persistence.*;

/**
 * 對應資料表: rune_single_logs
 */
@Entity
@Table(name = "rune_single_logs")
public class RuneSingleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "single_log_id") // 匹配資料表欄位 single_log_id
    private Long singleLogId; 

    @Column(name = "rune_orientation_id", nullable = false) // 匹配資料表欄位 rune_orientation_id
    private Integer runeOrientationId;

    @Column(name = "user_career_status_id") // 匹配資料表欄位 user_career_status_id
    private Integer userCareerStatusId; 

    @Column(name = "user_relationship_status_id") // 匹配資料表欄位 user_relationship_status_id
    private Integer userRelationshipStatusId; 

    // --- Getters and Setters (省略建構子) ---
    
    public Long getSingleLogId() { return singleLogId; }
    public void setSingleLogId(Long singleLogId) { this.singleLogId = singleLogId; }

    public Integer getRuneOrientationId() { return runeOrientationId; }
    public void setRuneOrientationId(Integer runeOrientationId) { this.runeOrientationId = runeOrientationId; }

    public Integer getUserCareerStatusId() { return userCareerStatusId; }
    public void setUserCareerStatusId(Integer userCareerStatusId) { this.userCareerStatusId = userCareerStatusId; }

    public Integer getUserRelationshipStatusId() { return userRelationshipStatusId; }
    public void setUserRelationshipStatusId(Integer userRelationshipStatusId) { this.userRelationshipStatusId = userRelationshipStatusId; }
}