package divination.spring.project.dto;

import java.time.LocalDateTime;

public class AdminPostDTO {
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private Integer loveCount;
    private Integer emotionCount;
    private Integer funnyCount;
    private LocalDateTime createdAt;
    private boolean isBlacklisted;

    // --- Getters and Setters ---
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
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
    public boolean isBlacklisted() { return isBlacklisted; }
    public void setBlacklisted(boolean blacklisted) { isBlacklisted = blacklisted; }
}