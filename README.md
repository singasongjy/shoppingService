# Shopping Service Backend

## 구현 범위
### 사용자 API : CustomerController.java
1. **카테고리별 최저가**
   - 카테고리별 최저 가격인 브랜드, 가격과 총액을 제공
   - 카테고리의 순서를 유지하여 반환
   - 예시:
      - **API 경로**: `GET /customer/category/lowest-price`


2. **가장 저렴한 브랜드 카테고리**
   - 전체 상품 카테고리 상품 중 단일 브랜드로 최저 가격인 브랜드와 총액
   - 가장 저렴한 상품을 조회하는 목적이므로 브랜드에 상품이 2개이상이라면, 가장 저렴한 가격의 상품으로 선택
   - 전체 상품 카테고리 상품을 구매하는 목적이므로 모든 카테고리에 대해 상품을 가지고 있어야 반환
   - 카테고리의 순서를 유지하여 반환
   - 예시:
      - **API 경로**: `GET /customer/brand/lowest-price`


3. **카테고리 최저가, 최고가 브랜드**
   - 특정 카테고리에서 최저 가격과 최고 가격 브랜드를 조회
   - 예시:
      - **API 경로**: `GET /customer/category/lowest-highest`
     

### 운영자 API : AdminController.java
1. **브랜드 관리 (등록, 수정, 삭제)**
   - 브랜드 추가, 수정, 삭제 기능 제공
   - 예시:
      - **API 경로**:
         - `POST /admin/brand`: 신규 브랜드 등록
         - `PUT /admin/brand/{brandId}`: 브랜드 수정
         - `DELETE /admin/brand/{brandId}`: 브랜드 삭제


2. **상품 관리 (등록, 수정, 삭제)**
   - 상품은 기존 브랜드를 사용하거나, 신규 브랜드를 먼저 생성한 후 등록
   - 예시:
      - **API 경로**:
         - `POST /admin/product`: 신규 상품 등록
         - `PUT /admin/product/{productId}`: 상품 수정
         - `DELETE /admin/product/{productId}`: 상품 삭제
   

3. **조회 관리 (브랜드, 카테고리, 상품)**
   - **GET /admin/categories**: 모든 카테고리 조회
   - **GET /admin/brands**: 모든 브랜드 조회
   - **GET /admin/products**: 모든 상품 조회

## 기술 스택
- **Java 17**: 최신 LTS 버전으로 안정성 및 성능을 제공합니다.
- **Spring Boot 3.1.4**: 애플리케이션 개발을 위한 기본 프레임워크
- **Spring Data JPA**: 데이터베이스 연동을 위한 ORM
- **H2 Database**: 개발 및 테스트 용도로 사용되는 인메모리 데이터베이스
- **JUnit 5, Mockito**: 단위 테스트 및 Mock 객체를 사용한 테스트

## 코드 빌드, 실행

### 1. **빌드 및 실행**
```shell
./gradlew clean build
./gradlew bootRun
```

### 2. **프론트 웹**
- **주소**: `http://localhost:8080`에서 사용자와 관리자 대시보드에 접근 가능합니다.

## 테스트 방법

### 1. **단위 테스트 실행**
```shell
./gradlew test
```
테스트 결과는 `build/reports/tests/test/index.html`에서 확인할 수 있습니다.

## API 문서
Swagger UI를 통해 API 문서화가 제공됩니다. 아래 링크에서 API를 확인하고 요청을 보낼 수 있습니다.
- **API 문서 경로**: `http://localhost:8080/swagger-ui/index.html`

## DB
- **초기 데이터**: `data.sql`
   - 기본적인 샘플 데이터를 제공하며, MySQL과 H2 데이터베이스에서 실행 가능합니다.
   - 데이터베이스 구조를 초기화할 때 `data.sql` 파일을 사용하여 기본 데이터가 삽입됩니다.
