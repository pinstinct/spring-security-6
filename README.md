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


## SecurityBuilder / SecurityConfigurer

## DelegatingFilterProxy

## 사용자 정의 보안 설정하기 