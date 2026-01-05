package divination.spring.project.service;

import divination.spring.project.model.Admin;
import divination.spring.project.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("adminDetailsService")
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByUsername(username);

        if (admin.isEmpty()) {
            System.out.println("登入嘗試失敗：找不到管理員帳號 -> " + username);
            throw new UsernameNotFoundException("找不到管理員帳號: " + username);
        }

        Admin adminEntity = admin.get();
        System.out.println("找到管理員：" + adminEntity.getUsername() + "，權限：" + adminEntity.getAuthorities());
        
        return adminEntity; 
    }
}