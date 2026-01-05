package divination.spring.project.repository;

import divination.spring.project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 處理 mood_posts 資料表
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}