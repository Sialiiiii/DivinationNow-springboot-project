package divination.spring.project.repository;

import divination.spring.project.model.RuneSingleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuneSingleLogRepository extends JpaRepository<RuneSingleLog, Long> {
    // 占卜紀錄的 CRUD
}