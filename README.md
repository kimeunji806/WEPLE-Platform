# WEPLE : 함께 만드는 연결된 협업 플랫폼
 
WEPLE은 프로젝트 수행 과정에서 발생하는 일정, 일감, 산출물, 커뮤니케이션, 저장소 이력을 **하나의 흐름으로 관리할 수 있는 웹 기반 협업 플랫폼**입니다.
 
팀 단위 프로젝트를 진행하면서 구성원, 역할, 권한, 일감, 파일, 위키, 알림, 저장소 정보를 통합적으로 관리하는 것을 목표로 개발되었습니다.
 
본 프로젝트는 여러 협업 도구를 오가며 발생하는 비효율을 해소하고, 실무자 관점에서의 기능 설계를 적용하여 프로젝트 관리 업무를 보다 체계적이고 효율적으로 처리할 수 있도록 개발되었습니다.

---

## 팀 구성 및 역할

<table align="center">
  <tr>
    <td align="center">
      <a href="https://github.com/time1014">
        <img src="https://avatars.githubusercontent.com/u/64236748?v=4" width="100px;" alt="방진영"/><br />
        <sub><b>방진영</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/crescentia0011">
        <img src="https://avatars.githubusercontent.com/u/254889839?v=4" width="100px;" alt="김병완"/><br />
        <sub><b>김병완</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kimeunji806">
        <img src="https://avatars.githubusercontent.com/u/258710580?v=4" width="100px;" alt="김은지"/><br />
        <sub><b>김은지</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kimminji28">
        <img src="https://github.com/kimminji28.png" width="100px;" alt="김민지"/><br />
        <sub><b>김민지</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/smk412">
        <img src="https://github.com/smk412.png" width="100px;" alt="송민규"/><br />
        <sub><b>송민규</b></sub>
      </a>
    </td>
  </tr>
  <tr>
    <th align="center">팀장</th>
    <th align="center">부팀장</th>
    <th align="center">팀원</th>
    <th align="center">팀원</th>
    <th align="center">팀원</th>
  </tr>
  <tr>
    <td align="center">프로젝트 총괄</td>
    <td align="center">배포</td>
    <td align="center">Git</td>
    <td align="center">개발환경</td>
    <td align="center">DB</td>
  </tr>
  <tr>
    <td align="center">
      일감 관리<br />
      테스트 케이스<br />
      캘린더
    </td>
    <td align="center">
      로그인/인증<br />
      사용자 관리<br />
      저장소 관리
    </td>
    <td align="center">
      프로젝트, 작업내역<br />
      위키, 알림<br />
      마이페이지, 칸반보드
    </td>
    <td align="center">
      그룹, 코드값<br />
      소요시간<br />
      파일관리
    </td>
    <td align="center">
      대시보드<br />
      간트차트<br />
      보조 기능
    </td>
  </tr>
</table>

---

## 프로젝트 미리보기

### 메인 화면
<img width="1920" height="945" alt="메인화면" src="https://github.com/user-attachments/assets/2ab4bf2b-ef1f-4a08-aa0d-4b9ba5b9a59d" />

### 시스템 구성도
![시스템구성도](./docs/system_flow.png)

### 자료 흐름도
<img width="2720" height="2000" alt="weple_dfd_v2_white_bg" src="https://github.com/user-attachments/assets/3230e7e4-c6f0-4313-8028-587dd1ee870a" />

### ERD
<img width="4780" height="3042" alt="3 논리 ERD 캡처" src="https://github.com/user-attachments/assets/631fc03f-8743-4c79-b42e-71bba9af3556" />

---

## 프로젝트 개요

- **프로젝트명**: WEPLE
- **프로젝트 유형**: 팀 프로젝트 (5인)
- **개발 기간**: 2026.06.08 ~ 2026.07.14 (약 5주)
- **개발 목적**
  - 프로젝트·일정·산출물 관리 업무 통합 및 전산화
  - 역할 및 권한 기반 접근 제어 구현
  - 일감/저장소/위키 등 협업 도구 분산 문제 해소
  - GitHub 연동을 통한 커밋-일감 추적 체계 구축

