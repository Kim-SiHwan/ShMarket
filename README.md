# ShMarket
중고 거래 웹사이트 제작 프로젝트
---
### 프로젝트 목표 
1. 견고한 어플리케이션 제작 
2. 성능을 개선하는 리팩토링 진행하며 기록과 검증
3. 메시지 큐를 사용한 웹 푸시 알람 제작, 채팅 기능 제작
4. Git을 통한 깔끔한 프로젝트 관리 
5. 단순 CRUD만을 사용하는 것이 아닌 기술의 고도화, 성능 리팩토링
---
### 추가해야할 기능
- 6/29일 기준
- 검색 기능 추가
- SMTP 메일 인증 추가 
- 회원 U, D 변경 

---
### 아키텍처

---
### 개발 환경

**Back**

`Spring Web` `Spring Boot` `Spring Data JPA` `Spring Security` `Maria DB` `Swagger2` 

`RabbitMq` `Firebase Cloud Messaging` `Json Web Token` `Web Socket`

---

**Front**

`Vue`  `Vue-Router` `Vuetify` `Vuex` `Vuex-Persisted State` `Axios`

---

**Deploy ( 아직 배포하지 않은 상태 )**

`AWS EC2` `AWS S3` 

---

### With Blog
- 웹 소켓 JWT 인증 이슈 해결하기
  - https://tjdans.tistory.com/25

- N+1 문제와 파생되는 MultipleBagFetch, Carstesian Product 문제 해결하기

- 조회 성능 최적화하기

- FCM 연동하기 

- 푸시 메시지를 비 동기적으로 사용하기 

