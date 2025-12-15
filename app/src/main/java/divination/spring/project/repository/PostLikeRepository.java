// divination/spring/project/repository/PostLikeRepository.java

package divination.spring.project.repository;

import divination.spring.project.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    // 根據貼文和用戶ID查找記錄
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId); 
    
    // 根據貼文 ID 查找所有讚 (用於刪除貼文時清理數據)
    List<PostLike> findByPostId(Long postId); 
    
    // 根據用戶 ID 查找用戶按過讚的所有記錄 (用於優化查詢)
    List<PostLike> findByUserId(Long userId);
}