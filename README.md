<br/>

<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring" width="72" />

# backend-api-server

[![Push Workflow](https://github.com/dev-montyoh/backend-api-server/actions/workflows/push-master.yaml.yml/badge.svg)](https://github.com/dev-montyoh/backend-api-server/actions/workflows/push-master.yaml.yml)

**개인 프로젝트에 사용되는 멀티모듈 백엔드 API 서버**

---

Spring Boot 기반의 멀티모듈 구조로, Gateway를 통해 각 서비스로 라우팅합니다. JWT 인증, 결제, 콘텐츠, 유저 관리 등 도메인별로 모듈을 분리하여 독립적으로 개발·배포할 수 있도록 설계했습니다.

---

## 사용 기술

- Java 19, Spring Boot 3.2
- Spring Cloud Gateway, Spring Security, Spring Data JPA
- MySQL 8.0, Redis
- Docker, GitHub Actions
- Flyway, MapStruct, OpenFeign, Swagger (springdoc)

---

## 주요 특징

- **멀티모듈** — 도메인별 독립 모듈로 관심사 분리
- **API Gateway** — Spring Cloud Gateway + JWT 인증/인가 중앙 처리
- **CI/CD 자동화** — PR → Build 검증, master push → Docker Image 빌드·배포
- **테스트 커버리지** — JaCoCo 라인 커버리지 80% 이상 강제

---

## 모듈 구성

| 모듈 | 역할 |
|---|---|
| `gateway` | 라우팅, JWT 인증·인가 |
| `auth` | 로그인, 토큰 발급·갱신 (MySQL + Redis) |
| `user` | 사용자 관리 |
| `content` | 콘텐츠 관리 |
| `payment` | 결제 처리 |

---

## CI/CD

GitHub Actions로 두 단계 파이프라인을 구성합니다.

- **PR → develop**: 각 모듈 빌드 및 테스트 검증
- **push → master**: 빌드 + Docker Image 생성 및 배포

---

## 문서

- **[개발 가이드 →](CONTRIBUTING.md)** — 로컬 환경 구성 · 브랜치 전략 · 커밋 규칙
