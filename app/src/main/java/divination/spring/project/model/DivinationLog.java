package divination.spring.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 對應資料表: divination_logs (所有占卜結果的主記錄)
 */
@Entity
@Table(name = "divination_logs")
public class DivinationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId; // PK

    @Column(name = "user_id", nullable = false)
    private Long userId; // 外鍵：指向 User

    @Column(name = "divination_type", nullable = false, length = 50)
    private String divinationType; // 占卜類型 (RUNE_SINGLE, RUNE_DOUBLE, JIAZI)

    @Column(name = "divination_time", nullable = false)
    private LocalDateTime divinationTime = LocalDateTime.now(); // 占卜時間

    @Column(name = "result_id")
    private Long resultId; // 指向 rune_single_logs, rune_double_logs, 或 jiazi_logs 的 ID

    @Column(name = "result_table", length = 50)
    private String resultTable; // 記錄結果存在哪張表 (e.g., 'rune_single_logs')
    
    // --- Getters and Setters ---
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDivinationType() { return divinationType; }
    public void setDivinationType(String divinationType) { this.divinationType = divinationType; }
    public LocalDateTime getDivinationTime() { return divinationTime; }
    public void setDivinationTime(LocalDateTime divinationTime) { this.divinationTime = divinationTime; }
    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public String getResultTable() { return resultTable; }
    public void setResultTable(String resultTable) { this.resultTable = resultTable; }
}