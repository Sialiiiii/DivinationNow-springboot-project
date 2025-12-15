// divination/spring/project/dto/PostDTO.java

package divination.spring.project.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private Long postId;
    private Long userId;
    private String username; // 發文者名稱
    private String content;
    private Integer loveCount;
    private Integer emotionCount;
    private Integer funnyCount;
    private LocalDateTime createdAt;
    private String userReactionType; // 當前登入用戶對此貼文按的表情符號 (e.g., "LOVE", "FUNNY")
    private boolean isPostOwnedByCurrentUser; // 是否為當前用戶發布
    
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
    public String getUserReactionType() { return userReactionType; }
    public void setUserReactionType(String userReactionType) { this.userReactionType = userReactionType; }
    public boolean isPostOwnedByCurrentUser() { return isPostOwnedByCurrentUser; }
    public void setPostOwnedByCurrentUser(boolean postOwnedByCurrentUser) { this.isPostOwnedByCurrentUser = postOwnedByCurrentUser; }
}
