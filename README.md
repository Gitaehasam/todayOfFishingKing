# 🐟 오늘은 낚시왕
낚시에 입문하고자 하는 MZ세대 맞춤 초보자용 낚시 서비스 🎣
</br> 

## 🛠️ 기술스택
<div>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/JMeter-D22128?style=for-the-badge&logo=JMeter&logoColor=white">
</div>
<div>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
  <img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">  
  <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">  
  <img src="https://img.shields.io/badge/cloudFront-E71D1D?style=for-the-badge&logo=cloudFront&logoColor=white">  
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black"> 
</div>
<div>
 <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">
 <img src="https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white">
 <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=Sass&logoColor=white"/>
 <img src="https://img.shields.io/badge/Three.js-000000?style=for-the-badge&logo=Three.js&logoColor=white"/>
  <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white"/>
</div>

<br>

## 📙 목차
1. [서비스 목표](#서비스-목표)  
2. [아키텍처](#아키텍처)  
3. [기술 스택 선정 이유](#기술-스택-선정-이유)  
4. [기능](#기능)  
5. [성능 테스트 결과](#게시판-조회에-cache를-적용한-성능-테스트-결과)
6. [회고](#회고)
7. [그라운드 룰](#ground-rules)

<br>

## 👀 서비스 목표
- 코로나 시기 낚시 인구가 949만명에서 1,138만명으로 약 20% 증가했습니다.
입문자들이 낚시 용어를 어려워하는 것에서 시작해서 입문자의 즐거운 낚시를 위한 서비스를 기획했습니다.

<br>

## ⚙️ 아키텍처
 <img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/52879aec-2ecf-4682-939a-e8594ba20bfb" width="800" />


<br>

## 😶 기술 스택 선정 이유
|Framework|선정 이유|
|---------|---------|
|Spring Boot|Spring과 비교했을 때 환경 설정을 쉽게 할 수 있어서 개발 생산성 증가|
|MySQL|구조화된 데이터 관리 및 오픈 소스 라이센스를 따르기 때문에 무료 사용 가능|
|JPA|ORM을 사용함으로써 Entity와 데이터베이스의 테이블을 매핑함|
|QueryDSL|조건이 복잡할 때 동적 쿼리를 사용해서 단일 API로 필터링을 걸 수 있음|
|AWS EC2|추후 데이터 처리량 증가에 대비해 높은 안정성 및 확장성을 가진 EC2를 선택|
|NGNIX|어떤 클라이언트도 내부 서버와 직접 통신하지 못 하게 하기 위해 reverse proxy 서버로 사용. 또한, 가벼우면서도 높은 성능을 가지고 있어서 선택|
|S3|업로드한 이미지 저장, 정적 리소스 파일 서빙용으로 사용|
|CloudFront|S3는 데이터를 보관하는 서비스인만큼 보안이 중요. S3를 퍼블릭으로 공개하지 않으면서 퍼블릭하게 접근할 수 있도록 하기 위해 사용|
|Docker|컨테이너 기반으로 여러 서버들을 쉽게 관리할 수 있음|
|Redis|유효 기간이 있는 Refresh Token 및 자주 갱신되지 않는 데이터를 캐싱 용도로 저장하기 위한 목적으로 사용|
|OpenVidu|SFU 구조로 WebRTC를 설계할 때 미디어 서버의 역할을 하기 위해 사용. Kurento에 비해서 러닝 커브가 낮아서 개발 생산성 증가|

<br>

## 🌟 기능
### 1. 어종 인식
- Teachable Machine을 이용해 약 50여종의 어류를 인식
 <img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/f9040167-4962-4426-b08c-639a2d799bd1" width="300"/>

<br>

### 2. 라이브 & 포토
- 인스타그램 라이브와 비슷하게 1:N 방식으로 진행
- 해시태그를 기반으로 물고기와 장소 자랑게시판
<img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/f9c62deb-e1b4-4495-95cc-f1112b874123" width="300"/>
&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/d8087274-a336-4b8d-ac83-c67860393a6a" width="300" heigth="1000" />
<br>

### 3. 아쿠아리움
- 내가 잡은 물고기로 꾸미는 나만의 아쿠아리움  
- three.js를 사용하여 생생하게 움직이는 물고기를 볼 수 있음
<img src="https://github.com/user-attachments/assets/ba582249-b5e1-4b42-805c-baddefabf751" width="300" />

<br>

### 4. 초보자 튜토리얼
- 한 단계씩 사진/움짤, 한 줄 멘트를 넣음  
<img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/aadeb5b0-6d06-46a9-b4d6-9402e2f224b4" width="300" />

<br>

### 5. 도감
<img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/54caac35-9b43-4009-b9d2-789edc1bffa9" width="300" />

<br>

### 6. 챗봇을 이용한 낚시 용어 검색  
<img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/0c0110bd-c4da-45dd-94bb-4024ab636fbd" width="300" />

<br>

### 7. 사용자 후기와 해시태그로 검색하는 맞춤형 낚시터 찾기  
<img src="https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/58ea8962-53d1-4f3c-8c77-86140115f974" width="300" />

<br>

## 게시판 조회에 Cache를 적용한 성능 테스트 결과
- 동시 접속 사용자 수는 105명, 초당 처리량 17 ~ 50개을 목표로 성능 테스트를 진행 
- 로컬 캐시인 Ehcache를 이용하고, 2차 캐시를 적용하여 기존 대비 2배 성능 개선
![성능테스트](https://github.com/Gitaehasam/fishingKingOfToday/assets/46569105/6b285196-7bd4-4a18-ab7b-109a2f122b29)
<br>

## 회고
**Keep**

- 민준: 코딩 컨벤션, 깃 컨벤션 마음에 듭니다. 앞으로 개발인생에 큰 기점이 될거같아요 :)
- 민정: Gather 코어 타임, 뭘 하면 다들 열정적으로 참여함
- 승경 : 모르는 것을 물어보는데 주저 없는 분위기
- 지연 : 문제가 생겼을 때 같이 해결 하는 모습
- 태윤:  코드 컨벤션, gather 코어타임
- 자영 : gather 코어 타임, 굳이 시간을 정하지 않아도 다 알아서 들어와서 좋았어요. 깃 정말 많이 공부한 것 같아요

**Problem**

- 민정: 도커 빌드할 때 파일명, 폴더명 이슈 ㅠ.ㅠ 파일명, 폴더명 통합이 필요해요.  QA 일정을 남겨놔야 해요. 토큰은 한 달로 길게 설정해야겠어요.
- 민준: 다음은 서비스 기획을 더 확실히 해야겠어요! 회의를 하면 할 수록 기능이 바뀌어요.
- 태윤: 프로젝트 로딩 속도, 같은 도메인 컨벤션 통일
- 승경: 같은 도메인 담장자와 명확한 의사소통 필요해요(응답DTO로 ~가 있으면 좋고❌, 응답DTO ~ 필요해요.)
- 지연: 인프라를 담당한 민정이가 고생하는 모습을 많이 봐서 다음 프로젝트는 저도 인프라를 참여하고 싶습니다, JIRA 사용하는 방법을 제대로 익히지 않고 해서 아쉬웠습니다.
- 자영 : 폴더명,,,, 컨벤션,,,꼭 정하고 가요,,,프론트 어디를 개발하고 있는지 소통이 부족한 것 같아요…

**Try**

- 민정: 무중단 배포를 하겠습니다. 배포할 때 서버가 잠깐 중단되어야 하는 게 불편해서
- 민준: 기획을 할 때 개개인 할당치를 확실하게 정하겠습니다!!
- 승경: Gather 코어타임처럼 평일의 코어타임이 있었으면 좋겠습니다.
- 태윤: 필요없는 코드는 꼭 제발 지우길…
- 지연: 남은 2월달에 인간젠킨스가 되어 인프라 공부하겠습니다, JIRA 공부하겠읍니다 모처럼 유료툴을 싸피에서 지원해준다는데 마음껏 써야죠!
- 자영: 컨벤션 문서화 하기!! 커밋 횟수 늘이기!!

<br>

## Ground Rules
1. 데일리 스크럼 진행
2. 사유 없는 지각 시 간식 사오기
3. 혼자서 하는 고민은 최대 1시간. 그 이후에는 팀원과 함께 하기
4. 읽씹은 안 돼요. 이모지라도 달기
5. SSAFY에서는 MM, 외부에서는 카카오톡으로 소통
6. 기록을 습관화하기
7. 회의 시간은 최대 1시간 30분
8. 서로의 의견 존중하기
9. 본인 방식에 대한 집착 버리기
10. 자기 비하 하지 않기

