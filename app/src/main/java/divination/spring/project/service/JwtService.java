package divination.spring.project.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import divination.spring.project.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;



@Service
public class JwtService {
    // 🚨 新增：在服務啟動時運行，用於檢查 Key 的實際讀取情況
    @PostConstruct 
    public void checkKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(HARDCODED_SECRET_KEY);
            System.err.println("🔑 JWT DEBUG: 硬編碼 Secret Key 原始字串長度: " + HARDCODED_SECRET_KEY.length());
            System.err.println("🔑 JWT DEBUG: Secret Key Base64 解碼後的 **位元組長度**: " + keyBytes.length);
            // 預期輸出應該是 73 (您的字串 Base64 解碼後的位元組數)
        } catch (Exception e) {
             System.err.println("FATAL: Secret Key 解碼失敗: " + e.getMessage());
        }
    }




    // 🚨 最終硬編碼 Secret Key：從您 application.properties 中獲取的值
    // 這將確保簽名和驗證使用完全相同的 Key，排除所有配置注入錯誤。
    private static final String HARDCODED_SECRET_KEY = 
            "VGhpc0lzQW5FZ21hRGV2aW5hdGlvbldlYkxvdmVZb3VPdmVyVGhlQXN0ZXJvaWRzV2hpdGVDcmVhbWFuZExlbW9u";
            
    // 最終硬編碼 Expiration (24 小時)
    private static final long EXPIRATION = 86400000L; 

    public JwtService() {
        // 保持空，因為不再使用 @Value
    }
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User) {
            claims.put("userId", ((User) userDetails).getId()); 
        }
        return generateToken(claims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .claims(extraClaims) 
                .subject(userDetails.getUsername()) 
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                // 修正：強制指定 HS512 算法
                .signWith(getSignInKey(), SignatureAlgorithm.HS512) 
                .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token) 
                .getPayload();
    }

    private SecretKey getSignInKey() { 
        byte[] keyBytes = Decoders.BASE64.decode(HARDCODED_SECRET_KEY); // 使用硬編碼 Key
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}