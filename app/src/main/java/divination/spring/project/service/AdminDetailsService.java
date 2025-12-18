package divination.spring.project.service;

import divination.spring.project.model.Admin; // 💡 確保這行路徑與你的 Admin.java 一致
import divination.spring.project.repository.AdminRepository; // 💡 確保 Repository 路徑正確
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("adminDetailsService") // 💡 指定名稱，對齊 SecurityConfig 的 @Qualifier
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 從資料庫查詢管理員，注意你的欄位是 admin_username
        Optional<Admin> admin = adminRepository.findByUsername(username);

        if (admin.isEmpty()) {
            System.out.println("🚨 登入嘗試失敗：找不到管理員帳號 -> " + username);
            throw new UsernameNotFoundException("找不到管理員帳號: " + username);
        }

        Admin adminEntity = admin.get();
        System.out.println("✅ 找到管理員：" + adminEntity.getUsername() + "，權限：" + adminEntity.getAuthorities());
        
        return adminEntity; // 💡 Admin 已經實現了 UserDetails，直接回傳即可
    }
}