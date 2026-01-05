package divination.spring.project.model;

import jakarta.persistence.*;

/**
 * 對應資料表: specific_rune_readings
 */
@Entity
@Table(name = "specific_rune_readings")
public class SpecificRuneReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specific_reading_id")
    private Long specificReadingId;

    @Column(name = "orientation_id", nullable = false)
    private Integer orientationId; 


    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "is_current_status_position", nullable = false)
    private Integer isCurrentStatusPosition;

    @Column(name = "interpretation_text", nullable = false, columnDefinition = "TEXT")
    private String interpretationText;


    // --- Getters and Setters ---
    public Long getSpecificReadingId() { return specificReadingId; }
    public void setSpecificReadingId(Long specificReadingId) { this.specificReadingId = specificReadingId; }

    public Integer getOrientationId() { return orientationId; }
    public void setOrientationId(Integer orientationId) { this.orientationId = orientationId; }

    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    public Integer getIsCurrentStatusPosition() { return isCurrentStatusPosition; }
    public void setIsCurrentStatusPosition(Integer isCurrentStatusPosition) { this.isCurrentStatusPosition = isCurrentStatusPosition; }

    public String getInterpretationText() { return interpretationText; }
    public void setInterpretationText(String interpretationText) { this.interpretationText = interpretationText; }

}