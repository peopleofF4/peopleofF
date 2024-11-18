![header](https://capsule-render.vercel.app/api?type=waving&color=gradient&height=300&section=header&text=PeopleOfF&fontSize=90)

  <br>
  <br>
  
---
# PeopleOfF

  <br>

### 🛵 개발기간

> 2024.11.06 ~ 2024.11.18

<br>
  
### 📖 프로젝트 소개
 
> PeopleOfF는 고객과 매장 소유자 모두를 위한 온라인 및 대면 주문 과정의 주문 관리 플랫폼 입니다. <br>
> 일반 사용자와 관리자 모두를 위한 전용 페이지를 포함하여 다양한 시나리오에서 주문의 원활한 운영과 관리를 보장합니다.

<br>

### 👥 팀원

<table border="1" class="table">
  <thead>
    <tr>
        <th scope="col" style="text-align: center;"> 팀장 </th>
        <th scope="col" style="text-align: center;"> 팀원 </th>
        <th scope="col" style="text-align: center;"> 팀원 </th>
        <th scope="col" style="text-align: center;"> 팀원 </th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/kimsung3113"><img src="https://avatars.githubusercontent.com/u/132237905?v=4" width="100px;" alt=""/></a><br /></td>
      <td align="center"><a href="https://github.com/kwj0605"><img src="https://avatars.githubusercontent.com/u/107970778?v=4" width="100px;" alt=""/></a><br /></td>
      <td align="center"><a href="https://github.com/sonoopy"><img src="https://avatars.githubusercontent.com/u/172015030?v=4" width="100px;" alt=""/></a><br /></td>
      <td align="center"><a href="https://github.com/wooseok50"><img src="https://avatars.githubusercontent.com/u/155416976?v=4" width="100px;" alt=""/></a><br /></td>
    </tr>
      <tr>
        <th scope="col" style="text-align: center;"><a href="https://github.com/kimsung3113"> 김성훈 </a></th>
        <th scope="col" style="text-align: center;"><a href="https://github.com/kwj0605"> 김우진 </a></th>
        <th scope="col" style="text-align: center;"><a href="https://github.com/sonoopy"> 최소진 </a></th>
        <th scope="col" style="text-align: center;"><a href="https://github.com/wooseok50"> 조우석 </a></th>
    </tr>
  </tbody>
</table>

  <br>

> **김성훈** <br>
>`회원 기능 구현, jwt, security, handler, common response, auditing` <br><br>
> **김우진** <br>
> `master, manager, category 기능 구현, AI api 연동`<br><br>
> **최소진** <br>
> `menu, store, review 기능 구현` <br><br>
> **조우석** <br>
> `order, payment 기능 구현` <br><br>

  <br>
  <br>

---

## 🛠️ Stacks

  <br>

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><br>
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><br>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><br>
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><br>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><br>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><br>

  <br>
  <br>

---

## ✨ 주요 기능

<br>

- **주문 관리**

  - **주문 취소:** 주문 생성 후 5분 이내에만 취소 가능하도록 제한
  - **주문 유형:** 온라인 주문과 대면 주문(가게에서 직접 주문) 모두 지원
  - **대면 주문 처리:** 가게 사장님이 직접 대면 주문을 접수
  - **사용자 페이지**: 고객이 카테고리를 탐색하고, 매장을 선택하고, 메뉴를 확인하며, 온라인으로 주문
  - **관리자 페이지**: 매장 소유자와 관리자가 주문을 효율적으로 조회, 수정 및 관리할 수 있는 제어 기능 제공

  <br>


- **데이터 보존 및 삭제 처리**

  - **데이터 보존:** 모든 데이터는 완전 삭제되지 않고 숨김 처리로 관리
  - **상품 숨김:** 개별 상품도 숨김 처리 가능하도록 구현
  - **데이터 감사 로그:** 모든 정보에 생성일, 생성 아이디, 수정일, 수정 아이디, 삭제일, 삭제 아이디를 포함

  <br>


- **접근 권한 관리**

  - **고객:** 자신의 주문 내역만 조회 가능
  - **가게 주인:** 자신의 가게 주문 내역, 가게 정보, 주문 처리 및 메뉴 수정 가능
  - **관리자:** 모든 가게 및 주문에 대한 전체 권한 보유

  <br>


- **AI API 연동**

  - **상품 설명 자동 생성:** AI API 연동으로 메뉴 등록 시 설명 추천
  - **AI 요청 기록:** AI API 요청 질문과 대답은 모두 데이터베이스에 저장

  <br>


- **보안**
  - **JWT 인증:** Spring Security와 JWT(Json Web Token)를 이용한 인증 및 권한 관리
  - 권한 확인 : CUSTOMER 이상의 권한은 요청마다 저장되어 있는 권한 값과 동일한지 체크
  - **비밀번호 암호화:** BCrypt 해시 알고리즘을 사용한 비밀번호 암호화
  - **데이터 유효성 검사:** 서버 측 데이터 유효성 검사를 위해 Spring Validator 사용

<br>
  
- **검색조건 및 정렬기능**
    - 정렬기능은 기본적으로 생성일순, 수정일순을 기준
    - 서치 기능에는 10건, 30건, 50건 기준으로 페이지에 노출
 
  
<br>
<br>

---

## 📂 서비스 구성 및 실행 방법

<br>

### ⚙️ 실행환경

```
JDK 17
SpringBoot 3.3.5

```

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/{DB_NAME}
    username: ${DB_ID}
    password: ${DB_PW}
    driver-class-name: org.postgresql.Driver
  servlet:
    multipart:
      location: C:/Users/rladn/Temp

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

jwt:
  secret: ${SECRET_KEY}

  access:
    expiration: 90
    # header: Authorization

  refresh:
    expiration: 900
    #header: Authorization-refresh

gemini:
  api:
    url: ${GEMINI_URL}
    key: ${GEMINI_KEY}

```
<br>
<br>

---
## 브랜치 전략

![branch](https://github.com/user-attachments/assets/2fe71cc2-8583-4a3e-a43c-3b3d9bbce07e)

1. upstream에서는 `main` 브랜치와 `develop` 브랜치만 관리한다.
2. 개발 feature는 `develop` 브랜치에서 따서 작업 후 `develop` 브랜치로 머지한다.
3. 협업으로 인한 추가 브랜치가 필요한 경우 upstream에서 `develop` 기준 `feature` 브랜치를 생성하여 2와 같은 방식으로 작업한다.

<br>
<br>

---

## 🎈 ERD

<details>
<summary>열기</summary>
<img width="6544" alt="ERD" src="https://github.com/user-attachments/assets/de76e536-5bf7-4c83-bc72-e168ae523b33">
</details>

<br>
<br>

---

## 📌 Architecture

<details>
<summary>열기</summary>
<img width="695" alt="architecture" src="https://github.com/user-attachments/assets/68a2890f-d128-4eae-8e86-5f6d99135995">
</details>

<br>
<br>

---

## 📄 API 명세서

<details>
<summary>열기</summary>
https://sneaky-prawn-eed.notion.site/API-1371ee33c50580a09f06de93422b1f29
</details>

<br>
<br>
<br>

---

## 📀 테스트용 포스트맨 컬렉션
<details>
<summary>열기</summary>
  https://github.com/user-attachments/files/17798169/PeopleOfF.postman_collection.json
</details>

<br>
<br>
<br>

![footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&height=300&section=footer)
