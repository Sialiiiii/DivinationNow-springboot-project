package divination.spring.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "fortunestick_jiazi") 
public class JiaziSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jiazi_sign_id") // 保持資料庫欄位名稱
    private Long jiaziSignId;

    @Column(name = "sign_number", nullable = false)
    private Integer signNumber; // 籤號 1-60

    @Column(name = "poetic_verse", nullable = false, columnDefinition = "TEXT")
    private String poeticVerse; // 籤詩本文

    @Column(name = "meaning_core", nullable = false, columnDefinition = "TEXT")
    private String meaningCore; // 籤詩核心解說

    @Column(name = "meaning_detail", nullable = false, columnDefinition = "TEXT")
    private String meaningDetail; // 籤詩詳細解說

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl; // 籤筒圖片 URL

    // --- Getters and Setters ---

    public Long getJiaziSignId() { return jiaziSignId; }
    public void setJiaziSignId(Long jiaziSignId) { this.jiaziSignId = jiaziSignId; }

    public Integer getSignNumber() { return signNumber; }
    public void setSignNumber(Integer signNumber) { this.signNumber = signNumber; }

    public String getPoeticVerse() { return poeticVerse; }
    public void setPoeticVerse(String poeticVerse) { this.poeticVerse = poeticVerse; }

    public String getMeaningCore() { return meaningCore; }
    public void setMeaningCore(String meaningCore) { this.meaningCore = meaningCore; }

    public String getMeaningDetail() { return meaningDetail; }
    public void setMeaningDetail(String meaningDetail) { this.meaningDetail = meaningDetail; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}