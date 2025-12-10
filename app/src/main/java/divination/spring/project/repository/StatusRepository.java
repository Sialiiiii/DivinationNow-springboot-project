package divination.spring.project.repository;

import divination.spring.project.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    
    /**
     * 根據 Status Value 查找對應的 Status Entity
     */
    Optional<Status> findByStatusValue(String statusValue);
}