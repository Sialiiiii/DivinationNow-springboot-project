package divination.spring.project.repository;

import divination.spring.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // 用於處理可能找不到結果的情況

// 繼承 JpaRepository<Entity, IdType>
// Spring Data JPA 會自動為我們實作 CRUD 相關的方法 (如 save, findAll, findById 等)
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Spring Data JPA 的查詢方法命名規則：
     * 透過方法名稱，Spring 會自動生成 SQL 查詢。
     * 我們需要根據 Email 查詢使用者來進行登入或註冊檢查。
     */
    Optional<User> findByEmail(String email);

    // 如果未來你希望用 username 檢查唯一性，可以新增：
    // Optional<User> findByUsername(String username);
}