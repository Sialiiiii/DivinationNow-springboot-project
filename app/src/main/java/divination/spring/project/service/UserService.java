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
        // 1. 獲取所有狀態，建立查閱地圖 Map<ID, Value (英文代碼)>
        Map<Integer, String> statusMap = statusRepository.findAll().stream()
            // statusMap 儲存的是狀態 ID 對應的英文代碼 (e.g., 1 -> "STUDENT")
            .collect(Collectors.toMap(Status::getStatusId, Status::getStatusValue)); 

        // 2. 查詢用戶
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); 

        // 3. 轉換為 Response DTO (包含性別的中文轉換)
        UserResponse response = new UserResponse(user);
        
        // 整合事業狀態名稱
        if (user.getCareerStatusId() != null) {
            String englishValue = statusMap.get(user.getCareerStatusId());
            // ⭐ 修正點 1: 使用 StatusMapping 轉換為中文名稱
            response.setCareerStatusName(StatusMapping.getChineseName(englishValue));
        }
        
        // 整合感情狀態名稱
        if (user.getRelationshipStatusId() != null) {
            String englishValue = statusMap.get(user.getRelationshipStatusId());
            // ⭐ 修正點 2: 使用 StatusMapping 轉換為中文名稱
            response.setRelationshipStatusName(StatusMapping.getChineseName(englishValue));
        }

        return response;
    }

    /**
     * 獲取所有狀態選項 (這裡 StatusOptionDTO 內部會調用 StatusMapping 轉換為中文)
     */
    public List<StatusOptionDTO> getAllStatusOptions() {
        return statusRepository.findAll().stream()
            .map(StatusOptionDTO::new) // StatusOptionDTO 構造函數會執行中文轉換
            .collect(Collectors.toList());
    }

    // --- 更新邏輯 ---

    /**
     * 更新用戶的個人資料和狀態
     */
    @Transactional // ⭐ 確保事務開啟，實體變更會被追蹤
    public UserResponse updateProfile(Long userId, UserUpdateRequest request) {
        // 1. 查詢要更新的用戶實體
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // ⭐ 修正點 3: 補齊所有的更新邏輯！ ⭐
        
        // 2. 更新暱稱 (只更新非空值)
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        
        // 3. 更新性別 (Gender)
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        
        // 4. 更新事業狀態 ID (ID 可以設為 null，代表清除狀態)
        user.setCareerStatusId(request.getCareerStatusId());
        
        // 5. 更新感情狀態 ID
        user.setRelationshipStatusId(request.getRelationshipStatusId());

        // 6. 儲存實體：雖然有 @Transactional，但明確調用 save 可以確保數據立即同步
        userRepository.save(user); 
        
        // 7. 重新調用查詢方法，整合最新的狀態名稱後返回
        return getUserProfile(userId);
    }
}