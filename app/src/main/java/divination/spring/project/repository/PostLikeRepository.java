package divination.spring.project.repository;

import divination.spring.project.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId); 
    List<PostLike> findByPostId(Long postId); 
    List<PostLike> findByUserId(Long userId);
}