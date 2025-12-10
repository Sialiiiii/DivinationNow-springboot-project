package divination.spring.project.service;

import divination.spring.project.model.User;
import divination.spring.project.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security 用這個方法來加載用戶資訊，進行登入驗證
     * @param username 這裡對應的是我們的 Email 欄位
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根據 Email 從資料庫尋找使用者
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者: " + username));

        // 2. 將我們的 User Entity 轉換成 Spring Security 的 UserDetails 介面實作
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),          // 用 Email 作為使用者名稱
                user.getPassword(),       // 資料庫中加密後的密碼
                getAuthorities(user.getRole()) // 獲取用戶權限
        );
    }

    /**
     * 獲取用戶的角色/權限
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)); // 例如: ROLE_USER
    }
}