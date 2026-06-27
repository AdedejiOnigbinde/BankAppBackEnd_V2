# BankApp Backend

A RESTful banking API built with **Spring Boot 2.7 / Java 17**, providing account management, internal & external transfers, bill payments, deposits, and a loan application/approval workflow, secured with stateless **JWT authentication** and **role-based access control**.

This service is the API for the [`FrontEnd`](../FrontEnd) client application.

## Tech Stack

| Concern | Technology |
|---|---|
| Language / Runtime | Java 17 |
| Framework | Spring Boot 2.7.4 (Web, Security, Data JPA, Validation, AOP, Session) |
| Build | Gradle (wrapper included) |
| Database | MySQL (local/dev), PostgreSQL (production) |
| Auth | JWT (`io.jsonwebtoken`), BCrypt password hashing |
| Mapping | ModelMapper (entity ↔ DTO) |
| Logging | Log4j2 + AspectJ logging aspect |
| Testing | JUnit 5, Mockito, Spring Security Test, JaCoCo coverage |
| Object Boilerplate | Lombok |
| Deployment | Heroku (`Procfile`, `system.properties`) |

## Architecture

The codebase follows a classic layered structure under `src/main/java/com/base/BaseDependencies`:

```
Controller/    REST endpoints (Account, Auth, Client, Transaction)
Service/       Business logic
Repository/    Spring Data JPA repositories
Models/        JPA entities (Client, Account, Transaction, Loan, ...)
Dtos/          API request/response shapes (incl. RequestDtos/)
Security/      JWT filter, UserDetailsService, SecurityConfig
Utils/         JwtManager, AccountNumberGenerator
Aspects/       Cross-cutting request/response/exception logging
Configuration/ Bean wiring (ModelMapper, PasswordEncoder, AuthenticationProvider)
ExceptionHandler/ Global @ControllerAdvice + domain-specific exceptions
Constants/     Centralized error/general message strings
```

Cross-cutting `@AfterReturning`/`@Before`/`@AfterThrowing` advice in `LoggingAspect` logs every method call, its arguments, return value, and any thrown exception across the whole `com.base.BaseDependencies` package — useful for tracing requests end-to-end without scattering log statements through services.

## Domain Model

- **Client** — a bank customer (or admin), holding credentials, `Role`s (`ADMIN`/`USER`), and owning `Account`s, `Beneficiary`s, `Bill`s, `Loan`s, `LoanRequest`s, and `PaidBills`.
- **Account** — a numbered account with `balance`, `dailyTransferLimit`, `calcLimit` (running total counted against the limit), `accountType`/`accountStatus`, plus its `Transaction`s, `DepositRequest`s, and `LimitModificationRequest`s.
- **Transaction** — a record of an inner-bank or outer-bank transfer against an account.
- **Loan / LoanRequest** — a client's loan application (amount, duration, interest rate, installment, status, amount paid) with an admin approval workflow.
- **Beneficiary** — a saved transfer recipient for a client.
- **Bill / PaidBills** — billers a client can pay, and the resulting payment history.
- **DepositRequest** — a client-submitted cash deposit pending admin processing.
- **LimitModificationRequest** — a request to change an account's daily transfer limit.

## Authentication & Security

- **Stateless JWT auth** — `JwtManager` issues HMAC-SHA signed tokens (24h validity, `BankerApi` issuer). Clients send `Authorization: bearer <token>`.
- **`JwtAuthenticationFilter`** runs once per request, validates the token signature, and reads the user's roles directly from the JWT `roles` claim — no database lookup on every request. `CustomUserDetailsService` is used only at login by `DaoAuthenticationProvider`.
- **PIN lockout** — after 3 consecutive wrong PIN attempts the account is locked for 30 minutes (`Client.pinLockedUntil`). Any transfer or bill-payment endpoint that requires a PIN will return 423 while locked.
- **Password storage** uses `BCryptPasswordEncoder` via a `DaoAuthenticationProvider`.
- **Role-based authorization** is centrally defined in `SecurityConfig.Endpoints`:
  - `ADMIN`-only: listing all accounts/clients, processing deposits, approving/rejecting loans.
  - `USER`-only: transfers, bill payments, account creation, profile/beneficiary/bill management, loan applications.
  - Shared (`ADMIN` or `USER`): reading accounts/transactions.
  - `/auth/**` is open (registration & login).
