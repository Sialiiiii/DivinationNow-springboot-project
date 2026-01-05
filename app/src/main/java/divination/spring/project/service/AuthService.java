package divination.spring.project.service;

import org.springframework.beans.factory.annotation.Autowired;
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
                       PasswordEncoder passwordEncoder,StatusRepository statusRepository) { 
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.statusRepository = statusRepository;
  }

  private Integer findStatusIdByValue(String value) {
    Status status = statusRepository.findByStatusValue(value)
      .orElseThrow(() -> new RuntimeException("無效的狀態值: " + value));
    return status.getStatusId();
  }

  /**
  * 註冊新用戶
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
}