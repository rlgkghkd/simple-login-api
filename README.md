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

### 로컬에서 실행

1.  **프로젝트 클론:**
    ```bash
    git clone <YOUR_REPOSITORY_URL>
    cd SimpleLoginAPI
    ```

2.  **Gradle 빌드:**
    ```bash
    ./gradlew bootJar
    ```

3.  **애플리케이션 실행:**
    ```bash
    java -jar build/libs/SimpleLoginAPI-0.0.1-SNAPSHOT.jar # 실제 JAR 파일 이름으로 변경
    ```

    애플리케이션은 기본적으로 `8080` 포트에서 실행됩니다.

### AWS EC2에 배포 (CI/CD)

이 프로젝트는 GitHub Actions를 사용하여 AWS EC2 인스턴스에 자동 배포되도록 설정되어 있습니다.

**필수 설정:**

1.  **AWS EC2 인스턴스 준비:**
    *   Java 17이 설치되어 있어야 합니다.
    *   인바운드 규칙에 `SSH (Port 22)`와 `Custom TCP (Port 8080)`가 허용되어 있어야 합니다.

2.  **GitHub Repository Secrets 설정:**
    GitHub 리포지토리의 `Settings` -> `Secrets and variables` -> `Actions`에서 다음 Secret을 추가해야 합니다.
    *   `SSH_PRIVATE_KEY`: EC2 인스턴스 접속에 사용되는 `.pem` 파일의 전체 내용 (`-----BEGIN...` 부터 `-----END...` 까지).
    *   `EC2_HOST`: EC2 인스턴스의 퍼블릭 IP 주소 또는 퍼블릭 DNS.
    *   `EC2_USER`: EC2 인스턴스에 접속할 사용자 이름 (예: `ec2-user`, `ubuntu`).

3.  **CI/CD 워크플로우:**
    `.github/workflows/cicd.yml` 파일에 정의된 워크플로우는 `dev` 브랜치에 푸시될 때마다 자동으로 빌드 및 배포를 수행합니다.

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
