package divination.spring.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.RuneDoubleLog;
import divination.spring.project.model.RuneSingleLog;
import divination.spring.project.repository.DivinationLogRepository;
import divination.spring.project.repository.RuneDoubleLogRepository;
import divination.spring.project.repository.RuneSingleLogRepository;

@Service
public class LogService {

    private final DivinationLogRepository divinationLogRepository;
    private final RuneSingleLogRepository runeSingleLogRepository;
    private final RuneDoubleLogRepository runeDoubleLogRepository;

    public LogService(DivinationLogRepository divinationLogRepository, 
                      RuneSingleLogRepository runeSingleLogRepository,
                      RuneDoubleLogRepository runeDoubleLogRepository) {
        this.divinationLogRepository = divinationLogRepository;
        this.runeSingleLogRepository = runeSingleLogRepository;
        this.runeDoubleLogRepository = runeDoubleLogRepository;
    }

    /**
     * 儲存單顆盧恩符文占卜紀錄
     */
    @Transactional
    public DivinationLog saveRuneSingleLog(Long userId, RuneSingleLog singleLog) {
        // 1. 儲存 RuneSingleLog
        RuneSingleLog savedRuneLog = runeSingleLogRepository.save(singleLog);

        // 2. 儲存 DivinationLog (主日誌)
        DivinationLog mainLog = new DivinationLog();
        mainLog.setUserId(userId);
        mainLog.setDivinationType("RUNE_SINGLE");
        mainLog.setResultTable("rune_single_logs");
        mainLog.setResultId(savedRuneLog.getSingleLogId());

        return divinationLogRepository.save(mainLog);
    }

    /**
     * 儲存雙顆盧恩符文占卜紀錄
     */
    @Transactional
    public DivinationLog saveRuneDoubleLog(Long userId, RuneDoubleLog doubleLog) {
        // 1. 儲存 RuneDoubleLog
        RuneDoubleLog savedRuneLog = runeDoubleLogRepository.save(doubleLog);

        // 2. 儲存 DivinationLog (主日誌)
        DivinationLog mainLog = new DivinationLog();
        mainLog.setUserId(userId);
        mainLog.setDivinationType("RUNE_DOUBLE");
        mainLog.setResultTable("rune_double_logs");
        mainLog.setResultId(savedRuneLog.getLogId());

        return divinationLogRepository.save(mainLog);
    }

    /**
     * 🚀 新增：儲存六十甲子籤占卜紀錄
     * 籤詩 ID 直接寫入 result_id，result_table 記錄來源表名
     */
    @Transactional
    public DivinationLog saveJiaziSignLog(Long userId, Long signId) {
        
        // 這裡不需要額外的 Log 表，因為結果 (signId) 已經是最終的 ID
        
        DivinationLog mainLog = new DivinationLog();
        mainLog.setUserId(userId);
        mainLog.setDivinationType("JIAZI_STICK");
        mainLog.setResultTable("fortunestick_jiazi"); // 資料表名稱
        mainLog.setResultId(signId); // 籤詩的 PK (jiazi_sign_id)

        return divinationLogRepository.save(mainLog);
    }
}