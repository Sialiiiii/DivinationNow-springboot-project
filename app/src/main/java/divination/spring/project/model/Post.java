package divination.spring.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mood_posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "love_count", nullable = false)
    private Integer loveCount = 0;

    @Column(name = "emotion_count", nullable = false)
    private Integer emotionCount = 0;

    @Column(name = "funny_count", nullable = false)
    private Integer funnyCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    
    // --- Getters and Setters ---
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getLoveCount() { return loveCount; }
    public void setLoveCount(Integer loveCount) { this.loveCount = loveCount; }
    public Integer getEmotionCount() { return emotionCount; }
    public void setEmotionCount(Integer emotionCount) { this.emotionCount = emotionCount; }
    public Integer getFunnyCount() { return funnyCount; }
    public void setFunnyCount(Integer funnyCount) { this.funnyCount = funnyCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}