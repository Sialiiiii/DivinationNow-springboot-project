package divination.spring.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users") 
public class User implements UserDetails { // 實作 UserDetails 介面

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") 
    private Long id; 

    @Column(unique = true, nullable = false)
    private String email; 

    @Column(name = "password_hash", nullable = false) 
    private String password; 

    private String username; 
    
    // 使用 @Transient 讓 Hibernate 忽略此欄位，不寫入 DB
    @Transient 
    private String role = "USER"; 

    @Column(name = "registration_date", updatable = false) 
    private LocalDateTime createdAt = LocalDateTime.now(); 
    
    // 新增欄位
    private LocalDate dateOfBirth; 
    private String gender; 
    
    @Column(name = "career_status_id") 
    private Integer careerStatusId; 

    @Column(name = "relationship_status_id") 
    private Integer relationshipStatusId;
    
    // ===============================================

    public User() {
    }

    // 建構子 (不包含 id, role, createdAt)
    public User(String email, String password, String username, 
                LocalDate dateOfBirth, String gender, 
                Integer careerStatusId, Integer relationshipStatusId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.careerStatusId = careerStatusId;
        this.relationshipStatusId = relationshipStatusId;
    }

    // --- Entity Getter 和 Setter (用於 JPA) ---
    public Long getId(){ return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // 🚀 修正點 1: 移除原有的 getPassword()，使用底下的 @Override 版本

    public void setPassword(String password) { this.password = password; }
    
    // 🚀 修正點 2: 將 getUsername() 統一為 @Override 版本
    
    public void setUsername(String username) { this.username = username; } 
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getCareerStatusId() { return careerStatusId; }
    public void setCareerStatusId(Integer careerStatusId) { this.careerStatusId = careerStatusId; }

    public Integer getRelationshipStatusId() { return relationshipStatusId; }
    public void setRelationshipStatusId(Integer relationshipStatusId) { this.relationshipStatusId = relationshipStatusId; }


    // --- UserDetails 介面實現 (與 JPA Getter/Setter 衝突的已合併) ---
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根據 role 屬性返回權限列表
        return List.of(new SimpleGrantedAuthority(role));
    }

    // 🚀 修正點 3: 實現 UserDetails 介面所需的 getPassword()
    @Override
    public String getPassword() {
        return password; // 這是 password hash，同時也是 Entity 的 Getter
    }
    
    // 🚀 修正點 4: 實現 UserDetails 介面所需的 getUsername()
    @Override
    public String getUsername() {
        return username; // 同時作為 Entity 的 Getter
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}