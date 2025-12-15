// divination/spring/project/model/PostLike.java

package divination.spring.project.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes")
@IdClass(PostLike.PostLikeId.class)
public class PostLike implements Serializable {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reaction_type", nullable = false, length = 10)
    private String reactionType; // "LOVE", "EMOTION", "FUNNY"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 複合主鍵類別
    public static class PostLikeId implements Serializable {
        private Long postId;
        private Long userId;

        // 請確保提供 hashCode, equals 和 Getters/Setters
        public PostLikeId() {}
        public PostLikeId(Long postId, Long userId) { this.postId = postId; this.userId = userId; }
        public Long getPostId() { return postId; }
        public void setPostId(Long postId) { this.postId = postId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        // 實現 hashCode 和 equals 略...
    }

    // --- Getters and Setters ---
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getReactionType() { return reactionType; }
    public void setReactionType(String reactionType) { this.reactionType = reactionType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}