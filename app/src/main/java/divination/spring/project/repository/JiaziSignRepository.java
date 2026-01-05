package divination.spring.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.JiaziSign;

@Repository
public interface JiaziSignRepository extends JpaRepository<JiaziSign, Long> {
    Optional<JiaziSign> findBySignNumber(Integer signNumber);
}