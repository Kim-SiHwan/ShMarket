# ShMarket
당근마켓과 같은 서비스를 제공하는 프로젝트
---
### 프로젝트 목표 
1. 견고한 어플리케이션 제작 
2. 성능을 개선하는 리팩토링 진행하며 기록과 검증
3. 메시지 큐를 사용한 웹 푸시 알람 제작, 채팅 기능 제작
4. Git을 통한 깔끔한 프로젝트 관리 ( 제대로 못한거같아서 아쉬움.. )
5. 단순 CRUD만을 사용하는 것이 아닌 기술의 고도화, 성능 리팩토링
---
### 추가해야할 기능
- 6/17일 기준 
- 테스트 코드 작성
- SMTP 메일 인증 ( 비밀번호 변경과 회원가입 )
  - 적용만 하면 된다.
- 회원 탈퇴, 비밀번호 변경 
- FCM 푸시가 2번 이하로만 전송되는 이슈 해결해야함. 
  - background 설정 변경을 통해 해결함. 
- 게시글 ( 중고 매물, 동네 생활 )의 수정 및 삭제 추가 
  - 완료 
- 코드 리팩토링 ( 코드 정리 및 개선, 디자인 패턴의 변경 고려 )
  - 18일부터 시작 
- 모아보기 ( 팔로잉 ) 기능 추가 
  - 완료
- 프론트 뷰 작성 
  - Main, Chat만 변경하면 될듯.
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
