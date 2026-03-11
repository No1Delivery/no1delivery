# 🚀 No.1 Delivery

No.1 Delivery는 대규모 트래픽 환경에서도 안정적인 서비스를 제공하기 위해
DDD(Domain-Driven Design) 아키텍처를 채택한 배달 플랫폼 백엔드 시스템입니다.

## 📌 프로젝트 설명

본 프로젝트는 고객, 가게 사장, 관리자가 상호작용하는 배달 주문 관리 시스템을 구축합니다.

객체지향 설계와 도메인 모델 중심 개발(Domain Driven Design)을 통해
복잡한 배달 비즈니스 로직을 명확하게 분리하고 확장성 있는 시스템을 지향합니다.

또한 Spring AI 기반 메뉴 설명 자동 생성 기능을 통해
가게 사장님이 메뉴 설명을 쉽게 작성할 수 있도록 지원합니다.

## 👥 팀원 역할 분담

| [김도현](https://github.com/kimdh32022) | [김관형](https://github.com/kwanhyoungkim) | [조여진](https://github.com/guineacodes) | [조하은](https://github.com/haeun228) | [한소연](https://github.com/soyeonnan) | [김두리](https://github.com/DDoori) |
|:------------------------------------:|:---------------------------------------:|:-------------------------------------:|:----------------------------------:|:-----------------------------------:|:--------------------------------:|
|     결제(Payment), <br/>배포(CI/CD)      |             카테고리(Category)              |               주문(Order)               |       가게(Store), <br/>AI 연동        |             리뷰(Review)              |       회원(User), <br/>인증/인가       |

## 🛠 Technology Stack

### 🚀 Backend & Core
| Category | Technology | Description |
| :--- | :--- | :--- |
| **Language** | `Java 17` | 고가용성 백엔드 개발 언어 |
| **Framework** | `Spring Boot 3.x` | 엔터프라이즈급 애플리케이션 프레임워크 |
| **Security** | `Spring Security` | 역할 기반(RBAC) 인증 및 권한 관리 |
| **ORM** | `Spring Data JPA` | 객체 지향적 데이터 영속화 관리 |
| **Query** | `QueryDSL` | Type-Safe한 동적 쿼리 및 복잡한 연산 처리 |

### 🗄 Database & Infrastructure
| Category | Technology | Description |
| :--- | :--- | :--- |
| **Main DB** | `PostgreSQL 15` | 신뢰성 높은 오픈소스 관계형 데이터베이스 |
| **Spatial** | `PostGIS` | 공간 데이터 확장 (위치 기반 서비스 확장성 고려) |
| **AI** | `Spring AI` | OpenAI 연동을 통한 지능형 서비스 구현 |
| **Docs** | `Swagger (Springdoc)` | API 명세 자동화 및 테스트 도구 제공 |



---

## 📦 Gradle Dependencies

이 프로젝트는 **Gradle**을 통해 의존성을 관리하며, 기능별로 체계적으로 분리하여 구성되었습니다.

### 🌱 Spring & Web
| Dependency | Description |
| :--- | :--- |
| `Spring Boot Starter Web` | MVC 아키텍처 및 RESTful API 구현 |
| `Spring Boot Starter Validation` | 데이터 무결성 보장을 위한 유효성 검증 |
| `Spring Boot Starter Security` | 인증/인가 필터 및 보안 설정 |

### 💾 Data & Query
| Dependency | Description |
| :--- | :--- |
| `PostgreSQL` / `Data JPA` | 메인 DB 드라이버 및 JPA 영속성 계층 |
| `Hibernate Spatial` / `JTS` | 공간 데이터 처리를 위한 Topology Suite |
| `QueryDSL` | 복잡한 조건 검색 및 동적 쿼리 최적화 |

### 🔐 Auth & AI & Docs
| Dependency | Description |
| :--- | :--- |
| `JWT (jjwt)` | 무상태(Stateless) 인증을 위한 토큰 라이브러리 |
| `Spring AI - OpenAI` | LLM 기반 상품 설명 생성 자동화 |
| `Springdoc OpenAPI` | Swagger UI 연동 라이브러리 |

### 🧪 Test Environment
| Dependency | Description |
| :--- | :--- |
| `JUnit5` | 단위 및 통합 테스트 프레임워크 |
| `Spring Security Test` | 보안 컨텍스트 테스트 지원 |
| `H2 Database` | 테스트용 In-Memory DB 환경 제공 |

## 🏗 DDD 기반 프로젝트 구조

이 프로젝트는 **계층형 아키텍처**(**Layered Architecture**)와 **Domain Driven Design**(**DDD**)을 결합하여 도메인 중심 구조로 설계되었습니다.

주요 도메인은 총 7가지로 나누어져 있으며, 각 도메인은 특정 비즈니스 영역을 책임집니다:  
**AI, Category, Store, Order, Payment, Review, User**

### 📂 Project Structure
```src/main/java/com/sparta/no1delivery/
├── domain
│   ├── ai
│   │   ├── application        # AiService
│   │   ├── domain             # AiLog, AiLogRepository, AiLogType, AiClient
│   │   ├── infrastructure     # AiLogAdvisor, OpenAiClient
│   │   └── presentation       # AiController, AiRequest, AiResponse DTOs
│   │
│   ├── category
│   │   ├── application        # CategoryService, CategoryServiceDto
│   │   ├── domain             # Category, CategoryId, CategoryRepository
│   │   └── presentation       # CategoryController
│   │
│   ├── store
│   │   ├── application        # StoreService, MenuService, QueryService
│   │   ├── domain             # Store, Menu, StoreRepository
│   │   ├── infrastructure     # StoreQueryRepositoryImpl
│   │   └── presentation       # StoreController, MenuController
│   │
│   ├── order
│   │   ├── application        # OrderService
│   │   ├── domain             # Order, OrderItem, DeliveryInfo
│   │   ├── infrastructure     # OrderQueryRepositoryImpl
│   │   └── presentation       # OrderController
│   │
│   ├── payment
│   │   ├── application        # PaymentService
│   │   ├── domain             # Payment, PaymentRepository
│   │   ├── infrastructure     # TossPaymentClient
│   │   └── presentation       # PaymentController
│   │
│   ├── review
│   │   ├── application        # ReviewService
│   │   ├── domain             # Review, ReviewRepository
│   │   ├── infrastructure     # ReviewQueryRepositoryImpl
│   │   └── presentation       # ReviewController
│   │
│   └── user
│       ├── application        # UserService, TokenService
│       ├── domain             # User, UserRepository
│       ├── infrastructure     # JwtProvider
│       └── presentation       # UserController
│
└── global
    ├── domain                # AddressToCoords, OwnerCheck, BaseEntity, RoleCheck
    ├── infrastructure        # Kakao API, JwtFilter, SecurityConfig, EventConfig
    └── presentation          # GlobalExceptionHandler
```

### 🗂 데이터베이스 설계 / ERD
- 데이터베이스 설계는 7가지 도메인 기준으로 이루어짐
- 도메인 간 의존 관계를 제거하여 독립적인 테이블 구조 유지
  <img width="1976" height="912" alt="delivery (2)" src="https://github.com/user-attachments/assets/5e0445d8-78ce-4a15-afc4-ee5e72ec570f" />



## ⚙️ 주요 기능

도메인 주도 설계(DDD)를 바탕으로 각 도메인별 핵심 비즈니스 로직을 다음과 같이 구현하였습니다.

### 👤 User (회원)
| 기능 | 설명 |
| :--- | :--- |
| 사용자 관리 | 회원가입, 정보 수정, 탈퇴 등 |
| 인증/인가 | JWT 기반 로그인, 역할(Role) 관리 |
| 배송지 관리 | 사용자 주소 등록/수정/삭제 |

### 🏪 Store (가게)
| 기능 | 설명 |
| :--- | :--- |
| 가게 관리 | 가게 등록, 정보 수정, 운영 상태 관리 |
| 메뉴 관리 | 메뉴 등록/수정/삭제, 품절 처리 기능 |

### 📦 Order (주문)
| 기능 | 설명 |
| :--- | :--- |
| 주문 처리 | 주문 생성, 상태 변경(접수/배달중/완료) |
| 주문 취소 | 생성 후 5분 이내 주문 취소 가능 |

### 💳 Payment (결제)
| 기능 | 설명 |
| :--- | :--- |
| 결제 처리 | 결제 내역 저장 및 주문 데이터와 연동 |
| Toss 연동 | Toss 결제 시스템과 연계 |

### ⭐ Review (리뷰)
| 기능 | 설명 |
| :--- | :--- |
| 리뷰 작성 | 주문 기반 리뷰 작성 및 평점 관리 |
| 가게 평점 | 리뷰 평점을 실시간 가게 평균 별점에 반영 |

### 🗂 Category (카테고리)
| 기능 | 설명 |
| :--- | :--- |
| 음식 카테고리 관리 | 카테고리 생성, 수정, 삭제 및 관리 |
| 권한 기반 접근 | MANAGER/MASTER 권한별 카테고리 관리 |

### 🤖 AI
| 기능 | 설명 |
| :--- | :--- |
| 메뉴 이름 & 설명 생성 | Spring AI(OpenAI)를 활용하여 메뉴 이름과 메뉴 설명 자동 생성 |
| 가게 설명 생성 | AI를 통해 가게 소개/설명 자동 생성 |
| AI 호출 로그 관리 | AI 요청 이력 및 응답 데이터 로그 저장 |

## ⚙️ 설치 및 실행 방법
### 1️⃣ 레포지토리 복제

    git clone https://github.com/No1Delivery/no1delivery.git

### 2️⃣ 환경 변수 설정

프로젝트 루트 경로에 `.env` 파일 생성
    
```
# PostgreSQL 설정
DB_URL=jdbc:postgresql://localhost:5432/<DB_NAME>
DB_NAME=<DB_NAME>
DB_USERNAME=<DB_USERNAME>
DB_PASSWORD=<DB_PASSWORD>

# 카카오 API
KAKAO_API_KEY=<YOUR_KAKAO_API_KEY>

# JWT 시크릿
JSON_WEB_TOKEN_SECRET=<YOUR_JWT_SECRET>

# OpenAI API
OPENAI_API_KEY=<YOUR_OPENAI_API_KEY>

# Toss 결제 시크릿 키
TOSS_SECRET_KEY=<YOUR_TOSS_SECRET_KEY>
```
- `< >` 부분을 본인의 값으로 교체해야 함

### 3️⃣ DB 구축

1. `docker-compose`로 PostgreSQL + PostGIS + pgRouting 컨테이너 실행
    - 프로젝트 루트 경로에서 명령어 실행
        ```
        docker compose -f ./docker/pgrouting/docker-compose.yml --env-file .env up -d
        ```
    - ⚠️ 이미 포트 `5432`에서 PostgreSQL이 실행 중이라면, 중지하거나 포트 변경 후 실행

2. DB 접속 후 `PostGIS` & `pgrouting` 익스텐션 활성화
    ```
    CREATE EXTENSION IF NOT EXISTS postgis;
    CREATE EXTENSION IF NOT EXISTS pgrouting;
    ```
3. Spring Boot 서버 실행해 테이블 생성
    ```
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
4. `GIST` 인덱스 추가 (공간 검색 최적화)
    ```
    DROP INDEX IF EXISTS idx_store_address_point;
    CREATE INDEX idx_store_address_point ON P_STORE USING GIST (point);
   ```

## 📄 API 명세서

- Swagger UI를 통해 모든 API 엔드포인트를 확인하고 테스트할 수 있습니다.
- URL: [http://localhost:3000/swagger-ui/index.html](http://localhost:3000/swagger-ui/index.html)

> ⚠️ 서버 실행 후 Swagger UI 접속 가능
