# settlement-system-spring

## 프로젝트 소개

### 동영상 스트리밍 및 정산 기능을 제공하는 Spring Boot 기반 백엔드 API입니다.


## 프로젝트 기간
### 24.8.19 ~ 24.9.14


## 기술 스택

[![Java](https://skillicons.dev/icons?i=java)](https://skillicons.dev)
[![Spring Boot](https://skillicons.dev/icons?i=spring)](https://skillicons.dev)
[![MySQL](https://skillicons.dev/icons?i=mysql)](https://skillicons.dev)
[![Docker](https://skillicons.dev/icons?i=docker)](https://skillicons.dev)
[![Gradle](https://skillicons.dev/icons?i=gradle)](https://skillicons.dev)
[![OAuth2](https://skillicons.dev/icons?i=auth0)](https://skillicons.dev)

## 주요 기능

1. **회원 서비스**
   - 사용자 및 판매자 계정에 대한 소셜 로그인 기능 구현 (Kakao OAuth2).
   - JWT를 사용한 로그인 및 로그아웃 처리.

2. **비디오 스트리밍 서비스**
   - 비디오 재생 추적을 위한 API 제공.
   - 광고 조회 수 계산 및 재생 위치 저장 기능.

3. **비디오 관리**
   - 데이터베이스 쿼리를 통한 비디오 등록, 조회, 수정 및 삭제 기능.

4. **데이터 통계 및 정산 시스템**
   - Spring Batch를 활용하여 상위 비디오 및 수익 계산 기능.
   - 일별, 주별, 월별로 조회 수와 광고 수익을 기반으로 정산.

5. **JWT 인증 및 관리**
   - 만료된 JWT에 대한 자동 재발급 로직 구현.
   - JWT 서명 검증 및 클레임 관리 강화.

6. **리프레시 토큰 관리**
   - 만료된 리프레시 토큰을 데이터베이스에서 자동으로 삭제하는 로직.
   - 주기적인 배치 작업을 통해 만료된 토큰 정리.


## 트러블 슈팅

1. [배치 작업 실행 실패](https://velog.io/@kkd0059/배치-작업-실행-실패)
2. [JWT 인증 실패](https://velog.io/@kkd0059/JWT-인증-실패)
3. [만료된 리프레시 토큰이 데이터베이스에 남아 있음](https://velog.io/@kkd0059/만료된-리프레시-토큰이-데이터베이스에-남아-있음)

## 디렉토리 구조
```
📁src 
  ┣ 📁main
    ┣ 📁java
      ┣ 📁batch
      ┃  ┣ 📁settlement 
      ┃  ┗ 📁statistics
      ┣ 📁common
      ┃  ┣ 📁exception
      ┃  ┗ 📁response
      ┣ 📁domain
      ┃  ┣ 📁ad 
      ┃  ┣ 📁adview
      ┃  ┣ 📁playhistory
      ┃  ┣ 📁user
      ┃  ┣ 📁video
      ┃  ┗ 📁videoad
      ┣ 📁Oauth2
      ┃  ┗ 📁kakao 
      ┣ 📁security
      ┃  ┣ 📁config
      ┃  ┗ 📁jwt
      ┗     ┗ 📁refeshToken
```
## 프로젝트 후기
Java와 Spring Boot를 활용하여 RESTful API를 구축하는 과정에서 프레임워크의 강력한 기능을 직접 경험할 수 있었습니다. 특히, Spring Boot의 자동 설정 및 간편한 배치 작업 구현 덕분에 빠른 개발이 가능했습니다. JWT 인증 및 리프레시 토큰 관리 과정에서 발생한 여러 트러블슈팅을 통해 문제 해결 능력이 크게 향상되었습니다. 로그 기록과 예외 처리의 중요성을 깨닫게 되었고, 효과적인 디버깅 방법을 배우게 되었습니다. 앞으로도 이번 프로젝트에서 배운 점들을 바탕으로 더 많은 도전에 임하고, 기술적으로 성장하는 기회를 계속 만들어 나가고 싶습니다.
