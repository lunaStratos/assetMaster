## 1. 프로젝트 개요  
  
- **서비스명:** 에셋마스터 (AssetMaster)  
      
- **목적:** 다수의 건물과 호실을 효율적으로 관리하고, 세입자 정보 및 임대 현황을 한눈에 파악하는 통합 건물 관리 솔루션  
- **주요 타겟:** 중소형 빌딩 임대인 및 부동산 관리업체

## 2. 기본 환경

- Java 21
- Gradle Wrapper 사용
- Node.js 20 이상
- npm

## 3. 실행 방법

1. 프론트엔드와 백엔드를 동시에 실행하려면:
   ```bash
   cd assetMaster-ui
   npm run start:all
   ```

2. 프론트엔드만 실행하려면:
   ```bash
   cd assetMaster-ui
   npm run dev
   ```

3. 백엔드만 실행하려면:
   ```bash
   cd assetMaster-ui
   npm run backend
   ```

## 4. 사용법

- 기본 테스트 계정 (앱 최초 실행 시 자동 생성):
  - 아이디: `admin`
  - 비밀번호: `admin1234`
- 회원가입 페이지에서 추가 계정을 생성할 수 있습니다.
  - 회원가입 필수 항목: 아이디, 비밀번호, 이름, 전화번호
- DB 접속 정보:
  - ID: `root`
  - 비밀번호: `root`

## 5. 참고

- `assetMaster-ui/package.json`에 `start:all` 스크립트가 추가되어 있습니다.
- Windows 환경에서 실행할 때 `gradlew.bat`를 사용합니다.
