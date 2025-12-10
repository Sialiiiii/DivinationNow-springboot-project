package divination.spring.project.repository;

import divination.spring.project.model.RuneOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 處理 rune_orientation 資料表的 CRUD 操作
 * 這個 Repository 介面由 RuneOneService 和未來可能的 RuneTwoService 共享。
 */
@Repository
public interface RuneOrientationRepository extends JpaRepository<RuneOrientation, Long> {

    /**
     * 獲取所有符文的正位和逆位狀態。
     * JpaRepository 的 findAll() 已經足夠。
     */
    List<RuneOrientation> findAll();
}