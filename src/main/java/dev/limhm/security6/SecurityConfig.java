package dev.limhm.security6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder builder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    AuthenticationManager authenticationManager = builder.build();

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/login").permitAll()
            .anyRequest().authenticated())  // 인가 API
        .formLogin(Customizer.withDefaults())
//        .securityContext(securityContext -> securityContext.requireExplicitSave(false))  // SecurityContextPersistenceFilter 를 이용해 제공해주는 세션 사용
        .authenticationManager(authenticationManager)
        .addFilterBefore(customAuthenticationFilter(http, authenticationManager),
            UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http,
      AuthenticationManager authenticationManager) {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(http);
    customAuthenticationFilter.setAuthenticationManager(authenticationManager);
    return customAuthenticationFilter;
  }

  @Bean
  public UserDetailsService userDetailsService() {  // 사용자 추가 설정
    return new CustomUserDetailsService();
  }

}
