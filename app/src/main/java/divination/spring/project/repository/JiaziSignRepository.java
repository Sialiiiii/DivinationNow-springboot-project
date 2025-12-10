package divination.spring.project.repository;

import divination.spring.project.model.JiaziSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JiaziSignRepository extends JpaRepository<JiaziSign, Long> {

    /**
     * 根據籤號 (1-60) 查找籤詩
     */
    Optional<JiaziSign> findBySignNumber(Integer signNumber);
}