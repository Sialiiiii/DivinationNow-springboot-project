package divination.spring.project.service;

import org.springframework.beans.factory.annotation.Autowired; // 保持 @Autowired 導入
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.model.Status;
import divination.spring.project.model.User;
import divination.spring.project.repository.StatusRepository;
import divination.spring.project.repository.UserRepository;

@Service
public class AuthService { 

  private final UserRepository userRepository;
  private final StatusRepository statusRepository; 
  private final PasswordEncoder passwordEncoder; 
  // private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder,StatusRepository statusRepository
                       /* 🚀 移除 JwtService jwtService */) { 
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    // this.authenticationManager = authenticationManager;
    this.statusRepository = statusRepository;
  }

  // 查找 Status ID 的輔助方法 (保持不變)
  private Integer findStatusIdByValue(String value) {
    Status status = statusRepository.findByStatusValue(value)
      .orElseThrow(() -> new RuntimeException("無效的狀態值: " + value));
    return status.getStatusId();
  }

  /**
  * 註冊新用戶的業務邏輯 (保持不變)
  */
  @Transactional 
  public User registerUser(User user, String careerStatusValue, String relationshipStatusValue) { 
    
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new RuntimeException("Email已存在");
    }

    Integer careerId = findStatusIdByValue(careerStatusValue);
    Integer relationshipId = findStatusIdByValue(relationshipStatusValue);

    user.setCareerStatusId(careerId);
    user.setRelationshipStatusId(relationshipId);

    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    return userRepository.save(user);
  } 

  // /**
  // * 登入業務邏輯：驗證憑證，Spring Security 會自動創建 Session 和 Cookie
  //    * @return User Entity (包含 user_id)，供前端儲存
  // */
  // public User loginUser(String email, String password) {
  //   try {
  //     // 觸發 Spring Security 的認證流程，如果成功，Session 就會被創建 (Session ID 寫入 Cookie)
  //     Authentication authentication = authenticationManager.authenticate(
  //         new UsernamePasswordAuthenticationToken(email, password)
  //     );

  //     // 認證成功，返回 User Entity (Principal)
  //     return (User) authentication.getPrincipal(); 
            
  //   } catch (AuthenticationException e) {
  //           // 登入失敗 (帳號或密碼錯誤)
  //           throw new RuntimeException("帳號或密碼錯誤");
  //       }
  // } 
}