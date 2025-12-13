package divination.spring.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import divination.spring.project.dto.DivinationHistoryDTO;
import divination.spring.project.model.DivinationLog;
import divination.spring.project.model.RuneDoubleLog;
import divination.spring.project.model.RuneOrientation;
import divination.spring.project.model.SpecificRuneReading;
import divination.spring.project.repository.DivinationLogRepository;
import divination.spring.project.repository.RuneDoubleLogRepository;
import divination.spring.project.repository.RuneSingleLogRepository;
import divination.spring.project.repository.SpecificRuneReadingRepository;

@Service
public class DivinationHistoryService {
    private final RuneSingleLogRepository runeSingleLogRepository;
    private final DivinationLogRepository logRepository;
    private final RuneDoubleLogRepository runeDoubleLogRepository;
    private final SpecificRuneReadingRepository specificRuneReadingRepository;
    // 假設也有 JiaziSignRepository 等其他 Repository

    public DivinationHistoryService(
        DivinationLogRepository logRepository,
        RuneDoubleLogRepository runeDoubleLogRepository,
        RuneSingleLogRepository runeSingleLogRepository, 
        SpecificRuneReadingRepository specificRuneReadingRepository
    ) {
        this.logRepository = logRepository;
        this.runeDoubleLogRepository = runeDoubleLogRepository;
        this.runeSingleLogRepository = runeSingleLogRepository;
        this.specificRuneReadingRepository = specificRuneReadingRepository;
    }

    /**
     * 獲取單個使用者所有 DivinationLog 紀錄，並轉換為包含詳細結果的 DTO
     */
    public List<DivinationHistoryDTO> getHistoryRecords(Long userId) {
        // findByUserIdOrderByDivinationTimeDesc 假設已在 DivinationLogRepository 中聲明
        List<DivinationLog> logs = logRepository.findByUserIdOrderByDivinationTimeDesc(userId);
        
        return logs.stream()
            .map(log -> {
                DivinationHistoryDTO dto = new DivinationHistoryDTO(log);
                getDetailedResult(log, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * 更新占卜紀錄的問題敘述 (question)
     */
    @Transactional
    public boolean updateQuestion(Long logId, Long userId, String newQuestion) {
        // findByLogIdAndUserId 假設已在 DivinationLogRepository 中聲明
        Optional<DivinationLog> optionalLog = logRepository.findByLogIdAndUserId(logId, userId);

        if (optionalLog.isPresent()) {
            DivinationLog log = optionalLog.get();
            log.setQuestion(newQuestion); // setQuestion 已在 DivinationLog 中添加
            logRepository.save(log);
            return true;
        }
        return false;
    }
    
    /**
     * 輔助方法：根據 result_table 查詢詳細結果和解釋，並設置到 DTO 中
     */
    private void getDetailedResult(DivinationLog log, DivinationHistoryDTO dto) {
        
        switch (log.getResultTable()) {
            case "rune_orientations": 
                handleRuneOneResult(log, dto);
                break;
            case "rune_double_logs":
                handleRuneDoubleResult(log, dto);
                break;
            case "fortunestick_jiazi":
                dto.setResult(String.format("六十甲子籤 (第 %d 籤)", log.getResultId()));
                dto.setInterpretation("甲子籤詩的核心解讀查詢邏輯待實作。");
                break;
            default:
                dto.setResult("未知占卜方式");
                dto.setInterpretation("無詳細解釋。");
        }
    }
    // --- 盧恩符文單顆結果處理 ---
    private void handleRuneOneResult(DivinationLog log, DivinationHistoryDTO dto) {
        // ⭐ 修正點 1: 將 Integer 轉換為 Long (解決錯誤 #1)
        Optional<RuneOrientation> optionalRune = runeSingleLogRepository.findById(log.getResultId().longValue()); 
        
        if (optionalRune.isPresent()) {
            RuneOrientation rune = optionalRune.get();
            String orientation = rune.getIsReversed() == 1 ? "逆位" : "正位";
            dto.setResult(String.format("盧恩符文 (單顆)：%s (%s)", rune.getFullNameZh(), orientation));
            dto.setInterpretation(rune.getRuneGeneralMeaning());
        } else {
            dto.setResult("盧恩符文結果缺失");
            dto.setInterpretation("無法找到符文詳細信息。");
        }
    }
    
    // --- 盧恩符文雙顆結果處理 ---
    private void handleRuneDoubleResult(DivinationLog log, DivinationHistoryDTO dto) {
        // longValue() 轉換已在先前修正中完成，此處保持正確
        Optional<RuneDoubleLog> optionalDoubleLog = runeDoubleLogRepository.findById(log.getResultId().longValue()); 
        
        if (optionalDoubleLog.isPresent()) {
            RuneDoubleLog doubleLog = optionalDoubleLog.get();
            
            // ... (查詢兩張牌的專屬解讀 ID)
            
            // 查詢第一張牌 (現況/基礎) 的細節
            String interpretation1 = specificRuneReadingRepository.findById(doubleLog.getRune1SpecificReadingId())
                .map(SpecificRuneReading::getInterpretationText)
                .orElse("無解釋");

            String result1 = getRuneNameFromSpecificReading(doubleLog.getRune1SpecificReadingId()) + " (現況)";

            // 查詢第二張牌 (建議/指引) 的細節
            String interpretation2 = specificRuneReadingRepository.findById(doubleLog.getRune2SpecificReadingId())
                .map(SpecificRuneReading::getInterpretationText)
                .orElse("無解釋");
                
            String result2 = getRuneNameFromSpecificReading(doubleLog.getRune2SpecificReadingId()) + " (建議)";


            dto.setResult(String.format("盧恩符文 (雙顆)：%s, %s", result1, result2));
            dto.setInterpretation(
                String.format("【牌一：現況】\n%s\n\n【牌二：建議】\n%s", interpretation1, interpretation2)
            );

        } else {
            dto.setResult("盧恩雙顆紀錄缺失");
            dto.setInterpretation("無法找到雙顆紀錄中介信息。");
        }
    }
    
    //輔助方法：統一查詢符文名稱的邏輯
    private String getRuneNameFromSpecificReading(Integer specificReadingId) {
        return specificRuneReadingRepository.findById(specificReadingId)
        // flatMap 查詢 SpecificRuneReading 內部的 orientationId
        // 將 Integer 轉換為 Long 
        .flatMap(reading -> runeSingleLogRepository.findById(reading.getOrientationId().longValue())) 
        .map(rune -> {
                String orientation = rune.getIsReversed() == 1 ? "逆位" : "正位";
                return String.format("%s %s", rune.getFullNameZh(), orientation);
            })
            .orElse("未知符文");
    }
}