package divination.spring.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.dto.RuneDoubleLogRequest;
import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.RuneDoubleLog;
import divination.spring.project.repository.DivinationLogRepository;
import divination.spring.project.repository.RuneDoubleLogRepository;
import divination.spring.project.repository.SpecificRuneReadingRepository;
import divination.spring.project.model.SpecificRuneReading;
import divination.spring.project.model.SpecificRuneReading;

import java.time.LocalDateTime;

@Service
public class LogService {

    private final DivinationLogRepository logRepository;
    private final RuneDoubleLogRepository runeDoubleLogRepository;
    private final SpecificRuneReadingRepository specificRuneReadingRepository;

    public LogService(DivinationLogRepository logRepository,
        RuneDoubleLogRepository runeDoubleLogRepository, SpecificRuneReadingRepository specificRuneReadingRepository) {
        this.logRepository = logRepository;
        this.runeDoubleLogRepository = runeDoubleLogRepository;
        this.specificRuneReadingRepository = specificRuneReadingRepository;
    }


    // 泛用儲存方法
    @Transactional
    public DivinationLog saveDivinationLog(Long userId, String type, String tableName, Integer resultId) {
        DivinationLog log = new DivinationLog();
        log.setUserId(userId);
        log.setDivinationType(type);
        log.setResultTable(tableName);
        log.setResultId(resultId);
        log.setDivinationTime(LocalDateTime.now());
        return logRepository.save(log);
    }

    /**
     * 儲存六十甲子籤的占卜紀錄
     */
    @Transactional
    public DivinationLog saveJiaziSignLog(Long userId, Integer signId) {
        return saveDivinationLog(
            userId, 
            "六十甲子籤", 
            "fortunestick_jiazi",
            signId
        );
    }
    
    /**
     * 儲存盧恩符文單顆占卜結果
     */
    @Transactional
    public DivinationLog saveRuneOneLog(Long userId, Integer orientationId) {
        return saveDivinationLog(
            userId,
            "盧恩符文(單指引)", 
            "rune_orientations", 
            orientationId
        );
    }


/**
     * 儲存盧恩符文雙顆占卜結果 (使用 DTO 傳輸)
     * ⭐ 修正點：必須接收 Long userId 作為第一個參數
     */
    @Transactional
    public DivinationLog saveRuneDoubleLog(Long userId, RuneDoubleLogRequest request) { 
        
        // 1. 提取查找參數
        Integer orientationId1 = request.getRune1SpecificReadingId(); // 實際上是 orientation_id
        Integer orientationId2 = request.getRune2SpecificReadingId();
        
        // 確定狀態 ID (career 或 relationship 總有一個有值)
        Integer statusId = request.getUserCareerStatusId() != null 
                           ? request.getUserCareerStatusId() 
                           : request.getUserRelationshipStatusId();

        if (statusId == null) {
            throw new IllegalArgumentException("必須提供事業或感情狀態 ID。");
        }

        // 2. 執行 ID 查找 (核心轉換邏輯)
        // 牌位: 1=現況/基礎, 2=建議/指引 (使用您確認的 1 和 2)
        SpecificRuneReading reading1 = specificRuneReadingRepository
            .findByOrientationIdAndStatusIdAndIsCurrentStatusPosition(orientationId1, statusId, 1)
            .orElseThrow(() -> new RuntimeException("無法找到第一張牌 (現況) 的特定解讀。"));
        
        SpecificRuneReading reading2 = specificRuneReadingRepository
            .findByOrientationIdAndStatusIdAndIsCurrentStatusPosition(orientationId2, statusId, 2)
            .orElseThrow(() -> new RuntimeException("無法找到第二張牌 (建議) 的特定解讀。"));

        // 獲取正確的 specific_reading_id
        Integer specificReadingId1 = reading1.getSpecificReadingId().intValue();
        Integer specificReadingId2 = reading2.getSpecificReadingId().intValue();

        // 3. 寫入中介表 (rune_double_logs)
        RuneDoubleLog doubleLog = new RuneDoubleLog();
        
        // ⭐ 使用正確的 specific_reading_id 寫入
        doubleLog.setRune1SpecificReadingId(specificReadingId1);
        doubleLog.setRune2SpecificReadingId(specificReadingId2);

        // 狀態 ID 寫入
        doubleLog.setUserCareerStatusId(request.getUserCareerStatusId()); 
        doubleLog.setUserRelationshipStatusId(request.getUserRelationshipStatusId()); 
        
        doubleLog = runeDoubleLogRepository.save(doubleLog);
        Long doubleLogId = doubleLog.getLogId();
        
        // 4. 寫入總表 (divination_logs)
        return saveDivinationLog(
            userId, 
            "盧恩符文(雙顆)", 
            "rune_double_logs",
            doubleLogId.intValue()
        );
    }
}