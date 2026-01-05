package divination.spring.project.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StatusMapping {

    private static final Map<String, String> STATUS_TO_ZH_MAP;
    
    static {
        Map<String, String> map = new HashMap<>();
        
        // --- 性別 (Gender) ---
        map.put("MALE", "男");
        map.put("FEMALE", "女");
        map.put("PREFER_NOT_TO_SAY", "未填寫"); 

        // --- 事業狀態 (Career) ---
        map.put("EMPLOYED", "就業中");      
        map.put("UNEMPLOYED", "職涯探索中"); 
        map.put("STUDENT", "學生");
        map.put("OTHER", "其他");
        
        // --- 感情狀態 (Relationship) ---
        map.put("MARRIED", "已婚");
        map.put("IN_A_RELATIONSHIP", "穩定交往"); 
        map.put("COMPLICATED", "曖昧");     
        map.put("SINGLE", "一個人也很好");  

        STATUS_TO_ZH_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * 將英文 Status Value 或 Gender 轉換為中文名稱
     */
    public static String getChineseName(String statusValue) {
        return STATUS_TO_ZH_MAP.getOrDefault(statusValue, statusValue);
    }
}