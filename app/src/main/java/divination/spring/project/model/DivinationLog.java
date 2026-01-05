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
    private Long userId;

    @Column(name = "divination_type", nullable = false)
    private String divinationType;

    @Column(name = "divination_time", nullable = false)
    private LocalDateTime divinationTime = LocalDateTime.now();

    @Column(name = "result_id", nullable = false)
    private Integer resultId; 

    @Column(name = "result_table", nullable = false)
    private String resultTable;
    
    @Column(name = "question", columnDefinition = "VARCHAR(255)") 
    private String question;

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
    
    public String getQuestion() {
        return question; 
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
}