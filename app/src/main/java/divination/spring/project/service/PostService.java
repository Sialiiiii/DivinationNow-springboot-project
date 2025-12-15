// divination/spring/project/service/PostService.java

package divination.spring.project.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.dto.PostDTO;
import divination.spring.project.model.Post;
import divination.spring.project.model.PostLike;
import divination.spring.project.model.User;
import divination.spring.project.repository.PostLikeRepository;
import divination.spring.project.repository.PostRepository;
import divination.spring.project.repository.UserBlacklistRepository;
import divination.spring.project.repository.UserRepository; // ⭐ 需要引入 UserRepository

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserBlacklistRepository blacklistRepository;
    private final UserRepository userRepository; // ⭐ 注入 UserRepository

    public PostService(PostRepository postRepository, 
                       PostLikeRepository postLikeRepository,
                       UserBlacklistRepository blacklistRepository,
                       UserRepository userRepository) { // ⭐ 構造函數注入
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
    }

    /**
     * 用戶發布新貼文
     */
    @Transactional
    public Post createPost(Long userId, String content) {
        if (blacklistRepository.existsByUserId(userId)) {
            throw new IllegalStateException("用戶已被鎖定，無法發布貼文。");
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        return postRepository.save(post);
    }
    
    /**
     * 處理用戶點讚/取消點讚/更換表情符號
     * reactionType 預期值: "LOVE", "EMOTION", "FUNNY"
     * 返回當前貼文的最新計數 (Map)
     */
    @Transactional
    public Map<String, Integer> handleReaction(Long postId, Long userId, String reactionType) {
        
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("找不到貼文ID: " + postId);
        }
        Post post = optionalPost.get();
        
        Optional<PostLike> optionalExistingLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        
        if (optionalExistingLike.isPresent()) {
            PostLike existingLike = optionalExistingLike.get();
            String existingType = existingLike.getReactionType();
            
            if (existingType.equals(reactionType)) {
                // 情況 1: 點擊相同的表情符號 -> 取消讚
                postLikeRepository.delete(existingLike);
                updatePostCount(post, existingType, -1);
            } else {
                // 情況 2: 點擊不同的表情符號 -> 更換讚
                updatePostCount(post, existingType, -1); // 減少舊的
                updatePostCount(post, reactionType, 1);  // 增加新的
                
                existingLike.setReactionType(reactionType);
                postLikeRepository.save(existingLike);
            }
        } else {
            // 情況 3: 首次點讚
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setUserId(userId);
            newLike.setReactionType(reactionType);
            postLikeRepository.save(newLike);
            
            updatePostCount(post, reactionType, 1); // 增加新的計數
        }
        
        // 返回更新後的計數
        return Map.of(
            "loveCount", post.getLoveCount(),
            "emotionCount", post.getEmotionCount(),
            "funnyCount", post.getFunnyCount()
        );
    }

    // 私有輔助方法：根據類型更新貼文的計數
    private void updatePostCount(Post post, String reactionType, int change) {
        switch (reactionType.toUpperCase()) {
            case "LOVE":
                post.setLoveCount(post.getLoveCount() + change);
                break;
            case "EMOTION":
                post.setEmotionCount(post.getEmotionCount() + change);
                break;
            case "FUNNY":
                post.setFunnyCount(post.getFunnyCount() + change);
                break;
            default:
                // 實際上應該在 Controller 驗證，但這裡也做防禦性編程
                throw new IllegalArgumentException("無效的表情符號類型: " + reactionType);
        }
        postRepository.save(post);
    }
    
    /**
     * 獲取所有貼文，並組裝為 PostDTO 供前端使用
     * @param currentUserId 當前登入的用戶 ID (若未登入則為 null)
     */
    public List<PostDTO> getPostDTOsForUser(Long currentUserId) {
        // 獲取所有貼文，按時間倒序排序
        List<Post> posts = postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        
        // 1. 預先獲取所有貼文作者的用戶名
        // (此處邏輯假設您 UserRepository 有 findAllById 方法，且 User Entity 具有 getId() 和 getUsername() 方法)
        Map<Long, String> usernames = userRepository.findAllById(
            posts.stream().map(Post::getUserId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getId, User::getUsernameJPA));
        
        // 2. 預先獲取當前用戶的所有點讚記錄
        // ⭐ 修正點 1: 使用條件賦值，確保 userReactions 在後續使用時是 '有效 final'。
        final Map<Long, String> userReactions; 
        if (currentUserId != null) {
             userReactions = postLikeRepository.findByUserId(currentUserId).stream()
                .collect(Collectors.toMap(PostLike::getPostId, PostLike::getReactionType));
        } else {
             // 如果未登入，賦予一個空的 Map
             userReactions = Map.of();
        }

        // 3. 轉換為 DTO
        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setUserId(post.getUserId());
            // dto.setUsername(usernames.getOrDefault(post.getUserId(), "未知用戶"));
            dto.setUsername(usernames.getOrDefault(post.getUserId(), "未知用戶"));
            dto.setContent(post.getContent());
            dto.setLoveCount(post.getLoveCount());
            dto.setEmotionCount(post.getEmotionCount());
            dto.setFunnyCount(post.getFunnyCount());
            dto.setCreatedAt(post.getCreatedAt());
            
            // ⭐ 修正點 2: 直接使用 userReactions
            dto.setUserReactionType(userReactions.get(post.getPostId()));
            
            // 設置是否為自己發布
            dto.setPostOwnedByCurrentUser(currentUserId != null && currentUserId.equals(post.getUserId()));
            
            return dto;
        }).collect(Collectors.toList());
    }
}