---

## 주요 기능

### 관리
- 회사별 사용자 가입승인
- 사용자 등록 및 계정 상태 관리
- 그룹 관리
- 역할 및 권한 관리
- 코드값 및 서비스 설정 관리

### 프로젝트
- 프로젝트 생성 및 관리
- 프로젝트 구성원 등록 및 관리
- 프로젝트별 모듈 설정
- 버전 및 마일스톤 관리
- 로드맵 관리

### 일감 / 테스트
- 일감 등록, 조회, 수정, 삭제
- 일감 댓글 및 작업내역 관리
- 일감 유형, 우선순위, 상태 관리
- 테스트 케이스 관리
- 요구사항 커버리지 확인

### 일정 / 현황
- 통합 캘린더 및 프로젝트 캘린더
- 작업 시간 등록 및 소요시간 관리
- 간트차트
- 칸반보드
- 프로젝트 진행 현황 확인

### 협업 / 산출물
- 위키 등록 및 관리
- 게시판
- 파일 업로드 및 다운로드
- 다운로드 이력 조회
- 알림 및 마이페이지

### 저장소
- GitHub 저장소 등록 및 관리
- 저장소 파일트리 및 파일 내용 조회
- 커밋 내역 조회
- 커밋 상세 및 변경 내역 확인
- 커밋 메시지 기반 일감 연결

---

## 업무 흐름

1. 관리자가 프로젝트를 생성하고 구성원과 역할·권한을 설정합니다.
2. 구성원이 일감을 등록하고 담당자를 지정합니다.
3. 담당자가 일감을 진행하며 소요시간과 작업내역을 기록합니다.
4. 진행 상황은 칸반보드와 간트차트로 실시간 공유됩니다.
5. 관련 문서와 지식은 위키로 등록·관리됩니다.
6. GitHub 커밋 메시지를 통해 일감과 저장소 이력이 자동으로 연결됩니다.
7. 각 단계의 변경사항은 알림을 통해 관련 구성원에게 실시간으로 전달됩니다.

---

## 기술 스택

### Backend
- Java 21
- Spring Boot 3.5.16
- Spring MVC
- Spring Security
- MyBatis
- Oracle Database
- Jasypt

### Frontend
- Thymeleaf
- JavaScript
- HTML5
- CSS3

### External / Infra
- GitHub REST API
- AWS S3
- AWS EC2
- Docker
- Jenkins
- GitHub Actions

### Dev Tools
- IntelliJ IDEA
- Oracle SQL Developer

### Collaboration
- Git
- GitHub

---

## 기술 스택 시각화

| 구분 | 사용 기술 |
|------|----------|
| Frontend | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white) |
| Backend | ![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) |
| Data & ORM | ![Oracle](https://img.shields.io/badge/Oracle-F00000?style=for-the-badge&logo=oracle&logoColor=white) ![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge) |
| Cloud & Storage | ![AWS S3](https://img.shields.io/badge/Amazon_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white) ![AWS EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) |
| Dev Tools | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) |
| Collaboration | ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) |
| Deployment | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) |

---

## 프로젝트 구조

```bash
WEPLE/
├── src/main/java/com/weple/cloud
│   ├── auth          # 인증 관련 기능
│   ├── system         # 시스템 및 관리자 기능
│   ├── project        # 프로젝트 관련 기능
│   ├── repository     # 저장소 연동 기능
│   └── ...            # 일감, 파일, 위키, 알림 등 협업 기능
│
├── src/main/resources
│   ├── mapper          # MyBatis Mapper XML
│   ├── static           # CSS, JavaScript, 이미지 등 정적 리소스
│   └── templates        # Thymeleaf 화면 템플릿
│
├── docs/
│   ├── main.png
│   ├── system_flow.png
│   ├── data_flow.png
│   └── erd.png
│
└── README.md
```
