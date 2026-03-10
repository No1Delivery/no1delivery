# 🚀 No.1 Delivery

No.1 Delivery는 대규모 트래픽 환경에서도 안정적인 서비스를 제공하기 위해
DDD(Domain-Driven Design) 아키텍처를 채택한 배달 플랫폼 백엔드 시스템입니다.

## 📌 프로젝트 설명

본 프로젝트는 고객, 가게 사장, 관리자가 상호작용하는 배달 주문 관리 시스템을 구축합니다.

객체지향 설계와 **도메인 모델 중심 개발(Domain Driven Design)**을 통해
복잡한 배달 비즈니스 로직을 명확하게 분리하고 확장성 있는 시스템을 지향합니다.

또한 Spring AI 기반 메뉴 설명 자동 생성 기능을 통해
가게 사장님이 메뉴 설명을 쉽게 작성할 수 있도록 지원합니다.

## ⚙️ Key Features

도메인 주도 설계(DDD)를 바탕으로 각 도메인별 핵심 비즈니스 로직을 다음과 같이 구현하였습니다.

### 👤 User & Auth (회원 및 인증)
| Category | Feature | Description |
| :--- | :--- | :--- |
| **Auth** | `JWT Authentication` | Spring Security와 JWT를 활용한 토큰 기반 인증 및 인가 |
| **RBAC** | `Role Management` | **CUSTOMER, OWNER, MANAGER, MASTER** 권한별 접근 제어 |
| **Profile** | `Address Management` | 사용자 배송 주소지 등록, 수정 및 관리 |

### 🏪 Store & Menu (가게 및 메뉴)
| Category | Feature | Description |
| :--- | :--- | :--- |
| **Store** | `Store Management` | 가게 등록, 정보 수정 및 운영 상태 관리 |
| **Category** | `Food Categories` | 음식 카테고리 관리 (MANAGER/MASTER 권한) |
| **Menu** | `Menu CRUD` | 메뉴 등록/수정/삭제 및 품절 대비 숨김 처리 기능 |
| **AI** | `AI Description` | **Spring AI(OpenAI)**를 활용한 메뉴 설명 자동 생성 기능 |

### 📦 Order & Payment (주문 및 결제)
| Category | Feature | Description |
| :--- | :--- | :--- |
| **Order** | `Order Flow` | 주문 생성, 상태 변경(접수/배달중/완료) 프로세스 관리 |
| **Policy** | `Order Cancellation` | 데이터 정합성을 고려하여 주문 생성 후 **5분 이내** 취소 가능 |
| **Payment** | `Payment Integration` | 결제 내역 저장 및 주문 데이터와의 연동 처리 |

### ⭐ Review & AI (리뷰 및 부가 기능)
| Category | Feature | Description |
| :--- | :--- | :--- |
| **Review** | `Review System` | 주문 기반 리뷰 작성 및 평점 관리 |
| **Rating** | `Store Rating` | 리뷰 평점을 실시간으로 가게 평균 별점에 반영 |
| **Logging** | `AI Request Logs` | AI 서비스 호출 이력 및 응답 데이터 로그 저장 |

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

> **API Documentation URL:** `/swagger-ui/index.html`

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

### 🏗 DDD 기반 프로젝트 구조

이 프로젝트는 **계층형 아키텍처(Layered Architecture)**와
Domain Driven Design을 결합하여 도메인 중심 구조로 설계되었습니다.

### 📂 Project Structure
```src/main/java/com/sparta/no1delivery/
├── domain
│   ├── ai
│   │   ├── application        # AiService
│   │   ├── domain             # AiLog, AiLogRepository, AiLogType, AiClient
│   │   ├── infrastructure     # AiLogAdvisor, OpenAiClient
│   │   └── presentation       # AiController, AiRequest, AiResponse DTOs
│
│   ├── category
│   │   ├── application        # CategoryService, CategoryServiceDto
│   │   ├── domain             # Category, CategoryId, CategoryRepository
│   │   └── presentation       # CategoryController
│
│   ├── store
│   │   ├── application        # StoreService, MenuService, QueryService
│   │   ├── domain             # Store, Menu, Owner, StoreRepository
│   │   ├── infrastructure     # StoreQueryRepositoryImpl
│   │   └── presentation       # StoreController, MenuController
│
│   ├── order
│   │   ├── application        # OrderService
│   │   ├── domain             # Order, OrderItem, DeliveryInfo
│   │   ├── infrastructure     # OrderQueryRepositoryImpl
│   │   └── presentation       # OrderController
│
│   ├── payment
│   │   ├── application        # PaymentService
│   │   ├── domain             # Payment, PaymentRepository
│   │   ├── infrastructure     # TossPaymentClient
│   │   └── presentation       # PaymentController
│
│   ├── review
│   │   ├── application        # ReviewService
│   │   ├── domain             # Review, ReviewRepository
│   │   ├── infrastructure     # ReviewQueryRepositoryImpl
│   │   └── presentation       # ReviewController
│
│   └── user
│       ├── application        # UserService, TokenService
│       ├── domain             # User, UserRepository
│       ├── infrastructure     # JwtProvider
│       └── presentation       # UserController
│
└── global
├── domain
│   ├── service            # AddressToCoords, OwnerCheck
│   └── common             # BaseEntity, RoleCheck
│
├── infrastructure
│   ├── api                # KakaoAddressToCoords
│   ├── security           # JwtFilter, SecurityConfig
│   └── event              # EventConfig
│
└── presentation
└── exception          # GlobalExceptionHandler
```
## 🧠 설계 특징
### 🧩 Aggregate Root 중심 설계

각 도메인의 중심 객체(Order, Store 등)를 통해서만 하위 엔티티에 접근하여
데이터 일관성을 유지합니다.

### 🏛 Rich Domain Model

엔티티 내부에서 비즈니스 로직을 수행하는
**풍부한 도메인 모델(Rich Domain Model)**을 지향합니다.

### 🔔 도메인 이벤트 활용

주문 완료 후 결제 처리 등은
도메인 이벤트 기반으로 처리하여 도메인 간 결합도를 낮추었습니다.

### 🔌 인프라 추상화

Infrastructure 계층을 통해 외부 시스템(DB, API)을 분리하여
도메인 로직이 인프라에 의존하지 않도록 설계했습니다.

## ⚡ 설치 및 실행 방법
1️⃣ 저장소 복제
git clone https://github.com/No1Delivery/no1delivery.git

2️⃣ 환경 변수 설정
src/main/resources/application.yml

DB 및 외부 API 설정을 추가합니다.

3️⃣ 애플리케이션 실행
./gradlew bootRun