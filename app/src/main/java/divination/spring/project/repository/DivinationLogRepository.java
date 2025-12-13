package divination.spring.project.repository;

import divination.spring.project.model.DivinationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // ⭐ 必須導入 Optional 類別

@Repository
public interface DivinationLogRepository extends JpaRepository<DivinationLog, Long> {
    
    // 定義一個方法來查找某個會員的所有占卜紀錄 (已存在)
    List<DivinationLog> findByUserIdOrderByDivinationTimeDesc(Long userId);
    
    // ⭐ 新增：用於更新問題時，驗證紀錄是否屬於該使用者
    Optional<DivinationLog> findByLogIdAndUserId(Long logId, Long userId);
}