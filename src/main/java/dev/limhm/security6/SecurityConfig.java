package dev.limhm.security6;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@EnableWebSecurity
@Configuration
public class SecurityConfig {

  /*
   * 한 개 이상의 SecurityFilterChain 빈이 필요하다.
   * SecurityFilterChain 빈을 정의하게 되면 자동설정에 의한 SecurityFilterChain 빈은 생성되지 않는다.
   * */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/logoutSuccess").permitAll()
            .anyRequest().authenticated())  // 인가 API
        .formLogin(Customizer.withDefaults())
        .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
            .logoutUrl("/logoutProc")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc", "GET"))  // logoutUrl 보다 우선순위 높음
            .logoutSuccessUrl("/logoutSuccess")
            .logoutSuccessHandler(new LogoutSuccessHandler() {
              @Override
              public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                  Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/logoutSuccess");
              }
            })  // logoutSuccessUrl 보다 우선순위 높음
            .deleteCookies("JSESSIONID", "remember-me")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .addLogoutHandler(new LogoutHandler() {
              @Override
              public void logout(HttpServletRequest request, HttpServletResponse response,
                  Authentication authentication) {
                // 세션 처리
                HttpSession session = request.getSession();
                session.invalidate();
                // Authentication 객체 처리
                SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
                // SecurityContext 객체 처리
                SecurityContextHolder.getContextHolderStrategy().clearContext();
              }
            }).permitAll()
        );  // 인증 API (로그 아웃)
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {  // 사용자 추가 설정
    UserDetails user = User.withUsername("user")
        .password("{noop}1111")
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

}
