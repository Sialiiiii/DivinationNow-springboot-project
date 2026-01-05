package divination.spring.project.repository;

import divination.spring.project.model.DivinationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DivinationLogRepository extends JpaRepository<DivinationLog, Long> {
    
    // 查找會員的所有占卜紀錄
    List<DivinationLog> findByUserIdOrderByDivinationTimeDesc(Long userId);
    
    // 用於更新問題，驗證紀錄是否屬於該使用者
    Optional<DivinationLog> findByLogIdAndUserId(Long logId, Long userId);
}