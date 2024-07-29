# 1. 초기화 과정 이해

## 프로젝트 생성/의존성 추가

### 자동 설정에 의한 기본 보안 작동

별도의 설정이나 코드를 작성하지 않아도 SpringBootWebSecurityConfiguration 클래스에 의해 기본적인 웹 보안 기능이 시스템에 연동되어 작동한다.
- 기본적으로 모든 요청에 대하여 인증여부를 검증하고 인증이 승인되어야 자원에 접근 가능
- 인증 방식은 폼 로그인 방식과 HttpBasic 로그인 방식을 제공
- 인증을 시도할 수 있는 로그인 페이지가 자동적으로 생성되어 렌더링
- 인증 승인이 이루어질 수 있도록 기본적으로 제공
  - SecurityProperties 설정 클래스에서 생성
  - username: user, password: 랜덤 문자열 


## SecurityBuilder / SecurityConfigurer : 설정 클래스 

- SecurityBuilder 인터페이스: 웹 보안을 구성 생성 역할을 하며 대표적으로 WebSecurity, HttpSecurity
- SecurityConfigurer 인터페이스: 필터 생성하고 여러 초기화 설정에 관여
  - init(B builder), configure(B builder) 메서드

### 실행 순서
- HttpSecurityConfiguration
  - HttpSecurity 객체 생성
  - @Scope("prototype") 빈 생성: 싱글톤 빈이 아니라 HttpSecurity 생성 할 때마다 빈 객체가 생성됨
  - SecurityConfigurer 인터페이스를 구현한 설정 객체(CsrfConfigurer, LogoutConfigurer, ...)들을 포함해 HttpSecurity 객체 생성
    - CsrfConfigurer, LogoutConfigurer 등의 객체들은 SecurityConfigurer 인터페이스를 구현했기 때문에 configure 메서드에서 CsrfFilter, LogoutFilter 등의 Filter 객체도 생성  
- 위에서 생성된 빈(HttpSecurity 객체)은 SpringBootWebSecurityConfiguration 클래스의 defaultSecurityFilterChain 메서드에 주입 
  - SecurityFilterChain 객체 생성

## HttpSecurity / WebSecurity : 초기화 

### HttpSecurity

HttpSecurityConfiguration 에서 생성 및 초기화를 진행하고, 보안에 필요한 각 설정 클래스와 필터들을 생성하고 최종적으로 **SecurityFilterChain** 빈 생성
- SecurityFilterChain 인터페이스
  - List<Filter> getFilters() 
  - boolean matches(HttpServletRequest): 현재 SecurityFilterChain 에서 처리해야 하는지 여부
- DefaultSecurityFilterChain 클래스: SecurityFilterChain 구현체 
  - 두개의 필드를 가진다.
    - RequestMatcher requestMatcher
    - List<Filter> filters

### WebSecurity

- WebSecurityConfiguration 에서 생성 및 초기화를 진행하고, HttpSecurity 에서 생성한 SecurityFilterChain 빈을 SecurityBuilder 에 저장
- WebSecurity 가 build() 를 실행하면 SecurityBuilder 에서 SecurityFilterChain 을 꺼내 **FilterChainProxy** 생성자에 전달
- FilterChainProxy
  - List<SecurityFilterChain> securityFilterChains 변수에 SecurityFilterChain 저장 

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

## DelegatingFilterProxy / FilterChainProxy : 런타임 요청 초기화 

### Filter

- 서블릿 필터는 웹 어플리케이션에서 HttpServletRequest, HttpServletResponse 를 가공하거나 검사하는데 사용하는 구성 요소
- 서블릿 필터는 서블릿 컨테이너(WAS: tomcat)에서 관리 
- Filter 인터페이스에는 init(), doFilter(), destroy() 메서드가 있음

### 실행 순서

Client -> FilterChain(Filter -> Filter -> Filter) -> Servlet

### DelegatingFilterProxy

- Filter 를 구현한 클래스
- 스프링에서 사용되는 필터로 서블릿 컨테이너와 스프링 애플리케이션 컨텍스트 간의 연결고리 역할을 하는 필터 
- 모든 최초의 요청은 DelegatingFilterProxy 거쳐 스프링 컨테이너로 전달
- 'springSecurityFilterChain' 이름으로 생성된 빈을 ApplicationContext 에서 찾아 요청을 위임


### FilterChainProxy

- 'springSecurityFilterChain' 이름으로 생성되는 필터 빈 
- DelegatingFilterProxy 부터 요청을 위임 받음 

## 사용자 정의 보안 설정하기

