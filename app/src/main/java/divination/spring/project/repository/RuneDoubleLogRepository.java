package divination.spring.project.repository;

import divination.spring.project.model.RuneDoubleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 處理 rune_double_logs 資料表
 */
@Repository
public interface RuneDoubleLogRepository extends JpaRepository<RuneDoubleLog, Long> {
}