package divination.spring.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId; // 💡 status_id 是 Integer

    @Column(name = "status_type", nullable = false)
    private String statusType; // Career 或 Relationship

    @Column(name = "status_value", nullable = false, unique = true)
    private String statusValue; // EMPLOYED, SINGLE 等

    // --- Getter 和 Setter (省略，請自行生成) ---
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    
    public String getStatusType() { return statusType; }
    public void setStatusType(String statusType) { this.statusType = statusType; }

    public String getStatusValue() { return statusValue; }
    public void setStatusValue(String statusValue) { this.statusValue = statusValue; }

    public Status() {}
}