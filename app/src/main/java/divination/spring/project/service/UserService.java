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

    // 依賴注入
    public UserService(UserRepository userRepository, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }
    
    // --- 查詢邏輯 ---

    /**
     * 根據 ID 查詢用戶資料，並整合狀態名稱 (事業/感情)
     */
    public UserResponse getUserProfile(Long userId) {
        // 1. 獲取所有狀態，建立查閱地圖 Map<ID, Value>，以利查找名稱
        Map<Integer, String> statusMap = statusRepository.findAll().stream()
            .collect(Collectors.toMap(Status::getStatusId, Status::getStatusValue));

        // 2. 查詢用戶
        User user = userRepository.findById(userId)
            // 實際應用中應拋出 NotFound 異常，這裡用 RuntimeException 簡化
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); 

        // 3. 轉換為 Response DTO 並整合狀態名稱
        UserResponse response = new UserResponse(user);
        
        // 整合事業狀態名稱
        if (user.getCareerStatusId() != null) {
            response.setCareerStatusName(statusMap.get(user.getCareerStatusId()));
        }
        // 整合感情狀態名稱
        if (user.getRelationshipStatusId() != null) {
            response.setRelationshipStatusName(statusMap.get(user.getRelationshipStatusId()));
        }

        return response;
    }

    /**
     * 獲取所有狀態選項 (供前端下拉選單使用)
     */
    public List<StatusOptionDTO> getAllStatusOptions() {
        return statusRepository.findAll().stream()
            .map(StatusOptionDTO::new)
            .collect(Collectors.toList());
    }

    // --- 更新邏輯 ---

    /**
     * 更新用戶的個人資料和狀態
     */
    @Transactional
    public UserResponse updateProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // 1. 更新暱稱 (只更新非空值)
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        
        // 2. 更新性別
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        
        // 3. 更新事業狀態 ID
        if (request.getCareerStatusId() != null) {
            user.setCareerStatusId(request.getCareerStatusId());
        }
        
        // 4. 更新感情狀態 ID
        if (request.getRelationshipStatusId() != null) {
            user.setRelationshipStatusId(request.getRelationshipStatusId());
        }
        
        userRepository.save(user);
        
        // 重新調用查詢方法，整合最新的狀態名稱後返回
        return getUserProfile(userId);
    }
}
