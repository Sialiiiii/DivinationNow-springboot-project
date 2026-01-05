package divination.spring.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_type", nullable = false)
    private String statusType;

    @Column(name = "status_value", nullable = false, unique = true)
    private String statusValue;

    
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    
    public String getStatusType() { return statusType; }
    public void setStatusType(String statusType) { this.statusType = statusType; }

    public String getStatusValue() { return statusValue; }
    public void setStatusValue(String statusValue) { this.statusValue = statusValue; }

    public Status() {}
}