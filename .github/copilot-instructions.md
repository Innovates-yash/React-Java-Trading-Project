# AI Coding Agent Instructions for Trading Platform

## Project Overview
This is a **Binance-like cryptocurrency trading platform** with:
- **Backend**: Spring Boot 3.2.4 (Java 19) REST API serving MySQL database
- **Frontend**: React 18 with Vite, Redux state management, Tailwind CSS styling
- Runs on **port 5454** (backend) / **Vite dev server** (frontend)

## Architecture

### Backend Structure (`Backend-Spring boot/src/main/java/com/zosh/`)
- **controller/**: REST endpoints (Auth, Order, Wallet, Asset, Coin, Payment, etc.)
- **service/**: Business logic layer (JWT handled in `config/`)
- **model/**: JPA entities for User, Order, Wallet, Coin, Asset
- **repository/**: Spring Data JPA repositories
- **config/**: 
  - `JwtProvider.java` - generates JWT with email claim + 24hr expiration
  - `JwtTokenValidator.java` - validates Bearer tokens
  - `AppConfig.java` - security configuration
- **exception/**: Custom exception handling
- **request/response/**: DTOs for API payloads

### Frontend Structure (`Frontend-React/src/`)
- **Redux/**: State slices (Auth, Coin, Order, Wallet, Asset, Watchlist, Withdrawal, Chat)
- **pages/**: Page components (Auth, Home, Wallet, Portfolio, Profile, Activity)
- **components/ui/**: Radix UI primitives + custom wrappers
- **components/custome/**: Reusable custom components (SpinnerBackdrop, CustomeToast)
- **Api/api.js**: Axios instance with JWT bearer token auto-inject from localStorage
- **Util/**: Helper functions (date formatting, profit calculations, watchlist checks)

### Data Flow
1. **Login**: User credentials → Auth controller → JWT generated with email + roles
2. **Protected API calls**: Frontend stores JWT in localStorage → Axios auto-injects `Authorization: Bearer <token>`
3. **State Management**: Redux thunk dispatches → service API calls → reducers update store
4. **Styling**: Tailwind CSS with Radix UI headless components

## Critical Patterns & Conventions

### Authentication & Security
- **JWT stored in**: `localStorage` (key: `'jwt'`)
- **Token format**: `Bearer <jwt>` in Authorization header
- **JWT structure**: Claims include `email` and `authorities` (roles)
- **Token expiration**: 24 hours (86400000ms)
- Extract email from token: `JwtProvider.getEmailFromJwtToken(jwt)` (backend)

### Redux State Management
- **Store location**: `Store.js` uses `legacy_createStore` with redux-thunk
- **Pattern**: Each feature has a folder with Reducer + Action (Auth/, Coin/, Order/, etc.)
- **Async actions**: Use thunk middleware to dispatch API calls
- **State access**: `useSelector((store) => store.featureName)`

### Frontend Component Patterns
- **Form handling**: React Hook Form + Yup/Zod validation (see Auth components)
- **UI components**: Use from `components/ui/` (shadcn-style Radix wrapper)
- **Toast notifications**: `useToast()` hook + `CustomeToast` component
- **Spinner loading**: `SpinnerBackdrop` overlay component
- **Routing**: React Router v6 with `useNavigate`, `useLocation`

### API Patterns (Backend)
- **Base URL**: `http://localhost:5454` (configurable to Railway deployment)
- **Controllers follow pattern**: `/{resource}/{action}` (e.g., `/api/orders/create`)
- **Response format**: Standard REST with appropriate HTTP status codes
- **Payment integration**: Stripe, Razorpay configured in `PaymentController`

## External Dependencies & Integration Points

### Backend Dependencies
- **JWT**: JJWT library (jjwt-api, jjwt-impl, jjwt-jackson)
- **Database**: MySQL (configured in `application.properties` on port 3306)
- **Email**: Spring Mail (Gmail SMTP, requires app password)
- **Payments**: Stripe API, Razorpay API
- **External APIs**: CoinGecko (crypto data), Gemini (AI chat), Google OAuth

### Frontend Dependencies
- **State**: Redux + Redux-thunk
- **Forms**: React Hook Form + Resolvers (Yup/Zod)
- **UI**: Radix UI primitives, Tailwind CSS
- **Charts**: ApexCharts, Recharts
- **HTTP**: Axios with auto-jwt injection
- **Validation**: Yup and Zod schemas

## Critical Setup & Build Commands

### Backend Setup
```bash
cd Backend-Spring boot
# Update application.properties with:
# - MySQL credentials (root password)
# - Gmail sender credentials + app password
# - Stripe/Razorpay API keys
# - CoinGecko/Gemini API keys
mvn clean install
mvn spring-boot:run  # Runs on port 5454
```

### Frontend Setup
```bash
cd Frontend-React
npm i
npm run dev   # Dev server (Vite HMR enabled)
npm run build # Production build
npm run lint  # ESLint check
```

## Debugging Tips
- **Frontend auth fails**: Check `localStorage` for `jwt` key and verify Bearer token in Axios requests
- **API calls rejected**: Validate JWT in `JwtTokenValidator` - ensure 24hr expiration hasn't passed
- **State not updating**: Check Redux reducer in `Redux/{FeatureName}/Reducer.js` and thunk dispatch
- **Styling issues**: Verify Tailwind classes in `tailwind.config.js` + check Radix UI imports in `components/ui/`
- **Build errors**: Clear `node_modules` + `package-lock.json`, then `npm i` (frontend); Maven clean install (backend)

## File References for Common Tasks
- **Add API endpoint**: Implement in `Backend-Spring boot/src/main/java/com/zosh/controller/{Feature}Controller.java`
- **Add Redux state**: Create `Frontend-React/src/Redux/{Feature}/Action.js` + `Reducer.js`
- **UI component wrapping**: Reference `Frontend-React/src/components/ui/button.jsx` pattern for Radix wrapper
- **Form validation**: See `Frontend-React/src/pages/Auth/signup/` for React Hook Form + Yup patterns
- **Error handling**: Check `Backend-Spring boot/src/main/java/com/zosh/exception/` for custom exceptions

---
**Note**: Database must exist (`treading`), all API keys configured before running. Frontend assumes backend running on localhost:5454.
