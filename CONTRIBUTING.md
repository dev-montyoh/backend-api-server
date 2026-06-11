# 개발 가이드

[← README로 돌아가기](README.md)

## 사전 요구사항

- Java 19
- IntelliJ IDEA
- Docker (선택) 또는 MySQL 8.0 로컬 설치

## 로컬 환경 구성

### 공통

1. Git Clone
2. 각 서비스의 `build.gradle` 우클릭 → **Link Gradle Project** → Gradle 빌드 확인

### Docker 사용 시

1. Docker 설치
2. Gradle에서 `:build setup:buildLocalSetup` 실행
   - 또는 `src/main/resources/db/docker-compose.yaml` 직접 실행
3. Docker에서 DB 컨테이너 확인
4. DB 접속 확인 — `application.yaml` local 프로필 DB 정보 참고
5. Application 실행

### 로컬 DB 사용 시

1. MySQL 8.0 설치
2. 다음 SQL 파일 실행
   - `src/main/resources/db/database/schema_initialisation.sql`
3. DB 접속 확인 — `application.yaml` local 프로필 DB 정보 참고
4. Application 실행

---

## 브랜치 전략

```
origin
  ├── master
  ├── develop
  └── features/
        ├── branch1
        └── branch2
```

1. `develop` 기준으로 `features/branch-name` 브랜치 생성
2. 개발 완료 후 `develop`으로 Pull Request 생성
3. GitHub Actions — 각 모듈 빌드 성공 확인
4. `develop` → `master` Pull Request 생성 및 Merge
5. GitHub Actions — 빌드 성공 및 Docker Image 생성 확인

---

## 커밋 메시지 규칙

```
feat: 새로운 기능 추가
fix: 버그 수정
refactor: 코드 리팩토링
chore: 빌드, 설정 변경
docs: 문서 수정
```
