package divination.spring.project.repository;

import divination.spring.project.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // 給 Spring Security 查找管理員
    Optional<Admin> findByUsername(String username);
}