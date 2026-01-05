package divination.spring.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.SpecificRuneReading;

@Repository
public interface SpecificRuneReadingRepository extends JpaRepository<SpecificRuneReading, Integer> {

    /**
     * 根據 orientation_id, status_id, position 查找 specific_reading
     * @param orientationId 符文正逆位ID (PK)
     * @param statusId 事業/感情狀態ID
     * @param position 牌位
     * @return 符文解讀 Optional 物件
     */
    Optional<SpecificRuneReading> findByOrientationIdAndStatusIdAndIsCurrentStatusPosition(
            Integer orientationId, Integer statusId, Integer position);
}