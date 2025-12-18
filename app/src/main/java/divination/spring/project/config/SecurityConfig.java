package divination.spring.project.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import divination.spring.project.model.Admin;
import divination.spring.project.model.User;
import divination.spring.project.service.AdminDetailsService;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 啟用 @PreAuthorize
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AdminDetailsService adminDetailsService;

    public SecurityConfig(
        @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, 
        AdminDetailsService adminDetailsService
    ) { 
        this.userDetailsService = userDetailsService;
        this.adminDetailsService = adminDetailsService;
    }

    /**
     * 核心安全過濾鏈
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 登入配置：讓使用者與管理者共用一個 Filter 入口
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login") 
                .usernameParameter("email") // 前端管理者登入時，欄位名稱仍傳 email，但內容填 admin_username
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=UTF-8");
                    
                    Object principal = authentication.getPrincipal();
                    String responseBody;

                    // 動態判斷登入者身分
                    if (principal instanceof Admin admin) {
                        responseBody = String.format(
                            "{\"id\": %d, \"username\": \"%s\", \"role\": \"ADMIN\", \"message\": \"管理員登入成功\"}", 
                            admin.getId(), admin.getUsername());
                    } else if (principal instanceof User user) {
                        responseBody = String.format(
                            "{\"id\": %d, \"email\": \"%s\", \"username\": \"%s\", \"careerStatusId\": %d, \"relationshipStatusId\": %d, \"role\": \"USER\", \"message\": \"登入成功\"}", 
                            user.getId(), user.getEmail(), user.getUsername(),
                            user.getCareerStatusId(), user.getRelationshipStatusId());
                    } else {
                        responseBody = "{\"message\": \"登入身分未知\"}";
                    }
                    
                    response.getWriter().write(responseBody);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\":\"帳號或密碼錯誤\"}");
                })
                .permitAll()
            )

            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\":\"您未登入或 Session 已失效\"}");
                })
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/admin/auth/**").permitAll() 
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                
                // 💡 修正點：使用 hasAuthority 並寫全稱 ROLE_ADMIN
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                
                // 保留你的占卜歷史配置
                .requestMatchers("/divination/history/**").authenticated()
                
                .requestMatchers(HttpMethod.GET, "/api/user/**").authenticated() 
                .requestMatchers(HttpMethod.PATCH, "/api/user/profile").authenticated()
                .anyRequest().authenticated()
            );

        // 註冊兩個認證提供者：一個查使用者表，一個查管理者表
        http.authenticationProvider(userAuthenticationProvider(passwordEncoder));
        http.authenticationProvider(adminAuthenticationProvider(passwordEncoder));
            
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); 
        authProvider.setPasswordEncoder(passwordEncoder); 
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService); 
        authProvider.setPasswordEncoder(passwordEncoder); 
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}