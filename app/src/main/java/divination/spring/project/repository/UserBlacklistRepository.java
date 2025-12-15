// divination/spring/project/repository/UserBlacklistRepository.java

package divination.spring.project.repository;

import divination.spring.project.model.UserBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlacklistRepository extends JpaRepository<UserBlacklist, Long> {
    // 檢查用戶是否在黑名單中
    boolean existsByUserId(Long userId);
}