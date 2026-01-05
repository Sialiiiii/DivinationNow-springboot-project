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

@Configuration // 啟動時先讀這個檔
@EnableWebSecurity // 啟動Spring Security
@EnableMethodSecurity
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
     * 核心過濾
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) //關閉CSRF防護
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) //開啟跨域許可，不同網址才能連
            
            // 登入配置：讓使用者與管理者共用一個 Filter 入口
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login") 
                .usernameParameter("email") // 前端管理者登入時，欄位名稱仍傳 email，但內容填 admin_username
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=UTF-8");
                    
                    Object principal = authentication.getPrincipal();
                    String responseBody;

                    // 判斷登入者身分
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
            // 權限分級
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/admin/auth/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/divination/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/divination/history/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/user/**").authenticated() 
                .requestMatchers(HttpMethod.PATCH, "/api/user/profile").authenticated()
                .requestMatchers(HttpMethod.GET, "/posts").permitAll() 
                .requestMatchers(HttpMethod.POST, "/posts").authenticated()
                .anyRequest().authenticated()
            );

        // 註冊兩個認證提供者：一個查使用者表，一個查管理者表
        http.authenticationProvider(userAuthenticationProvider(passwordEncoder));
        http.authenticationProvider(adminAuthenticationProvider(passwordEncoder));
            
        return http.build();
    }

    // 身分驗證器，設定驗證方法
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


    // 跨域白名單 (CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);  // 允許前端帶 Cookie 過來
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}