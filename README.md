# React-Java-Trading-Project

Project tree (representative)

Treading-Plateform/
.github/
copilot-instructions.md
Source Code/
Backend-Spring boot/
pom.xml
Dockerfile
mvnw, mvnw.cmd
src/
main/
java/
com/
zosh/
TreadingPlateformApplication.java
controller/
AuthController.java
OrderController.java
WalletController.java
CoinController.java
PaymentController.java
AssetController.java
ChatController.java
service/
AuthService.java
CustomeUserServiceImplementation.java
CoinService.java
CoinServiceImpl.java
WalletService.java
OrderService.java
PaymentService.java
ChatBotServiceImpl.java
DataInitializationComponent.java
model/
User.java
Order.java
Wallet.java
Coin.java
Asset.java
TwoFactorAuth.java
repository/
UserRepository.java
OrderRepository.java
WalletRepository.java
CoinRepository.java
config/
JwtProvider.java
JwtTokenValidator.java
AppConfig.java
exception/
GlobalExceptionHandler.java
CustomErrorResponse.java
request/
AuthRequest.java
OrderRequest.java
response/
ApiResponse.java
AuthResponse.java
resources/
application.properties
db/
migrations/ (Flyway or SQL)
test/
java/
com/
zosh/
TreadingPlateformApplicationTests.java
Frontend-React/
package.json
package-lock.json
vite.config.js
tailwind.config.js
postcss.config.js
index.html
public/
src/
main.jsx (or index.jsx)
Api/
api.js
Redux/
Store.js
Auth/
Action.js
Reducer.js
Coin/
Order/
Wallet/
Asset/
Watchlist/
Withdrawal/
Chat/
pages/
Auth/
Login.jsx
Signup.jsx
Home/
Wallet/
PaymentDetailsForm.jsx
Portfolio/
Profile/
Profile.jsx
AccountVarificationForm.jsx
Activity/
Navbar/
Navbar.jsx
components/
ui/
button.jsx
dialog.jsx
badge.jsx
card.jsx
toast.jsx
spinnerBackdrop.jsx
custome/
SpinnerBackdrop.jsx
CustomeToast.jsx
Util/
date.js
profitCalc.js
watchlist.js
assets/
styles/
README.md
README.md
.gitignore
Detailed project summary

High-level purpose
Binance-like crypto trading platform: web frontend (React + Vite) and Java Spring Boot backend (MySQL). Core domains: authentication, orders/trading, wallet/balances, coins/market data, payments, user profile & KYC, chat/bot.
Backend (Backend-Spring boot)
Tech: Spring Boot 3.2.x on Java 19, Spring Data JPA, MySQL, JJWT for JWT, Spring Mail, Stripe/Razorpay SDKs for payments.
Package layout:
controller: REST endpoints per domain (Auth, Orders, Wallet, Coin, Payment).
service: business logic, transactional boundaries, third-party integrations.
model: JPA entities (User, Wallet, Order, Coin, Asset, TwoFactorAuth).
repository: Spring Data repositories for DB access.
config: security & JWT (JwtProvider creates tokens with email + authorities, 24hr expiration; JwtTokenValidator validates bearer tokens; AppConfig wires security filter chain).
exception: centralized exception handling with @ControllerAdvice.
request/response: DTOs for input/output.
Important flows:
Auth: POST /api/auth/login -> JwtProvider builds JWT including email claim; response contains token.
Protected APIs: JwtTokenValidator checks Authorization: Bearer <jwt>, extracts email and roles.
Wallet/Orders: @Transactional service methods with locking or optimistic locking to prevent race conditions.
Payments: PaymentController delegates to Stripe/Razorpay SDKs, uses webhooks for asynchronous confirmation.
Config: application.properties contains DB credentials, mail, API keys. Backend default port: 5454.
Frontend (Frontend-React)
Tech: React 18, Vite, Redux + redux-thunk, React Hook Form + Yup/Zod, TailwindCSS, Radix UI wrappers, Axios for HTTP.
Structure:
Redux slices: Auth, Coin, Order, Wallet, Asset, etc. Store configured in Store.js using legacy_createStore and thunk.
Api/api.js: Axios instance configured with baseURL http://localhost:5454 and interceptor to inject Authorization: Bearer <jwt> where jwt is localStorage.getItem('jwt').
Pages & Components: pages/* for routes (Profile.jsx, Wallet, Home). UI primitives in components/ui and reusable components in components/custome.
Key UI flows:
Login/Signup: forms use React Hook Form; on success store token in localStorage key 'jwt' and load user into Redux.
Profile (example): Profile.jsx shows user info, 2-step verification status and uses AccountVarificationForm to enable/verify OTP; dispatches enableTwoStepAuthentication and verifyOtp actions that call backend with jwt and otp.
Live market data: market data fetched via Coin service; charts via ApexCharts/Recharts.
Security pattern:
JWT stored in localStorage under 'jwt'. Frontend includes it in Authorization header.
Forms validated using Yup/Zod; backend revalidates inputs.
Integrations & external systems
CoinGecko (market data), Gemini/AI for chat (ChatBotServiceImpl on backend), Stripe/Razorpay for payments, Gmail SMTP for email (requires app password).
Caching recommended for market data (e.g., Redis) in production.
Dev & build
Backend:
mvn clean install
mvn spring-boot:run (runs on port 5454)
Configure application.properties before running (DB, mail, API keys)
Frontend:
npm i
npm run dev (Vite)
npm run build
npm run lint
Important conventions & debugging tips
JWT expiry: 24 hours. If API calls fail, check localStorage 'jwt' and token expiration.
Axios: single instance injects JWT automatically; inspect network requests in browser DevTools.
State issues: check feature Reducer.js and Action.js for thunk patterns.
Concurrency: use DB transactions + locking; for multi-service flows use SAGA/event-driven compensations.
If frontend build or dependency errors: delete node_modules and package-lock.json then npm i.
Notable file examples (quick references)
Backend: com.zosh.config.JwtProvider.java (token creation), JwtTokenValidator.java (filter), AppConfig.java (security).
Frontend: src/Api/api.js (axios), src/Redux/Auth/Action.js & Reducer.js, Profile.jsx (2FA & verify flows — uses enableTwoStepAuthentication and verifyOtp actions), package.json at Frontend-React root.
