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
        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
//            .loginPage("/loginPage")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/", true)
            .failureUrl("/failed")
            .usernameParameter("userId")
            .passwordParameter("passwd")
            // 위에 설정보다 handler 설정이 우선 순위가 높다.
            .successHandler((request, response, authentication) -> {
              System.out.println("authentication : " + authentication);
              response.sendRedirect("/home");
            })
            .failureHandler((request, response, exception) -> {
              System.out.println("exception : " + exception.getMessage());
              response.sendRedirect("/login");
            })
            .permitAll()
        );  // 인증 API (폼 인증 필터)
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
