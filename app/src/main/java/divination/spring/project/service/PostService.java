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
import divination.spring.project.repository.UserRepository; 

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserBlacklistRepository blacklistRepository;
    private final UserRepository userRepository;
    public PostService(PostRepository postRepository, 
                       PostLikeRepository postLikeRepository,
                       UserBlacklistRepository blacklistRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
    }

    /**
     * 發布新貼文
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
     * 用戶點讚/取消點讚/更換表情符號
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
                // 點擊相同的表情符號 -> 取消讚
                postLikeRepository.delete(existingLike);
                updatePostCount(post, existingType, -1);
            } else {
                // 點擊不同的表情符號 -> 更換讚
                updatePostCount(post, existingType, -1);
                updatePostCount(post, reactionType, 1);
                
                existingLike.setReactionType(reactionType);
                postLikeRepository.save(existingLike);
            }
        } else {
            // 首次點
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setUserId(userId);
            newLike.setReactionType(reactionType);
            postLikeRepository.save(newLike);
            
            updatePostCount(post, reactionType, 1); 
        }
        
        return Map.of(
            "loveCount", post.getLoveCount(),
            "emotionCount", post.getEmotionCount(),
            "funnyCount", post.getFunnyCount()
        );
    }

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
                throw new IllegalArgumentException("無效的表情符號類型: " + reactionType);
        }
        postRepository.save(post);
    }
    
    /**
     * 獲取所有貼文，並組裝為 PostDTO 供前端使用
     * @param currentUserId 當前登入的用戶 ID (若未登入則為 null)
     */
    public List<PostDTO> getPostDTOsForUser(Long currentUserId) {
        List<Post> posts = postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        
        // 預先獲取所有貼文作者的用戶名
        Map<Long, String> usernames = userRepository.findAllById(
            posts.stream().map(Post::getUserId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getId, User::getUsernameJPA));
        
        // 預先獲取當前用戶的所有點讚記錄
        final Map<Long, String> userReactions; 
        if (currentUserId != null) {
             userReactions = postLikeRepository.findByUserId(currentUserId).stream()
                .collect(Collectors.toMap(PostLike::getPostId, PostLike::getReactionType));
        } else {
             userReactions = Map.of();
        }

        // 轉換為 DTO
        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setUserId(post.getUserId());
            dto.setUsername(usernames.getOrDefault(post.getUserId(), "未知用戶"));
            dto.setContent(post.getContent());
            dto.setLoveCount(post.getLoveCount());
            dto.setEmotionCount(post.getEmotionCount());
            dto.setFunnyCount(post.getFunnyCount());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUserReactionType(userReactions.get(post.getPostId()));
            dto.setPostOwnedByCurrentUser(currentUserId != null && currentUserId.equals(post.getUserId()));
            
            return dto;
        }).collect(Collectors.toList());
    }
}