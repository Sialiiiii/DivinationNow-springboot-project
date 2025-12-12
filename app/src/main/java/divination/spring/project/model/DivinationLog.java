package divination.spring.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "divination_logs")
public class DivinationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 外鍵，引用 users 表

    @Column(name = "divination_type", nullable = false)
    private String divinationType; // 例如: "Rune Single"

    @Column(name = "divination_time", nullable = false)
    private LocalDateTime divinationTime = LocalDateTime.now();

    // 多型關聯: 儲存結果 ID
    @Column(name = "result_id", nullable = false)
    private Integer resultId; 

    // 多型關聯: 儲存結果表名 (例如: "rune_orientations")
    @Column(name = "result_table", nullable = false)
    private String resultTable;
    
    // 省略 Getter/Setter/Constructors 保持簡潔...

    // 完整的 Getter/Setter 需根據您的需求添加
    public Long getLogId() { return logId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setDivinationType(String divinationType) { this.divinationType = divinationType; }
    public void setResultId(Integer resultId) { this.resultId = resultId; }
    public void setResultTable(String resultTable) { this.resultTable = resultTable; }
    public Long getUserId() { return userId; }
    public String getDivinationType() { return divinationType; }
    public Integer getResultId() { return resultId; }
    public String getResultTable() { return resultTable; }
    public LocalDateTime getDivinationTime() { return divinationTime; }
}