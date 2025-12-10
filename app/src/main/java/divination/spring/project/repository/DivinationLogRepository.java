package divination.spring.project.repository;

import divination.spring.project.model.DivinationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivinationLogRepository extends JpaRepository<DivinationLog, Long> {
    // 總體占卜紀錄的 CRUD
}