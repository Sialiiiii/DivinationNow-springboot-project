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
    private String divinationType;

    @Column(name = "divination_time", nullable = false)
    private LocalDateTime divinationTime = LocalDateTime.now();

    // 多型關聯: 儲存結果 ID
    @Column(name = "result_id", nullable = false)
    private Integer resultId; 

    // 多型關聯: 儲存結果表名 (例如: "rune_orientations")
    @Column(name = "result_table", nullable = false)
    private String resultTable;
    
    @Column(name = "question", columnDefinition = "VARCHAR(255)") 
    private String question; // 欄位已存在

    // --- Getters and Setters ---
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
    public void setDivinationTime(LocalDateTime divinationTime) { 
        this.divinationTime = divinationTime;
    }
    
    // ⭐ 修正點 1: 移除拋出異常的代碼，直接返回欄位值
    public String getQuestion() {
        return question; 
    }
    
    // ⭐ 修正點 2: 新增 setQuestion(String) 方法 (解決錯誤 #2)
    public void setQuestion(String question) {
        this.question = question;
    }
}