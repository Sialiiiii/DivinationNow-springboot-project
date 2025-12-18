package divination.spring.project.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.dto.AdminPostDTO;
import divination.spring.project.model.Post;
import divination.spring.project.model.User;
import divination.spring.project.model.UserBlacklist;
import divination.spring.project.repository.PostLikeRepository;
import divination.spring.project.repository.PostRepository;
import divination.spring.project.repository.UserBlacklistRepository;
import divination.spring.project.repository.UserRepository;

@Service
public class AdminService {

    private final PostRepository postRepository;
    private final UserBlacklistRepository blacklistRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    // 假設您已有 PostRepository, PostLikeRepository, UserRepository
    public AdminService(PostRepository postRepository, 
                        UserBlacklistRepository blacklistRepository,
                        PostLikeRepository postLikeRepository,
                        UserRepository userRepository) {
        this.postRepository = postRepository;
        this.blacklistRepository = blacklistRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
    }
    
    // --- 貼文管理 ---

    /**
     * 獲取所有貼文供管理員審查
     */
    public List<AdminPostDTO> getAllPostsForAdmin() {
        // 獲取所有貼文，按時間倒序排序
        List<Post> posts = postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));

        // 獲取所有相關用戶ID
        Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());
        
        // 預先獲取用戶名 (假設您的 User Entity 有 getUsernameJPA() 方法來獲取暱稱)
        Map<Long, String> usernames = userRepository.findAllById(userIds)
                .stream().collect(Collectors.toMap(User::getId, User::getUsernameJPA)); 

        // 預先獲取黑名單用戶ID列表
        Set<Long> blacklistedUserIds = blacklistRepository.findAll().stream()
                .map(UserBlacklist::getUserId).collect(Collectors.toSet());

        return posts.stream().map(post -> {
            AdminPostDTO dto = new AdminPostDTO();
            dto.setPostId(post.getPostId());
            dto.setUserId(post.getUserId());
            dto.setContent(post.getContent());
            dto.setLoveCount(post.getLoveCount());
            dto.setEmotionCount(post.getEmotionCount());
            dto.setFunnyCount(post.getFunnyCount());
            dto.setCreatedAt(post.getCreatedAt());

            dto.setUsername(usernames.getOrDefault(post.getUserId(), "未知用戶"));
            dto.setBlacklisted(blacklistedUserIds.contains(post.getUserId()));

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 刪除貼文（供管理員使用）
     */
    @Transactional
    public boolean deletePostByAdmin(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            // 刪除所有相關的讚記錄 (假設 PostLikeRepository 有 findByPostId 方法)
            postLikeRepository.deleteAll(postLikeRepository.findByPostId(postId));
            postRepository.delete(post.get());
            return true;
        }
        return false;
    }
    
    // --- 黑名單管理 ---

    /**
     * 將用戶加入黑名單
     */
    @Transactional
    public boolean blacklistUser(Long userId, Integer adminId, String reason) {
        if (blacklistRepository.existsByUserId(userId)) {
            return false; // 已經在黑名單中
        }
        
        UserBlacklist blacklist = new UserBlacklist();
        blacklist.setUserId(userId);
        blacklist.setReason(reason != null ? reason : "違反版規");
        // ⭐ 將 Admin ID (Integer) 設置到 UserBlacklist 的 Long 欄位
        blacklist.setLockedByAdminId(adminId.longValue()); 
        
        blacklistRepository.save(blacklist);
        return true;
    }

    /**
     * 從黑名單中移除用戶
     */
    @Transactional
    public boolean unblacklistUser(Long userId) {
        // ⭐ 假設 UserBlacklistRepository 已經新增了 findByUserId(Long userId) 方法
        Optional<UserBlacklist> record = blacklistRepository.findByUserId(userId); 
        if (record.isPresent()) {
            blacklistRepository.delete(record.get());
            return true;
        }
        return false;
    }
}