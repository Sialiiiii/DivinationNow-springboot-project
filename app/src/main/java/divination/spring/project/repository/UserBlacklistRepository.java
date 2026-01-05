package divination.spring.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.UserBlacklist;

@Repository
public interface UserBlacklistRepository extends JpaRepository<UserBlacklist, Long> {
    boolean existsByUserId(Long userId);

    Optional<UserBlacklist> findByUserId(Long userId);
}