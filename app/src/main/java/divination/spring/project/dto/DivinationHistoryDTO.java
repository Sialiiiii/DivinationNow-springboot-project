package divination.spring.project.dto;

import divination.spring.project.model.DivinationLog;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DivinationHistoryDTO {
    private Long id; 
    private String method; 
    private String result; 
    private String interpretation; // 新增屬性
    private String time;
    private String question; 
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public DivinationHistoryDTO(DivinationLog log) {
        this.id = log.getLogId();
        this.method = log.getDivinationType();
        this.time = log.getDivinationTime() != null ? log.getDivinationTime().format(TIME_FORMATTER) : "N/A";
        this.question = log.getQuestion();
        // result 和 interpretation 將在 Service 層設置，此處省略初始化
    }
    
    // --- Getters ---
    public Long getId() { return id; }
    public String getMethod() { return method; }
    public String getResult() { return result; }
    public String getInterpretation() { return interpretation; }
    public String getTime() { return time; }
    public String getQuestion() { return question; }
    
    // ⭐ 修正點：新增所有缺失的 Setter 
    public void setId(Long id) { this.id = id; }
    public void setMethod(String method) { this.method = method; }
    public void setResult(String result) { this.result = result; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
    public void setTime(String time) { this.time = time; }
    public void setQuestion(String question) { this.question = question; }
}