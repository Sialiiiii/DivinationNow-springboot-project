package divination.spring.project.config;

import divination.spring.project.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException; // 引入 JWT 異常
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // 嘗試解析 JWT (🚀 核心除錯區域)
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (JwtException e) {
            // 打印錯誤並繼續，讓 Spring Security 返回 401
            System.err.println("JWT 解析或驗證失敗: " + e.getClass().getName() + " - " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            System.err.println("JWT 服務發生意外錯誤: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }


        // 如果用戶名存在且 SecurityContext 中沒有認證信息
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // 提取 User ID 作為 Principal
                    Object principalId = jwtService.extractClaim(jwt, claims -> claims.get("userId"));
                    Long userId = null;

                    if (principalId instanceof Number) {
                        userId = ((Number) principalId).longValue();
                    } else if (principalId != null) {
                         userId = Long.valueOf(principalId.toString());
                    }

                    // 建立認證 Token：使用 Long (userId) 作為 Principal
                    Object principal = (userId != null) ? userId : userDetails;

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principal, 
                            null, 
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 設置認證上下文
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                System.err.println("JWT 認證過程中發生錯誤 (載入用戶信息失敗等): " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}