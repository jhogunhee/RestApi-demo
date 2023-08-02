# 공지글 RestApi 구현

### 문제 해결 전략
* Notice제목, 콘텐츠, 시작 날짜, 종료 날짜, 첨부 파일 등과 같은 모든 필수 필드를 포함하여 
공지 데이터를 나타내도록 엔터티 클래스를 생성. 또한 등록 날짜, 조회 수 및 작성자 정보에 대한 필드 포함

* Notice클래스 의 엔터티 에 대한 CRUD(만들기, 읽기, 업데이트, 삭제) 작업 구현
  NoticeService 서비스 클래스는 공지글과 관련된 비즈니스 로직 및 데이터 조작을 처리

* 게시글 삭제시 게시글에 포함된 첨부파일(파일디렉토리 및 DB)에 있는 내용 모두 삭제
* 게시글에 있는 첨부파일 수정시 파일 디렉토리에 있는 첨부파일 모두 삭제 및 재생성작업
  
## Getting Started
H2DB 설치 및 해당 설정으로 접속

![image](https://github.com/jhogunhee/RestApi-demo/assets/31683173/0b09ccbd-5c28-4291-8153-5375ca28c5e5)

서버 실행시 테이블 자동생성 

![image](https://github.com/jhogunhee/RestApi-demo/assets/31683173/3b6d510a-ec6a-438f-a57b-524a3ba22c5f)

RESTAPI 송신을 위한 Postman 설치 및 실행
* 공지글과 첨부파일이 동시에 존재하는 경우 json으로 보낼수 없기 떄문에 form-data형식으로 송신

API목록 <br/>
GET http://localhost:8080/api/notice 공지글 전체조회 <br/>
GET http://localhost:8080/api/notice/${id} 공지글 번호로 조회 <br/>

POST http://localhost:8080/api/notice 공지글 등록 <br/>
PUT  http://localhost:8080/api/notice/${id} 공지글 수정 <br/>
DELETE http://localhost:8080/api/notice/${id} 공지글 삭제





