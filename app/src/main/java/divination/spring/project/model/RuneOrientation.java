package divination.spring.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rune_orientations")
public class RuneOrientation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orientation_id")
  private long orientationId;

  @Column(name = "rune_id")
  private Integer runeId;

  @Column(name = "is_reversed")
  private Integer isReversed;

  @Column(name = "full_name_en", nullable = false, length = 100)
  private String fullNameEn;

  @Column(name = "full_name_zh", nullable = false, length = 100)
  private String fullNameZh;

  @Column(name = "rune_image_url", nullable = false, length = 255)
  private String runeImageUrl;

  @Column(name = "rune_general_meaning", nullable = false, columnDefinition = "TEXT")
  private String runeGeneralMeaning;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  public long getOrientationId() { return orientationId; }
  public void setOrientationId(long orientationId) { this.orientationId = orientationId; }

  public Integer getRuneId() { return runeId; }
  public void setRuneId(Integer runeId) { this.runeId = runeId; }

  public Integer getIsReversed() { return isReversed;}
  public void setIsReversed(Integer isReversed) { this.isReversed = isReversed; }

  public String getFullNameEn() { return fullNameEn; }
  public void setFullNameEn(String fullNameEn) { this.fullNameEn = fullNameEn; }

  public String getFullNameZh() { return fullNameZh; }
  public void setFullNameZh(String fullNameZh) { this.fullNameZh = fullNameZh; }

  public String getRuneImageUrl() { return runeImageUrl; }
  public void setRuneImageUrl(String runeImageUrl) { this.runeImageUrl = runeImageUrl; }

  public String getRuneGeneralMeaning() { return runeGeneralMeaning; }
  public void setRuneGeneralMeaning(String runeGeneralMeaning) { this.runeGeneralMeaning = runeGeneralMeaning; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  
}
