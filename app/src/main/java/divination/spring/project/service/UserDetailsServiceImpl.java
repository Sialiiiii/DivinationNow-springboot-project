package divination.spring.project.service;

import divination.spring.project.model.User;
import divination.spring.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    // 🚀 關鍵修正：直接返回我們的 User Entity
        // 因為 User 實體本身已經實作了所有 UserDetails 介面的方法，
        // 這樣可以確保 getPassword() 返回的密碼雜湊能夠正確地被 DaoAuthenticationProvider 比對。
    return user; 
  }
    
    // 🚀 移除 getAuthorities 方法，因為 User Entity 已經實作了 getAuthorities()

}