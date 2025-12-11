package divination.spring.project.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder; // 引入 PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) { 
        this.userDetailsService = userDetailsService;
    }
    
  

    /**
     * 核心安全過濾鏈配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF
            .csrf(AbstractHttpConfigurer::disable)
            // 啟用 CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Session 登入配置 (讓 Spring Security 處理登入和 Session 建立)
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login") 
                .usernameParameter("email") 
                // 登入成功處理
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=UTF-8");
                    // 返回使用者資訊，以便前端更新 Pinia Store
                    String responseBody = String.format("{\"id\": 1, \"email\": \"%s\", \"message\": \"登入成功\"}", 
                                                      authentication.getName());
                    response.getWriter().write(responseBody);
                })
                // 登入失敗處理
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\":\"帳號或密碼錯誤\"}");
                })
                .permitAll()
            )

            // 配置認證失敗處理 (未登入存取受保護資源時)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"message\":\"您未登入或 Session 已失效\"}");
                    }
                })
            )

            // 配置授權規則
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() 
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                // 所有其他路徑都需要認證
                .anyRequest().authenticated()
            );
            
        return http.build();
    }

    /**
     * CORS 配置 Bean：允許前端跨域存取並允許攜帶 Cookie
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 認證提供者 (DaoAuthenticationProvider)
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); 
        authProvider.setPasswordEncoder(passwordEncoder); 
        return authProvider;
    }
}