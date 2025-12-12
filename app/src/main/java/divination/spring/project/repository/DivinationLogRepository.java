package divination.spring.project.repository;

import divination.spring.project.model.DivinationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivinationLogRepository extends JpaRepository<DivinationLog, Long> {
    
    // 定義一個方法來查找某個會員的所有占卜紀錄
    List<DivinationLog> findByUserIdOrderByDivinationTimeDesc(Long userId);
}