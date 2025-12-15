// divination/spring/project/model/UserBlacklist.java

package divination.spring.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_blacklist")
public class UserBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private Long blacklistId;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "locked_by_admin_id")
    private Long lockedByAdminId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Getters and Setters ---
    public Long getBlacklistId() { return blacklistId; }
    public void setBlacklistId(Long blacklistId) { this.blacklistId = blacklistId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getLockedByAdminId() { return lockedByAdminId; }
    public void setLockedByAdminId(Long lockedByAdminId) { this.lockedByAdminId = lockedByAdminId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}