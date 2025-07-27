# Simple Login API

간단한 로그인 및 사용자 관리 기능을 제공하는 Spring Boot 기반 API 프로젝트입니다.

## 기술 스택

*   Java 17
*   Spring Boot 3.x
*   Spring Security (JWT)
*   H2 Database (인메모리)
*   Gradle
*   Swagger/OpenAPI 3

## 프로젝트 실행 방법

이 프로젝트는 GitHub Actions를 사용하여 AWS EC2 인스턴스에 자동 배포되도록 설정되어 있습니다.

**Public Ipv4:** `13.125.174.54`

**Public DNS**`ec2-13-125-174-54.ap-northeast-2.compute.amazonaws.com`

## API 엔드포인트

애플리케이션이 실행되면 다음 엔드포인트를 사용할 수 있습니다.

**Swagger UI:** `http://localhost:8080/swagger-ui.html` (로컬 환경)

### 인증 (Authentication)

*   **회원가입 (Signup)**
    *   `POST /signup`
    *   **Request Body:**
        ```json
        {
            "username": "string",
            "password": "string",
            "nickname": "string"
        }
        ```
    *   **Response:** `SignupResponseDto`

*   **로그인 (Login)**
    *   `POST /login`
    *   **Request Body:**
        ```json
        {
            "username": "string",
            "password": "string"
        }
        ```
    *   **Response:** `LoginResponseDto` (JWT 토큰 포함)

### 관리자 기능 (Admin Functionality)

*   **권한 부여 (Assign Role)**
    *   `PATCH /admin/users/{userId}/roles`
    *   **Headers:** `Authorization: Bearer <JWT_TOKEN>` (관리자 권한 필요)
    *   **Path Variable:** `userId` (권한을 부여할 사용자의 ID)
    *   **Request Body:**
        ```json
        {
            "userRole": "ADMIN" | "USER"
        }
        ```
    *   **Response:** `AssignUserRoleResponseDto`

## 초기 관리자 계정

애플리케이션이 처음 실행될 때 (H2 인메모리 DB가 초기화될 때마다), 다음 관리자 계정이 자동으로 생성됩니다.

*   **Username:** `admin`
*   **Password:** `Admin12#$`

**주의:** 이 비밀번호는 개발 및 테스트 목적으로만 사용해야 합니다. 실제 운영 환경에서는 더 강력하고 안전한 비밀번호를 사용하거나, 환경 변수 등을 통해 관리하는 것이 좋습니다.

## 기타

*   H2 Database는 인메모리 모드로 동작하므로, 애플리케이션 재시작 시 모든 데이터가 초기화됩니다.
*   API 문서화는 Swagger/OpenAPI 3를 사용합니다.
