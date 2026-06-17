# Java DevCamp — Product Shop API

Spring Boot REST API for a bank product catalogue. Customers can browse products, check eligibility by customer type, and authenticate via JWT.

**Stack:** Java 17 · Spring Boot 4.0.6 · PostgreSQL · Flyway · Spring Security (JWT)

## Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL with pre-existing `cis` and `auth` schemas (customer and user data)
- Database connection configured in `src/main/resources/application.properties`

## Quick start

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:3000**.

| Resource | URL |
|----------|-----|
| Home page | http://localhost:3000/ |
| Health check | http://localhost:3000/health |

## Project structure

```
src/main/java/za/co/entelect/java_devcamp/
├── JavaDevcampApplication.java    # Application entry point
├── config/                        # Security and JSON error handlers
├── constant/                      # Shared constants (e.g. customer types)
├── controller/                    # REST and MVC controllers
├── dto/
│   ├── request/                   # Incoming request bodies
│   └── response/                  # Outgoing response bodies
├── entity/                        # JPA entities (public schema)
├── exception/                     # Custom exceptions and global handler
├── repository/                    # Spring Data JPA repositories
└── service/                       # Business logic
```

## Authentication

Protected endpoints require a JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

Obtain a token via `POST /api/v1/auth/login`. Tokens expire after 3600 seconds (configurable via `jwt.expiry-seconds`).

---

## API reference

Base path: `/api/v1`

### Authentication

#### `POST /api/v1/auth/login`

Authenticate and receive a JWT.

**Auth:** None

**Request body:**

```json
{
  "email": "products@entelect.co.za",
  "password": "SpringProducts01$"
}
```

**Response `200`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

**Response `401`:** Invalid email or password

---

### Profile

#### `GET /api/v1/profile`

Returns the authenticated user's application record and linked CIS customer profile.

**Auth:** Required

**Response `200`:**

```json
{
  "user_id": 1,
  "email": "admin@entelect.co.za",
  "role": "user",
  "customer": {
    "customer_id": 3,
    "email": "admin@entelect.co.za",
    "first_name": "admin",
    "last_name": "admin",
    "customer_types_id": 5
  }
}
```

**Response `401`:** Not authenticated  
**Response `404`:** User not found

---

### Customers

#### `GET /api/v1/customers`

Returns the authenticated customer's record. Admin users (`customer_types_id = 5`) receive all customers.

**Auth:** Required

**Response `200`:** Array of customer objects

**Response `401`:** Not authenticated  
**Response `404`:** Customer not found

---

#### `GET /api/v1/customers/{id}`

Returns a single customer by ID from the CIS schema.

**Auth:** None

**Response `200`:** Customer object

**Response `404`:** Customer not found

---

#### `POST /api/v1/customers`

Registers a new customer in CIS and creates login credentials in `auth.application_user`.

**Auth:** None

**Request body:**

```json
{
  "email": "jane.doe@example.com",
  "first_name": "Jane",
  "last_name": "Doe",
  "id_number": "9001015800085",
  "password": "MySecurePassword1$"
}
```

**Response `201`:** Created customer object (includes `customer_types_id`, default `1`)

**Response `409`:** Email already registered

---

#### `GET /api/v1/customers/{customerId}/eligible-products`

Returns active products the customer is eligible for based on their customer type.

**Auth:** None

**Response `200`:** Array of product objects

**Response `404`:** Customer not found

---

### Products

#### `GET /api/v1/products`

Returns the full product catalogue.

**Auth:** None

**Response `200`:** Array of product objects

---

#### `GET /api/v1/products/{id}`

Returns a single product by ID.

**Auth:** None

**Response `200`:** Product object

**Response `404`:** Product not found

---

#### `POST /api/v1/products/{productId}/take-up`

Checks whether the authenticated customer is eligible to take up a product. Does not persist a take-up — returns an eligibility result only.

**Auth:** Required

**Response `200`:**

```json
{
  "customer_id": 1,
  "email": "jane.doe@example.com",
  "product_id": 2,
  "product_name": "Gold Cheque Account",
  "eligible": true
}
```

**Response `401`:** Not authenticated  
**Response `404`:** Product or customer not found

---

### Actuator

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/health` | Application health status |
| `GET` | `/info` | Build and application metadata |
| `GET` | `/metrics` | Runtime metrics |

---

## Error responses

All API errors return JSON:

```json
{
  "error": "Human-readable message"
}
```

| Status | When |
|--------|------|
| `401` | Missing or invalid JWT, or bad login credentials |
| `403` | Authenticated but not authorized |
| `404` | Customer, product, or user not found |
| `409` | Duplicate email on registration |
| `500` | Unexpected server error |

---

## Database

Flyway manages the `public` schema (products, fulfilment types, product–customer-type eligibility). The `cis` and `auth` schemas are external and must exist before startup.

| Schema | Purpose |
|--------|---------|
| `public` | Product catalogue (managed by Flyway) |
| `cis` | Customer records |
| `auth` | Application users and credentials |

### Customer types (eligibility)

| ID | Type |
|----|------|
| 1 | Individual |
| 2 | Sole Proprietor |
| 3 | Non-Profit |
| 4 | CIPC Registered |
| 5 | Admin |

---

## Configuration

Key settings in `application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `3000` | HTTP port |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/postgres` | Database URL |
| `jwt.secret` | (dev key) | HS256 signing secret (min 32 chars) |
| `jwt.expiry-seconds` | `3600` | Token lifetime |

---

## Development

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package
mvn package
```
