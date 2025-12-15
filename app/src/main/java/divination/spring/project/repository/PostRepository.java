// divination/spring/project/repository/PostRepository.java

package divination.spring.project.repository;

import divination.spring.project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 處理 mood_posts 資料表的 JPA Repository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 繼承 JpaRepository 後，它會自動提供 CRUD 操作，例如 save(), findById(), findAll()
}