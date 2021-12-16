# 데이터 수집 프로그램

# 설명 
## 주요 작성 위치 
- parser : 데이터 파싱 로직 작성하는 곳
- crawlingTarget : 크롤링할 사이트나 오픈API 추가하는 곳
- dao : 크롤링 결과를 DB에 저장하기 위한 곳
- persistence : sqlMap작성
- quartz : quartz job 추가하기 

## 시스템 코드
- persistence : mybatis설정 
- jsoup : jsoup 사용하는 부분
- properties : 프로그램 프로퍼티 저장 위치 
- util : 유틸함수