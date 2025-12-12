package divination.spring.project.service;

import divination.spring.project.model.DivinationLog;
import divination.spring.project.repository.DivinationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    private final DivinationLogRepository logRepository;

    public LogService(DivinationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * 儲存六十甲子籤的占卜紀錄
     */
    @Transactional
    public DivinationLog saveJiaziSignLog(Long userId, Integer signId) {
        // 直接使用泛用方法存檔
        return saveDivinationLog(
            userId, 
            "六十甲子籤", 
            "fortunestick_jiazi", // 確保表格名稱正確
            signId
        );
    }
    
    /**
     * 儲存盧恩符文單顆占卜結果
     */
    @Transactional
    public DivinationLog saveRuneOneLog(Long userId, Integer orientationId) {
        // 直接使用泛用方法存檔
        return saveDivinationLog(
            userId,
            "盧恩符文(單指引)", 
            "rune_orientations", // 確保表格名稱正確
            orientationId
        );
    }

    /**
     * 泛用儲存方法：對應 divination_logs 的六個核心欄位
     */
    @Transactional
    public DivinationLog saveDivinationLog(Long userId, String type, String tableName, Integer resultId) {
        // 創建 DivinationLog 實體並賦值
        DivinationLog log = new DivinationLog();
        log.setUserId(userId);
        log.setDivinationType(type);
        log.setResultTable(tableName);
        log.setResultId(resultId);
        
        return logRepository.save(log);
    }
}