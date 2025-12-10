package divination.spring.project.repository;

import divination.spring.project.model.SpecificRuneReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecificRuneReadingRepository extends JpaRepository<SpecificRuneReading, Long> {

    /**
     * 🚀 修正點：修正方法簽名以匹配 Entity 中修正後的屬性名稱 (orientationId)
     * 避免 Hibernate 嘗試查詢 rune_orientation_id 欄位。
     * @param orientationId 符文正逆位 ID (PK)
     * @param statusId 事業/感情狀態 ID
     * @param position 牌位 (1=現況, 0=建議)
     * @return 符文解讀 Optional 物件
     */
    Optional<SpecificRuneReading> findByOrientationIdAndUserStatusIdAndIsCurrentStatusPosition(
            Integer orientationId, Integer statusId, Integer position);
}