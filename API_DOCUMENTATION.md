# E-commerce MVP API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

---

## Endpoints

### Authentication

#### Register User
```http
POST /auth/register
```

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "role": "BUYER" | "SELLER"
}
```

**Response:** `200 OK`
```json
{
  "token": "string",
  "userId": 1,
  "username": "string",
  "role": "BUYER"
}
```

#### Login
```http
POST /auth/login
```

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:** `200 OK`
```json
{
  "token": "string",
  "userId": 1,
  "username": "string",
  "role": "BUYER"
}
```

---

### Products (Public)

#### Get All Products
```http
GET /products
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 10,
    "sellerId": 2,
    "createdAt": "2024-12-08T10:00:00",
    "updatedAt": "2024-12-08T10:00:00"
  }
]
```

#### Get Product by ID
```http
GET /products/{id}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stock": 10,
  "sellerId": 2,
  "createdAt": "2024-12-08T10:00:00",
  "updatedAt": "2024-12-08T10:00:00"
}
```

---

### Products (Seller Only)

#### Get My Products
```http
GET /products/seller/my-products
```
**Auth Required:** SELLER

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 10,
    "sellerId": 2,
    "createdAt": "2024-12-08T10:00:00",
    "updatedAt": "2024-12-08T10:00:00"
  }
]
```

#### Create Product
```http
POST /products
```
**Auth Required:** SELLER

**Request Body:**
```json
{
  "name": "string",
  "description": "string",
  "price": 99.99,
  "stock": 10
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 99.99,
  "stock": 10,
  "sellerId": 2,
  "createdAt": "2024-12-08T10:00:00",
  "updatedAt": "2024-12-08T10:00:00"
}
```

#### Update Product
```http
PUT /products/{id}
```
**Auth Required:** SELLER (own products only)

**Request Body:**
```json
{
  "name": "string",
  "description": "string",
  "price": 99.99,
  "stock": 10
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 99.99,
  "stock": 10,
  "sellerId": 2,
  "createdAt": "2024-12-08T10:00:00",
  "updatedAt": "2024-12-08T10:00:00"
}
```

#### Delete Product
```http
DELETE /products/{id}
```
**Auth Required:** SELLER (own products only)

**Response:** `204 No Content`

---

### Shopping Cart (Buyer Only)

#### Get Cart
```http
GET /cart
```
**Auth Required:** BUYER

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 1,
    "productId": 1,
    "quantity": 2,
    "product": {
      "id": 1,
      "name": "Laptop",
      "price": 999.99,
      "stock": 10
    }
  }
]
```

#### Add to Cart
```http
POST /cart
```
**Auth Required:** BUYER

**Request Body:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "productId": 1,
  "quantity": 2,
  "product": {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "stock": 10
  }
}
```

#### Update Cart Item Quantity
```http
PUT /cart/{id}?quantity={quantity}
```
**Auth Required:** BUYER

**Query Parameters:**
- `quantity` (integer): New quantity

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "productId": 1,
  "quantity": 3,
  "product": {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "stock": 10
  }
}
```

#### Remove from Cart
```http
DELETE /cart/{id}
```
**Auth Required:** BUYER

**Response:** `204 No Content`

---

### Orders (Buyer Only)

#### Checkout
```http
POST /orders/checkout
```
**Auth Required:** BUYER

**Request Body:**
```json
{
  "couponCode": "SAVE10"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 1999.98,
  "couponCode": "SAVE10",
  "discount": 10.00,
  "finalAmount": 1989.98,
  "status": "COMPLETED",
  "createdAt": "2024-12-08T10:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "priceAtPurchase": 999.99
    }
  ]
}
```

#### Get Order History
```http
GET /orders
```
**Auth Required:** BUYER

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 1999.98,
    "couponCode": "SAVE10",
    "discount": 10.00,
    "finalAmount": 1989.98,
    "status": "COMPLETED",
    "createdAt": "2024-12-08T10:00:00",
    "items": []
  }
]
```

#### Get Order Details
```http
GET /orders/{id}
```
**Auth Required:** BUYER

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 1999.98,
  "couponCode": "SAVE10",
  "discount": 10.00,
  "finalAmount": 1989.98,
  "status": "COMPLETED",
  "createdAt": "2024-12-08T10:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "priceAtPurchase": 999.99
    }
  ]
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "timestamp": "2024-12-08T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error message",
  "path": "/api/products"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-12-08T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-12-08T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/cart"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-12-08T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/products/999"
}
```

---

## Swagger UI

Interactive API documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

Features:
- Try out all endpoints
- View request/response schemas
- JWT authentication support
- Request/response examples

---

## Test Credentials

### Buyer Account
```
Username: buyer
Password: password
```

### Seller Account
```
Username: seller
Password: password
```

### Test Coupons
- `SAVE10` - $10 off
- `SAVE50` - $50 off

---

## Example Usage

### Complete Buyer Flow

1. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"buyer","password":"password"}'
```

2. **Get Products**
```bash
curl http://localhost:8080/api/products
```

3. **Add to Cart**
```bash
curl -X POST http://localhost:8080/api/cart \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}'
```

4. **Checkout with Coupon**
```bash
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"couponCode":"SAVE10"}'
```

### Complete Seller Flow

1. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"seller","password":"password"}'
```

2. **Create Product**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"New Product",
    "description":"Product description",
    "price":49.99,
    "stock":20
  }'
```

3. **Update Product**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Updated Product",
    "description":"Updated description",
    "price":59.99,
    "stock":15
  }'
```

4. **Delete Product**
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```
