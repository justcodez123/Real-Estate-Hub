# 🗄️ Database Structure & ER Diagram

## Entity-Relationship Diagram

```
┌───────────────────────────────────────────────────────────────┐
│                      DATABASE SCHEMA                          │
└───────────────────────────────────────────────────────────────┘

        ┌─────────────────────┐
        │      USERS          │
        │═════════════════════│
        │ PK: id              │
        │     first_name      │
        │     last_name       │
        │     email (UNIQUE)  │
        │     phone           │
        │     user_type       │
        │     company         │
        │     license_number  │
        │     bio             │
        │     active          │
        │     created_at      │
        │     updated_at      │
        └─────────────────────┘
                │ 1
                │
                │ owns/manages
                │
                ↓ *
        ┌─────────────────────┐
        │    PROPERTIES       │
        │═════════════════════│
        │ PK: id              │
        │     title           │
        │     description     │
        │     price           │
        │     address         │
        │     city            │
        │     state           │
        │     zip_code        │
        │     property_type   │
        │     listing_type    │
        │     bedrooms        │
        │     bathrooms       │
        │     square_feet     │
        │     year_built      │
        │     available       │
        │     created_at      │
        │     updated_at      │
        │ FK: owner_id        │
        └─────────────────────┘
                │ 1
                │
                │ has
                │
                ↓ *
        ┌─────────────────────┐
        │  PROPERTY_IMAGES    │
        │═════════════════════│
        │ PK: id              │
        │     image_url       │
        │     caption         │
        │     is_primary      │
        │     display_order   │
        │     uploaded_at     │
        │ FK: property_id     │
        └─────────────────────┘
```

## 📋 Table Descriptions

### **1. USERS Table**
Stores information about users in the system (agents, owners, buyers, admins).

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Unique identifier |
| first_name | VARCHAR(255) | User's first name |
| last_name | VARCHAR(255) | User's last name |
| email | VARCHAR(255) | User's email (unique) |
| phone | VARCHAR(255) | Contact phone number |
| user_type | ENUM | AGENT, OWNER, BUYER, ADMIN |
| company | VARCHAR(255) | Company name (for agents) |
| license_number | VARCHAR(255) | Real estate license number |
| bio | VARCHAR(1000) | User biography/description |
| profile_image_url | VARCHAR(255) | Profile picture URL |
| active | BOOLEAN | Account active status |
| created_at | TIMESTAMP | Account creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

### **2. PROPERTIES Table**
Stores real estate property listings.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Unique identifier |
| title | VARCHAR(255) | Property title/headline |
| description | VARCHAR(2000) | Detailed description |
| price | DECIMAL(19,2) | Property price |
| address | VARCHAR(255) | Street address |
| city | VARCHAR(255) | City name |
| state | VARCHAR(255) | State/Province |
| zip_code | VARCHAR(255) | Postal code |
| property_type | ENUM | HOUSE, APARTMENT, CONDO, TOWNHOUSE, LAND, COMMERCIAL |
| listing_type | ENUM | FOR_SALE, FOR_RENT |
| bedrooms | INTEGER | Number of bedrooms |
| bathrooms | INTEGER | Number of bathrooms |
| square_feet | DECIMAL(19,2) | Property area in sqft |
| year_built | INTEGER | Construction year |
| available | BOOLEAN | Listing availability status |
| created_at | TIMESTAMP | Listing creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |
| owner_id | BIGINT (FK) | Reference to USERS table |

### **3. PROPERTY_IMAGES Table**
Stores multiple images for each property.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Unique identifier |
| image_url | VARCHAR(255) | Image URL/path |
| caption | VARCHAR(500) | Image description/caption |
| is_primary | BOOLEAN | Primary display image flag |
| display_order | INTEGER | Order for image gallery |
| uploaded_at | TIMESTAMP | Upload timestamp |
| property_id | BIGINT (FK) | Reference to PROPERTIES table |

## 🔗 Relationships

### **1. User → Property (One-to-Many)**
- **Description**: One user can own or list multiple properties
- **Foreign Key**: `properties.owner_id` → `users.id`
- **Cascade**: ON DELETE SET NULL
- **JPA Mapping**:
  - User side: `@OneToMany(mappedBy = "owner")`
  - Property side: `@ManyToOne` with `@JoinColumn(name = "owner_id")`

### **2. Property → PropertyImage (One-to-Many)**
- **Description**: One property can have multiple images
- **Foreign Key**: `property_images.property_id` → `properties.id`
- **Cascade**: ON DELETE CASCADE (orphanRemoval = true)
- **JPA Mapping**:
  - Property side: `@OneToMany(mappedBy = "property")`
  - PropertyImage side: `@ManyToOne` with `@JoinColumn(name = "property_id")`

## 📊 Indexes

Performance indexes are created on frequently queried columns:

```sql
-- Properties table indexes
CREATE INDEX idx_city ON properties(city);
CREATE INDEX idx_property_type ON properties(property_type);
CREATE INDEX idx_listing_type ON properties(listing_type);
CREATE INDEX idx_price ON properties(price);

-- Users table indexes
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_type ON users(user_type);

-- Foreign key indexes
CREATE INDEX idx_property_owner ON properties(owner_id);
CREATE INDEX idx_image_property ON property_images(property_id);
```

## 🎯 H2 Database Configuration

Current configuration in `application.properties`:

```properties
# Database URL (in-memory)
spring.datasource.url=jdbc:h2:mem:realestatedb

# Auto-create tables from JPA entities
spring.jpa.hibernate.ddl-auto=create-drop

# Initialize with sample data
spring.jpa.defer-datasource-initialization=true

# H2 Console access
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 🔍 Sample Queries

### Get all properties with owner information:
```sql
SELECT p.*, u.first_name, u.last_name, u.email 
FROM properties p 
LEFT JOIN users u ON p.owner_id = u.id
WHERE p.available = true;
```

### Get property with all images:
```sql
SELECT p.title, i.image_url, i.caption, i.is_primary
FROM properties p
LEFT JOIN property_images i ON p.id = i.property_id
WHERE p.id = 1
ORDER BY i.display_order;
```

### Get all properties by agent:
```sql
SELECT p.* 
FROM properties p
INNER JOIN users u ON p.owner_id = u.id
WHERE u.user_type = 'AGENT' AND u.email = 'john.smith@realestate.com';
```

## 🚀 Access H2 Console

1. Start the Spring Boot application
2. Navigate to: `http://localhost:8080/h2-console`
3. Enter connection details:
   - JDBC URL: `jdbc:h2:mem:realestatedb`
   - Username: `sa`
   - Password: (leave empty)
4. Click "Connect"

## 📦 Sample Data

The application automatically initializes with:
- **3 Users** (2 Agents, 1 Owner)
- **5 Properties** (various types and listing types)
- **5+ Property Images** (linked to properties)

All sample data is created via the `DataInitializer` class on application startup.
