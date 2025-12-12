package divination.spring.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.RuneOrientation;

@Repository
public interface RuneSingleLogRepository extends JpaRepository<RuneOrientation, Long> {
}