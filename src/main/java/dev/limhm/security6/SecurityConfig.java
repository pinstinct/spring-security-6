package dev.limhm.security6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())  // 인가 API
        /*
         * 보안에 취약하므로 잘 사용하지 않는다.
         * 로그인에 성공하면 응답 헤더에 Authorization 에 'ID:PW' 문자열을 base64 인코딩 한 값이 노출된다.
         * */
        .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));  // 인증 API (기본 인증 필터)
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
