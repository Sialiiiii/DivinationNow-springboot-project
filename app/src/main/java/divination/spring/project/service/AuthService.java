package divination.spring.project.service;

import divination.spring.project.model.Status; 
import divination.spring.project.model.User;
import divination.spring.project.repository.StatusRepository; 
import divination.spring.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.crypto.password.PasswordEncoder; // 正確導入
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

@Service
public class AuthService { 

    private final UserRepository userRepository;
    private final StatusRepository statusRepository; 
    private final PasswordEncoder passwordEncoder; 
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // 完整的建構子注入
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,JwtService jwtService,
                       StatusRepository statusRepository) { 
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.statusRepository = statusRepository;
    }

    // 查找 Status ID 的輔助方法
    private Integer findStatusIdByValue(String value) {
        Status status = statusRepository.findByStatusValue(value)
            .orElseThrow(() -> new RuntimeException("無效的狀態值: " + value));
        return status.getStatusId();
    }

    /**
     * 註冊新用戶的業務邏輯：包含 Status ID 查找
     */
    @Transactional 
    public User registerUser(User user, String careerStatusValue, String relationshipStatusValue) { 
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email已存在");
        }

        // 查找 Status ID 並設置到 User Entity
        Integer careerId = findStatusIdByValue(careerStatusValue);
        Integer relationshipId = findStatusIdByValue(relationshipStatusValue);

        user.setCareerStatusId(careerId);
        user.setRelationshipStatusId(relationshipId);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    } 

    /**
     * 登入業務邏輯：驗證憑證並生成 JWT
     */
    public String loginUser(String email, String password) {
        
        // 觸發 Spring Security 的認證流程
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, 
                        password
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails);
    } 
}