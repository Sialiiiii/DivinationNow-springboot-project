package divination.spring.project.config;

import java.io.IOException; // 🚀 新增: IOException 導入
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest; // 🚀 新增: HttpServletRequest 導入
import jakarta.servlet.http.HttpServletResponse; // 🚀 新增: HttpServletResponse 導入
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException; // 🚀 新增: AuthenticationException 導入
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint; // 🚀 新增: BasicAuthenticationEntryPoint 導入
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;


  public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, 
             UserDetailsService userDetailsService, 
             PasswordEncoder passwordEncoder) { 
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      // 關閉 CSRF (適用於 API 服務)
      .csrf(AbstractHttpConfigurer::disable)
      
      // 配置 CORS (使用 corsConfigurationSource bean)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      
             // 🚀 關鍵修正區塊：處理認證失敗 (401)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 返回 401
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"message\":\"Authentication failed: Invalid or missing token.\"}");
                    }
                })
            )

      // 配置授權規則
      .authorizeHttpRequests(auth -> auth
        // 1. 允許所有 /auth/ 路徑 (註冊、登入)
        .requestMatchers("/auth/**").permitAll() 
        
        // 2. 允許 /images/** 路徑 (圖片服務)
        .requestMatchers("/images/**").permitAll() 
        
        // 3. 允許 GET 占卜資料 (讀取籤詩列表) 是公開的
        .requestMatchers(HttpMethod.GET, "/divination/**").permitAll()
        
        // 4. 🚀 關鍵修正：所有 POST (紀錄) 請求都需要驗證
        .requestMatchers(HttpMethod.POST, "/divination/**").authenticated() 

        // 5. 允許 OPTIONS 預檢請求 (CORS)
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
        
        // 6. 其他所有請求都需要驗證
        .anyRequest().authenticated()
      )
      .sessionManagement(session -> session
        // 禁用 Session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authenticationProvider(authenticationProvider()) 
      // 在 UsernamePasswordAuthenticationFilter 之前加入 JWT 過濾器
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // 🚀 修正點：替換通配符 "*" 為明確的前端來源
    // 前端運行在 Vite 伺服器 (預設 5173)，因為要傳遞憑證，必須明確指定來源。
    configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
    
    // 允許所有方法，包括 OPTIONS
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    // 允許所有Header
    configuration.setAllowedHeaders(List.of("*"));
    // 允許傳遞憑證
    configuration.setAllowCredentials(true); 
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 對所有路徑應用此配置
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // 定義 Authentication Provider Bean
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService); 
    authProvider.setPasswordEncoder(passwordEncoder); 
    return authProvider;
  }
}