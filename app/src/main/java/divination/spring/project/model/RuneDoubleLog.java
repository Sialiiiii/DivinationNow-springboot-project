package divination.spring.project.model;

import jakarta.persistence.*;

/**
 * 對應資料表: rune_double_logs (盧恩符文雙顆占卜結果紀錄)
 */
@Entity
@Table(name = "rune_double_logs")
public class RuneDoubleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "double_log_id")
    private Long logId; // PK (匹配資料庫欄位 double_log_id)

    // 這裡不直接存 user_id，而是由 DivinationLog 引用 (但為了簡化，Entity 中仍保留所有欄位)
    
    @Column(name = "rune1_orientation_id", nullable = false)
    private Integer rune1OrientationId; // 第一張牌 (現況) 的 orientation_id

    @Column(name = "rune2_orientation_id", nullable = false)
    private Integer rune2OrientationId; // 第二張牌 (建議) 的 orientation_id

    @Column(name = "user_career_status_id")
    private Integer userCareerStatusId; // 事業狀態 ID (nullable)

    @Column(name = "user_relationship_status_id")
    private Integer userRelationshipStatusId; // 感情狀態 ID (nullable)

    // --- Getters and Setters (省略建構子) ---
    
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public Integer getRune1OrientationId() { return rune1OrientationId; }
    public void setRune1OrientationId(Integer rune1OrientationId) { this.rune1OrientationId = rune1OrientationId; }

    public Integer getRune2OrientationId() { return rune2OrientationId; }
    public void setRune2OrientationId(Integer rune2OrientationId) { this.rune2OrientationId = rune2OrientationId; }

    public Integer getUserCareerStatusId() { return userCareerStatusId; }
    public void setUserCareerStatusId(Integer userCareerStatusId) { this.userCareerStatusId = userCareerStatusId; }

    public Integer getUserRelationshipStatusId() { return userRelationshipStatusId; }
    public void setUserRelationshipStatusId(Integer userRelationshipStatusId) { this.userRelationshipStatusId = userRelationshipStatusId; }
}