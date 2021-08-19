### ShMarket
중고 거래 웹사이트 제작 프로젝트
---
### 프로젝트 소개

ShMarket은 당근마켓을 모티브로 제작된 토이 프로젝트로, 
실제 전국 행정동을 통해 입력된 거주지 기반 근처 N Km 이내의 사용자들과의
커뮤니티를 통해 소통이 가능합니다.
또한 주 기능인 물품 거래는 채팅과 키워드 푸시 알림을 제공합니다.
Rabbit MQ와 Web Socket을 통해 채팅을 구현하고,
Rabbit MQ와 Firebase Cloud Messaging을 통해 채팅과 키워드 알림의 웹 푸시 알림을 제공합니다.

---
### 프로젝트 목표 
1. 견고한 어플리케이션 제작 
-> 대용량 데이터, 분산 환경으로 서비스하려면 scale out과 load balancing을 통해 처리해보는 것도 좋을 것 같아 고려중.
2. 성능을 개선하는 리팩토링 진행하며 기록과 검증
3. 메시지 큐를 사용한 웹 푸시 알람 제작, 채팅 기능 제작
4. Git을 통한 깔끔한 프로젝트 관리 
5. 단순 CRUD만을 사용하는 것이 아닌 기술의 고도화, 성능 리팩토링
---
### 아키텍처
![image](https://user-images.githubusercontent.com/66605925/129331256-ff1fde73-a669-4ce1-b270-9a26e0e40e66.png)

---
### With Blog
- 프로젝트 개요
  - https://tjdans.tistory.com/27

- 웹 소켓 JWT 인증 이슈 해결하기
  - https://tjdans.tistory.com/25

- N+1 문제와 파생되는 MultipleBagFetch, Carstesian Product 문제 해결하기
  - https://tjdans.tistory.com/28
  
- 조회 성능 최적화하기
  - 최종 결과 -> 232초 -> 0.004 ~ 0.2초로 개선 
  - https://tjdans.tistory.com/29
 
- 푸시 메시지를 비 동기적으로 사용하기 
  - https://tjdans.tistory.com/31

- Docker-Compose를 사용해 배포 

- Redis 캐싱 전략과 장애 대비
  - https://tjdans.tistory.com/32
--- 

### 개발 환경
![image](https://user-images.githubusercontent.com/66605925/129332681-19fb1ffb-b82c-48c3-b24c-07ef4db55d33.png)


**Back**

`Java` `Spring Boot` `Spring Data JPA` `Spring Security` `MySQL` `Swagger2` 

`RabbitMq` `Firebase Cloud Messaging` `Json Web Token` `Web Socket`

---

**Front**

`Javascript` `Vue`  `Vue-Router` `Vuetify` `Vuex` `Vuex-Persisted State` `Axios`

---

**Deploy ( 아직 배포하지 않은 상태 )**

`AWS EC2` `AWS S3` 

---
### ERD
![image](https://user-images.githubusercontent.com/66605925/129332942-f89752c3-236b-4507-889c-af5feb6bd822.png)

---
### 구현 기능
**`회원`**

1. 회원 가입 ( SMTP를 사용한 E-mail 인증 )

2. 회원 정보 조회

3. 회원 정보 수정

4. 회원 탈퇴

5. 비밀번호 찾기

6. 매너 평가 C, R

![매너평가](https://user-images.githubusercontent.com/66605925/129896273-78efdb06-989b-4a5b-8123-f56e52f5f034.gif)


7. 거래 후기 C, R

![거래후기](https://user-images.githubusercontent.com/66605925/129896283-178b6860-80d5-49f2-8592-9b0d0a3cb676.gif)

8. 회원 차단 C, R, D ( 차단 유저의 게시글 조회하지 않음 )

![차단](https://user-images.githubusercontent.com/66605925/129896303-ee0aaaad-a203-4cba-96b0-950c95ce55d6.gif)


**`지역`**

1. 지역구 조회

2. 지역구 설정

3. 지역간 거리 계산

**`상품`**

1. 상품 추가

1.1 상품 태그 추가 ( 키워드 푸시 알림 전송 )

2. 상품 조회

2.1 본인 거주지 주변 N Km만큼의 동네에 존재하는 상품만 조회

2.2 차단한 사용자의 게시글 필터링 조회

2.3 카테고리 필터링 조회

3. 상품 수정

3.1 이미지 및 제목, 내용 수정 가능

3.2 상품 상태 변경 ( 판매 중, 예약 중, 판매 완료 )

4. 상품 삭제

5. 채팅창 이동

6. 작성자로 상품 조회

7. 사용자 관심상품 목록 조회

**`게시글`**

1. 게시글 추가

2. 게시글 조회

2.1 본인 거주지 주변 N Km만큼의 동네에 존재하는 상품만 조회

2.2 차단한 사용자의 게시글 필터링 조회

2.3 카테고리 필터링 조회

3. 게시글 수정

3.1 이미지 및 제목, 내용 수정 가능

4. 게시글 삭제

5. 댓글 C, R, U, D

![댓글](https://user-images.githubusercontent.com/66605925/129896385-b509d8d4-2b33-47dd-a5c9-fff7b7d767f4.gif)


6. 작성자로 게시글 조회

**`키워드 푸시 알림 ( Redis, Rabbit MQ 사용 )`**

1. 키워드 등록

1.1 DB 키워드 등록

1.2 FCM Topic 등록

2. 키워드 조회

3. 키워드 삭제

3.1 DB 키워드 삭제

3.2 FCM Topic 삭제

4. 키워드 알림 내역 생성

5. 키워드 알림 내역 조회

6. 키워드 알림 상태 변경 ( 안읽음 -> 읽음 )

![키워드](https://user-images.githubusercontent.com/66605925/129896185-010352bb-f02f-4111-8e06-bb65e5d8eddc.gif)


**`채팅 ( Web Socket, Rabbit MQ 사용 )`**

1. 채팅방 생성

2. 채팅방 리스트 조회

3. 채팅방 상세 조회

3.1 채팅 내역 조회

3.2 채팅 전송

![채팅](https://user-images.githubusercontent.com/66605925/129896129-08f0aed0-6a0b-4e14-9c3a-586d29c73f59.gif)


**`Qna`**

1. Qna C, R, D

2. 답변이 달렸을 때 알림 Entity에 추가

**`관리자`**

1. 회원 관리

2. 댓글 관리

3. 상품 관리

4. 게시글 관리

5. Qna 답변

`**유효성 검증**`

1. Front 단에서 1차 검증 수행

2. Filter 단에서 권한 검증 수행 

3. Spring Validate를 통한 검증 수행

`**예외 처리**`

1. Custom Exception 생성해 명확한 의미의 예외 발생

2. RestController Advice를 통해 통합해 처리

3. 사용자에게 적절한 HTTP Status와 Message를 전달
---
### API DOCS
## Member
![member](https://user-images.githubusercontent.com/66605925/130019285-18c80cf5-1170-4853-8ef1-7584219bff51.PNG)

## Admin
![adm](https://user-images.githubusercontent.com/66605925/130019335-015612de-5da4-4976-833f-9577214669e1.PNG)

## Area
![area](https://user-images.githubusercontent.com/66605925/130019313-3a39013a-b3e4-40f6-a2a0-29380afa0195.PNG)

## Board
![board](https://user-images.githubusercontent.com/66605925/130019321-598f3910-0a90-4a88-82ca-0c9a18e7038e.PNG)

## Comment
![co,m,m](https://user-images.githubusercontent.com/66605925/130019276-4f1179a6-d1fb-490d-b368-e133cceaaefa.PNG)

## Product
![prodd](https://user-images.githubusercontent.com/66605925/130019255-e7103098-2712-4bd5-b375-77f986cc15e6.PNG)

## Chat
![chat](https://user-images.githubusercontent.com/66605925/130019268-716ca73e-8205-4a00-9678-bc2afec1d0e8.PNG)

## Push
![push](https://user-images.githubusercontent.com/66605925/130019240-ca1c9a00-d392-4d27-a4cb-9e60a59ef8c6.PNG)

## Notice
![norttuii](https://user-images.githubusercontent.com/66605925/130019349-d3c8598b-4bd4-4c19-93d5-6c37dee8767a.PNG)

## Qna
![Qna](https://user-images.githubusercontent.com/66605925/130019224-1f899507-bcac-4d40-aa41-b3f6046f0a8d.PNG)

---


  
