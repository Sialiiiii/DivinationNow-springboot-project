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

    public AdminService(PostRepository postRepository, 
                        UserBlacklistRepository blacklistRepository,
                        PostLikeRepository postLikeRepository,
                        UserRepository userRepository) {
        this.postRepository = postRepository;
        this.blacklistRepository = blacklistRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
    }
    
    // ==================== 貼文管理 ====================

    /**
     * 獲取所有貼文供管理員審查
     */
    public List<AdminPostDTO> getAllPostsForAdmin() {
        List<Post> posts = postRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));

        Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());
        
        Map<Long, String> usernames = userRepository.findAllById(userIds)
                .stream().collect(Collectors.toMap(User::getId, User::getUsernameJPA)); 

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
     * 刪除貼文
     */
    @Transactional
    public boolean deletePostByAdmin(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postLikeRepository.deleteAll(postLikeRepository.findByPostId(postId));
            postRepository.delete(post.get());
            return true;
        }
        return false;
    }
    
    // ==================== 黑名單管理 ====================

    /**
     * 將用戶加入黑名單
     */
    @Transactional
    public boolean blacklistUser(Long userId, Integer adminId, String reason) {
        if (blacklistRepository.existsByUserId(userId)) {
            return false;
        }
        
        UserBlacklist blacklist = new UserBlacklist();
        blacklist.setUserId(userId);
        blacklist.setReason(reason != null ? reason : "違反版規");
        blacklist.setLockedByAdminId(adminId.longValue()); 
        
        blacklistRepository.save(blacklist);
        return true;
    }

    /**
     * 從黑名單中移除用戶
     */
    @Transactional
    public boolean unblacklistUser(Long userId) {
        Optional<UserBlacklist> record = blacklistRepository.findByUserId(userId); 
        if (record.isPresent()) {
            blacklistRepository.delete(record.get());
            return true;
        }
        return false;
    }

    /**
     * 獲取特定用戶的黑名單詳細資訊 (💡 前端查看原因用)
     */
    public Optional<UserBlacklist> getBlacklistDetail(Long userId) {
        return blacklistRepository.findByUserId(userId);
    }

    /**
     * 獲取所有會員列表 (已包含黑名單狀態標記)
     */
    public List<User> findAllUsers() {
        List<User> allUsers = userRepository.findAll();
        
        Set<Long> blacklistedUserIds = blacklistRepository.findAll()
                .stream()
                .map(UserBlacklist::getUserId)
                .collect(Collectors.toSet());

        allUsers.forEach(user -> {
            user.setBlacklisted(blacklistedUserIds.contains(user.getId()));
        });

        return allUsers;
    }
}