# 1. 초기화 과정 이해

## SecurityBuilder / SecurityConfigurer : 설정 클래스 

### 실행 순서
- HttpSecurityConfiguration
  - HttpSecurity 객체 생성
  - @Scope("prototype") 빈 생성: 싱글톤 빈이 아니라 HttpSecurity 생성 할 때마다 빈 객체가 생성됨
  - SecurityConfigurer 인터페이스를 구현한 설정 객체(CsrfConfigurer, LogoutConfigurer, ...)들을 포함해 HttpSecurity 객체 생성
    - CsrfConfigurer, LogoutConfigurer 등의 객체들은 SecurityConfigurer 인터페이스를 구현했기 때문에 configure 메서드에서 CsrfFilter, LogoutFilter 등의 Filter 객체도 생성  
- 위에서 생성된 빈(HttpSecurity 객체)은 SpringBootWebSecurityConfiguration 클래스의 defaultSecurityFilterChain 메서드에 주입 
  - SecurityFilterChain 객체 생성

## HttpSecurity / WebSecurity : 초기화 

### 실행 순서

- HttpSecurityConfiguration
  - HttpSecurity 객체 생성 및 SecurityConfigurer 구현 클래스 추가 후 SecurityFilterChain 생성
- HttpSecurity
  - performBuild() 메서드를 통해 RequestMatcher, Filter 목록을 포함해 DefaultSecurityFilterChain 객체 생성

- WebSecurityConfiguration
  - WebSecurity 객체 생성
  - setFilterChains() 메서드를 통해 HttpSecurity 의 DefaultSecurityFilterChain 객체 정보를 securityFilterChains 에 저장
  - addSecurityFilterChainBuilder() 메서드를 통해 Filter 객체 생성
    - Filter 객체는 WebSecurity 객체의 performBuild() 메서드를 통해 생성함
    - 이 때, FilterChainProxy 객체 생성 및 설정 추가  
