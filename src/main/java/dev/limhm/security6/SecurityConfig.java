package dev.limhm.security6;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

//@EnableWebSecurity
@Configuration
public class SecurityConfig {

  /*
   * 한 개 이상의 SecurityFilterChain 빈이 필요하다.
   * SecurityFilterChain 빈을 정의하게 되면 자동설정에 의한 SecurityFilterChain 빈은 생성되지 않는다.
   * */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName("customParam=y");

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/logoutSuccess").permitAll()
            .anyRequest().authenticated())  // 인가 API
        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
            .successHandler(new AuthenticationSuccessHandler() {
              @Override
              public void onAuthenticationSuccess(HttpServletRequest request,
                  HttpServletResponse response, Authentication authentication)
                  throws IOException, ServletException {
                SavedRequest savedRequest = requestCache.getRequest(request, response);
                String redirectUrl = savedRequest.getRedirectUrl();
                response.sendRedirect(redirectUrl);
              }
            }))
        .requestCache(httpSecurityRequestCacheConfigurer -> httpSecurityRequestCacheConfigurer
            .requestCache(requestCache));  // 요청 캐시 RequestCache, SavedRequestl
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
