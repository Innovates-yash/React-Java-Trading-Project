# Treading-Plateform

Binance-like cryptocurrency trading platform — Spring Boot backend + React frontend (Vite + Redux).  
Backend runs on port `5454`. Frontend is a Vite dev server.

---

## Table of Contents
- About
- Architecture (high level)
- Repo layout (short)
- Requirements
- Environment / secrets
- Quick start (Windows)
  - Backend
  - Frontend
- Common tasks
- Important files & flows
- Security & operational notes
- Contributing
- License

---

## About
This project implements a trading platform with core domains: Authentication, Orders/Matching, Wallets/Balances, Coins/Market Data, Payments, Profile/KYC and Chat. Designed as a production-ready codebase with modular services and clear patterns for microservice extraction.

---

## Architecture (high level)
- Backend: Spring Boot 3.x, Spring Data JPA, MySQL, JJWT for JWT, transactional service layer.
- Frontend: React 18, Vite, Redux (redux-thunk), React Hook Form + Yup/Zod, TailwindCSS, Radix UI primitives.
- Integrations: CoinGecko (market data), Stripe/Razorpay (payments), Gmail SMTP (mail), external chat/AI provider.
- Auth: JWT (24h), stored by frontend in `localStorage` (key `'jwt'`), sent as `Authorization: Bearer <jwt>`.

---

## Repo layout (representative)
- Backend-Spring boot/
  - src/main/java/com/zosh/
    - controller/, service/, model/, repository/, config/, exception/, request/, response/
  - src/main/resources/application.properties
- Frontend-React/
  - src/
    - Api/api.js
    - Redux/ (Store.js, Auth, Coin, Order, Wallet, ...)
    - pages/ (Auth, Profile, Wallet, Home, ...)
    - components/ui/, components/custome/
  - index.html, package.json

---

## Requirements
- Java 19 (or matching configured JDK)
- Maven 3.8+
- Node.js 18+ and npm
- MySQL (create database `treading`)
- (Optional) Docker if you prefer containerized run

---

## Environment / required properties
Update `Backend-Spring boot/src/main/resources/application.properties` (or external env) with:
- spring.datasource.url=jdbc:mysql://localhost:3306/treading
- spring.datasource.username=
- spring.datasource.password=
- spring.mail.username=
- spring.mail.password=    (Gmail app password)
- stripe.api.key=
- razorpay.key=
- coinGecko.api.key (if used)
- gemini.api.key / gemini.secret (if used)
- jwt.secret (used by JwtProvider)

Frontend: adjust base API url in `Frontend-React/src/Api/api.js` if backend host/port differs (default: `http://localhost:5454`).

---

## Quick start (Windows)

1. Start MySQL & create DB:
   - Use MySQL Workbench / CLI to create database `treading`.

2. Backend
   ```powershell
   cd "c:\Users\Yash Gupta\IdeaProjects\Treading-Plateform\Source Code\Backend-Spring boot"
   # edit application.properties with credentials & API keys
   mvn clean install
   mvn spring-boot:run
   ```
   Backend listens on: http://localhost:5454

3. Frontend
   ```powershell
   cd "c:\Users\Yash Gupta\IdeaProjects\Treading-Plateform\Source Code\Frontend-React"
   npm install
   npm run dev
   ```
   Vite dev server will start (open printed URL, typically http://localhost:5173).

---

## Common tasks
- Build backend: `mvn clean package`
- Run backend tests: `mvn test`
- Format & lint frontend: `npm run lint`
- Build frontend for production: `npm run build`

---

## Important files & flows
- Backend
  - JwtProvider.java — creates JWT with `email` + `authorities`, 24h expiration.
  - JwtTokenValidator.java — request filter validating Bearer token.
  - AppConfig.java — Spring Security config.
  - Controllers: `/api/auth`, `/api/orders`, `/api/wallet`, `/api/payment`, etc.
- Frontend
  - src/Api/api.js — Axios instance that auto-injects the JWT from `localStorage.getItem('jwt')`.
  - src/Redux/Store.js — application Redux store (legacy_createStore + thunk).
  - src/pages/Profile/Profile.jsx — example of 2FA flows & dialogs (uses AccountVarificationForm).
  - Key UI components: `components/ui/*` and `components/custome/*`.

---

## Security & operational notes
- JWT stored client-side under key `'jwt'`. Rotate secret and keep short-lived tokens in production; implement refresh tokens or blacklist for revocation.
- Do not commit secrets/API keys. Use environment variables or a secret manager in CI/CD.
- Use Flyway or Liquibase for DB migrations in production (avoid Hibernate ddl-auto = update).
- Use HTTPS in production. Configure CORS and rate limiting for public endpoints.

---

## Testing & CI
- Backend: unit tests with JUnit + Mockito; integration tests with @SpringBootTest (recommended: Testcontainers).
- Frontend: add Jest/React Testing Library for component/unit tests.
- CI suggestion: run maven build & tests, install frontend deps, run lint, run frontend build, then dockerize artifacts.

---

## Contributing
- Fork → branch named `feature/<short-desc>` → PR with description & tests.
- Keep controller changes backwards-compatible and add API versioning (e.g., `/api/v1/...`) for breaking changes.

---

## License
Add a LICENSE file as appropriate (e.g., MIT). This repo currently has no license inlined here.

---

If you want, I can:
- Generate a GitHub Actions workflow to build backend + frontend.
- Create a .env.example and sample application.properties with placeholders.
- Add a Docker Compose for local full-stack with MySQL.
