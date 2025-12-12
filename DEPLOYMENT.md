# Deployment Guide

## Prerequisites

1. **Docker Desktop** installed and running
2. **Git** (optional, for version control)

---

## Quick Start (5 minutes)

### Step 1: Start Docker Desktop

Ensure Docker Desktop is running on your system.

### Step 2: Navigate to Project Directory

```bash
cd "c:\web projects\demo"
```

### Step 3: Start All Services

```bash
docker-compose up -d
```

This command will:
- Build the Spring Boot backend
- Build the Nuxt 3 frontend
- Start PostgreSQL database
- Start pgAdmin (database management tool)

### Step 4: Wait for Services to Start

Check service status:
```bash
docker-compose ps
```

All services should show "Up" status. The backend may take 1-2 minutes to start as it initializes the database.

### Step 5: Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **pgAdmin**: http://localhost:5050

---

## First Time Setup

### Test the Application

1. **Open Frontend**: Navigate to http://localhost:3000

2. **Register as Buyer**:
   - Click "Register"
   - Username: `testbuyer`
   - Email: `testbuyer@example.com`
   - Password: `password`
   - Role: BUYER
   - Click "Register"

3. **Browse Products**:
   - You'll be redirected to the product listing
   - Click on any product to view details

4. **Add to Cart**:
   - Select quantity
   - Click "Add to Cart"

5. **Checkout**:
   - Navigate to "Cart" in the menu
   - Click "Proceed to Checkout"
   - Enter coupon code: `SAVE10`
   - Click "Place Order"

6. **View Order**:
   - Navigate to "Orders" in the menu
   - Click on your order to see details

### Test Seller Features

1. **Logout**: Click "Logout" in the navigation

2. **Login as Seller**:
   - Username: `seller`
   - Password: `password`

3. **Manage Products**:
   - Navigate to "My Products"
   - Click "Add Product" to create a new product
   - Click "Edit" to modify existing products
   - Click "Delete" to remove products

---

## Stopping the Application

### Stop All Services

```bash
docker-compose down
```

### Stop and Remove All Data

```bash
docker-compose down -v
```

**Warning**: This will delete all database data including users, products, and orders.

---

## Troubleshooting

### Backend Won't Start

**Check logs**:
```bash
docker-compose logs backend
```

**Common issues**:
- PostgreSQL not ready: Wait 30 seconds and check again
- Port 8080 already in use: Stop other applications using port 8080
- Build failed: Check Java version in Dockerfile

**Solution**:
```bash
docker-compose down
docker-compose up -d
```

### Frontend Won't Start

**Check logs**:
```bash
docker-compose logs frontend
```

**Common issues**:
- Port 3000 already in use: Stop other applications using port 3000
- Build failed: Check Node.js version in Dockerfile

**Solution**:
```bash
docker-compose down
docker-compose build frontend
docker-compose up -d
```

### Database Connection Issues

**Check PostgreSQL status**:
```bash
docker-compose ps postgres
```

**Reset database**:
```bash
docker-compose down -v
docker-compose up -d
```

### CORS Errors

**Verify backend CORS configuration**:
- Check `SecurityConfig.java`
- Allowed origins should include `http://localhost:3000`

**Restart backend**:
```bash
docker-compose restart backend
```

---

## Development Mode

### Backend Development (without Docker)

1. **Start PostgreSQL**:
```bash
docker-compose up -d postgres
```

2. **Run backend locally**:
```bash
cd backend
./mvnw spring-boot:run
```

Backend will be available at http://localhost:8080

### Frontend Development (without Docker)

1. **Install dependencies**:
```bash
cd frontend
npm install
```

2. **Run development server**:
```bash
npm run dev
```

Frontend will be available at http://localhost:3000 with hot reload.

---

## Database Management

### Access pgAdmin

1. Navigate to http://localhost:5050
2. Login:
   - Email: `admin@admin.com`
   - Password: `admin`

3. Add server connection:
   - Right-click "Servers" → "Register" → "Server"
   - General tab:
     - Name: `E-commerce DB`
   - Connection tab:
     - Host: `postgres`
     - Port: `5432`
     - Database: `ecommerce`
     - Username: `postgres`
     - Password: `postgres`

### View Tables

Navigate to: Servers → E-commerce DB → Databases → ecommerce → Schemas → public → Tables

You should see:
- users
- products
- cart_items
- orders
- order_items
- coupons

---

## API Testing

### Using Swagger UI

1. Navigate to http://localhost:8080/swagger-ui.html

2. **Test Authentication**:
   - Expand "Authentication" section
   - Click "POST /api/auth/login"
   - Click "Try it out"
   - Enter:
     ```json
     {
       "username": "buyer",
       "password": "password"
     }
     ```
   - Click "Execute"
   - Copy the token from response

3. **Authorize**:
   - Click "Authorize" button at top
   - Enter: `Bearer <paste-token-here>`
   - Click "Authorize"

4. **Test Protected Endpoints**:
   - Now you can test any endpoint that requires authentication

### Using cURL

**Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"buyer\",\"password\":\"password\"}"
```

**Get Products**:
```bash
curl http://localhost:8080/api/products
```

**Add to Cart** (replace TOKEN):
```bash
curl -X POST http://localhost:8080/api/cart \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"productId\":1,\"quantity\":2}"
```

---

## Production Deployment

### Environment Variables

Create a `.env` file in production with secure values:

```env
# Database
POSTGRES_DB=ecommerce_prod
POSTGRES_USER=ecommerce_user
POSTGRES_PASSWORD=<strong-random-password>

# pgAdmin
PGADMIN_DEFAULT_EMAIL=admin@yourdomain.com
PGADMIN_DEFAULT_PASSWORD=<strong-random-password>

# Backend (in application.yml)
JWT_SECRET=<64-character-random-string>
```

### Security Checklist

- [ ] Change all default passwords
- [ ] Use strong JWT secret (64+ characters)
- [ ] Enable HTTPS
- [ ] Configure proper CORS origins
- [ ] Disable Swagger in production (or protect it)
- [ ] Set up database backups
- [ ] Configure firewall rules
- [ ] Use environment-specific configurations

### Docker Compose for Production

Modify `docker-compose.yml`:

1. Remove pgAdmin service (or protect it)
2. Add volume mounts for logs
3. Configure restart policies
4. Set resource limits
5. Use production-optimized images

---

## Monitoring

### View Logs

**All services**:
```bash
docker-compose logs -f
```

**Specific service**:
```bash
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

### Check Resource Usage

```bash
docker stats
```

---

## Backup and Restore

### Backup Database

```bash
docker exec postgres-db pg_dump -U postgres ecommerce > backup.sql
```

### Restore Database

```bash
docker exec -i postgres-db psql -U postgres ecommerce < backup.sql
```

---

## Updating the Application

### Pull Latest Changes

```bash
git pull origin main
```

### Rebuild and Restart

```bash
docker-compose down
docker-compose build
docker-compose up -d
```

---

## Support

For issues or questions:
1. Check logs: `docker-compose logs`
2. Review [README.md](file:///c:/web%20projects/demo/README.md)
3. Check [API_DOCUMENTATION.md](file:///c:/web%20projects/demo/API_DOCUMENTATION.md)
4. Review [walkthrough.md](file:///C:/Users/20221204/.gemini/antigravity/brain/c1c2e52f-1354-4ba3-b164-791ce583f149/walkthrough.md)