- **CORS** is wide open (`@CrossOrigin("*")`) on every controller — tighten this before any production exposure beyond the paired frontend.
- Session creation policy is `STATELESS` (no server-side session state).

## API Overview

### Auth — `/auth`
| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/auth/register` | Public | Register a new client (`USER` role) |
| POST | `/auth/register-admin` | Public | Register a new admin (`ADMIN` role) |
| POST | `/auth/login` | Public | Authenticate, returns a JWT |

### Accounts — `/account`
| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/account/create` | USER | Open a new account for the authenticated client |
| GET | `/account/all?page=0` | ADMIN | Paginated list of all accounts (10 per page) |
| GET | `/account/client` | USER/ADMIN | All accounts owned by the authenticated client |
| GET | `/account/{accountId}` | USER/ADMIN | Fetch a single account |
| DELETE | `/account/{accountId}` | USER | Close/delete an account |
| POST | `/account/deposit` | USER | Submit a deposit request |
| PATCH | `/account/deposit` | ADMIN | Process (approve/credit) a deposit request |
| GET | `/account/deposit` | USER | The client's own deposit requests |
| GET | `/account/deposit/all` | ADMIN | All pending/processed deposit requests |

### Client — `/client`
| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/client/all?page=0` | ADMIN | Paginated list of all clients (10 per page) |
| DELETE | `/client/remove` | USER | Delete the authenticated client's account |
| GET / DELETE | `/client/beneficiary`, `/client/beneficiary/{id}` | USER | View / remove saved beneficiaries |
| GET / DELETE | `/client/bill`, `/client/bill/{id}` | USER | View / remove registered bills |
| GET / PATCH | `/client/profile` | USER | View / update profile |
| PATCH | `/client/profile/password` | USER | Change password |
| POST | `/client/loan` | USER | Apply for a loan |
| GET | `/client/loan`, `/client/loan/{loanId}`, `/client/loan/sum` | USER | List loans, fetch one, or get total outstanding sum |
| GET | `/client/loan/admin` | ADMIN | List all pending loan requests |
| PATCH | `/client/loan/status` | ADMIN | Approve/reject a loan request |

### Transactions — `/transaction`
| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/transaction/inner-bank` | USER | Transfer between accounts within the bank |
| POST | `/transaction/outer-bank` | USER | Transfer to an external bank account |
| GET | `/transaction/{accountId}?page=0` | USER/ADMIN | Paginated transaction history for an account (20 per page, newest first) |
| GET | `/transaction/recent` | USER/ADMIN | Recent transactions across the client's accounts |
| GET | `/transaction/bill` | USER/ADMIN | Paid bill history |
| POST | `/transaction/bill` | USER | Pay a registered bill |
| POST | `/transaction/loan` | USER | Make a payment toward a loan |

## Getting Started

### Prerequisites
- JDK 17 (`system.properties` pins the Heroku buildpack to 17 as well)
- A local MySQL 8 instance with a `bankerapp` database for dev

### Configuration
`src/main/resources/application.properties` is **gitignored** (it holds local DB credentials and the JWT secret) — create your own:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankerapp
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
jwt.secret=<base64-encoded-secret>
```

Production (`application-prod.properties`, activated via `SPRING_PROFILES_ACTIVE=prod`) runs against PostgreSQL and reads everything from environment variables: `PROD_DATABASE_HOST`, `PROD_DATABASE_PORT`, `PROD_DATABASE_NAME`, `PROD_DATABASE_USERNAME`, `PROD_DATABASE_PASSWORD`, `JWT_SECRET`, `PORT`.

### Running

```bash
./gradlew bootRun
```

In VS Code: **Run and Debug → "Run Backend"** (debuggable), or **Terminal → Run Task → "Run Backend"**.

### Testing & Coverage

```bash
./gradlew test jacocoTestReport
```

Generates a coverage report at `build/reports/jacoco/test/html/index.html`. In VS Code: **Terminal → Run Task → "Test Coverage"**.

## Deployment

The app is packaged as an executable jar and run via the `Procfile` (`java -jar build/libs/BaseDependencies-0.0.1-SNAPSHOT.jar`), making it deployable directly to Heroku with the `prod` Spring profile active.
