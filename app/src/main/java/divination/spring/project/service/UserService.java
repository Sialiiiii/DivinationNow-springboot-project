package divination.spring.project.service;

import divination.spring.project.dto.StatusOptionDTO;
import divination.spring.project.dto.UserResponse;
import divination.spring.project.dto.UserUpdateRequest;
import divination.spring.project.model.Status;
import divination.spring.project.model.User;
import divination.spring.project.repository.StatusRepository;
import divination.spring.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    public UserService(UserRepository userRepository, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }
    

    /**
     * 根據 ID 查詢用戶資料
     */
    public UserResponse getUserProfile(Long userId) {
        Map<Integer, String> statusMap = statusRepository.findAll().stream()
            .collect(Collectors.toMap(Status::getStatusId, Status::getStatusValue)); 

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); 

        UserResponse response = new UserResponse(user);
        
        if (user.getCareerStatusId() != null) {
            String englishValue = statusMap.get(user.getCareerStatusId());
            response.setCareerStatusName(StatusMapping.getChineseName(englishValue));
        }
        
        if (user.getRelationshipStatusId() != null) {
            String englishValue = statusMap.get(user.getRelationshipStatusId());
            response.setRelationshipStatusName(StatusMapping.getChineseName(englishValue));
        }

        return response;
    }

    /**
     * 獲取所有狀態選項
     */
    public List<StatusOptionDTO> getAllStatusOptions() {
        return statusRepository.findAll().stream()
            .map(StatusOptionDTO::new)
            .collect(Collectors.toList());
    }


    /**
     * 更新用戶的個人資料和狀態
     */
    @Transactional
    public UserResponse updateProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        
        user.setCareerStatusId(request.getCareerStatusId());
        user.setRelationshipStatusId(request.getRelationshipStatusId());

        userRepository.save(user); 
        
        return getUserProfile(userId);
    }
}