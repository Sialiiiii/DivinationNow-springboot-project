package divination.spring.project.repository;

import divination.spring.project.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;


@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    
    /**
     * 根據 Status Value 查找對應的 Status Entity
     */
    Optional<Status> findByStatusValue(String statusValue);


    /**
     * 根據 Status Type 查找所有 Status Entity 
     */
    List<Status> findByStatusType(String statusType);
}