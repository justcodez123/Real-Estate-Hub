-- ========================================
-- Real Estate Database Schema
-- ========================================
-- This file documents the database structure
-- H2 will auto-generate these tables based on JPA entities
-- ========================================

-- ========================================
-- USERS TABLE
-- ========================================
-- Stores information about users (agents, owners, buyers, admins)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,  -- AGENT, OWNER, BUYER, ADMIN
    company VARCHAR(255),
    license_number VARCHAR(255),
    bio VARCHAR(1000),
    profile_image_url VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subscription_type VARCHAR(50) NOT NULL DEFAULT 'FREE'
);

-- ========================================
-- PROPERTIES TABLE
-- ========================================
-- Stores property listings
CREATE TABLE properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    property_type VARCHAR(50) NOT NULL,  -- HOUSE, APARTMENT, CONDO, TOWNHOUSE, LAND, COMMERCIAL
    listing_type VARCHAR(50) NOT NULL,   -- FOR_SALE, FOR_RENT
    bedrooms INTEGER,
    bathrooms INTEGER,
    square_feet DECIMAL(19,2),
    year_built INTEGER,
    image_url VARCHAR(255),  -- Deprecated, kept for backward compatibility
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    owner_id BIGINT,  -- Foreign Key to users table
    
    -- Foreign Key Constraint
    CONSTRAINT fk_property_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ========================================
-- PROPERTY_IMAGES TABLE
-- ========================================
-- Stores multiple images for each property
CREATE TABLE property_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    caption VARCHAR(500),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    property_id BIGINT NOT NULL,  -- Foreign Key to properties table
    
    -- Foreign Key Constraint
    CONSTRAINT fk_image_property FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);

-- ========================================
-- CONTACT_AGENTS TABLE
-- ========================================
-- Stores contact/inquiry messages from users to property agents/owners
CREATE TABLE contact_agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    sender_name VARCHAR(255) NOT NULL,
    sender_email VARCHAR(255) NOT NULL,
    sender_phone VARCHAR(255) NOT NULL,
    additional_info TEXT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP,

    -- Foreign Key Constraints
    CONSTRAINT fk_contact_agent_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_contact_agent_property FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);

-- ========================================
-- SCHEDULE_VIEWINGS TABLE
-- ========================================
-- Stores property viewing schedules/appointments
CREATE TABLE schedule_viewings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    viewing_date DATE NOT NULL,
    viewing_time TIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',  -- PENDING, CONFIRMED, REJECTED, COMPLETED, CANCELLED
    notes VARCHAR(1000),
    rejection_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    rejected_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,

    -- Foreign Key Constraints
    CONSTRAINT fk_viewing_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_viewing_property FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);

-- ========================================
-- SUBSCRIPTIONS TABLE
-- ========================================
-- Stores subscription details for users
CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- Foreign Key to users table
    plan_type VARCHAR(50) NOT NULL DEFAULT 'FREE',
    start_date DATE NOT NULL,
    end_date DATE,
    price DECIMAL(19,2),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    auto_renew BOOLEAN NOT NULL DEFAULT FALSE,
    payment_method VARCHAR(255),
    transaction_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraint
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================================
-- INDEXES FOR PERFORMANCE
-- ========================================
CREATE INDEX idx_city ON properties(city);
CREATE INDEX idx_property_type ON properties(property_type);
CREATE INDEX idx_listing_type ON properties(listing_type);
CREATE INDEX idx_price ON properties(price);
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_type ON users(user_type);
CREATE INDEX idx_property_owner ON properties(owner_id);
CREATE INDEX idx_image_property ON property_images(property_id);
CREATE INDEX idx_contact_agent_user ON contact_agents(user_id);
CREATE INDEX idx_contact_agent_property ON contact_agents(property_id);
CREATE INDEX idx_contact_agent_is_read ON contact_agents(is_read);
CREATE INDEX idx_schedule_viewing_user ON schedule_viewings(user_id);
CREATE INDEX idx_schedule_viewing_property ON schedule_viewings(property_id);
CREATE INDEX idx_schedule_viewing_status ON schedule_viewings(status);
CREATE INDEX idx_schedule_viewing_date ON schedule_viewings(viewing_date);

-- ========================================
-- ENTITY RELATIONSHIPS (ER Diagram)
-- ========================================
/*
┌─────────────────────────────┐
│      USERS                  │
│─────────────────────────────│
│ PK: id                      │
│     first_name              │
│     last_name               │
│     email (unique)          │
│     phone                   │
│     user_type               │
│     company                 │
│     license_number          │
│     bio                     │
│     active                  │
│     created_at              │
│     updated_at              │
└──────────┬──────────────────┘
           │ 1
           │
    ┌──────┴────────┬──────────────────┐
    │ owns/lists    │ contacts/inquires │ schedules
    │ viewings      │ about             │ viewings
    │               │                   │
    │ *             │ *                 │ *
    │               │                   │
┌───┴────────────┐ ┌┴──────────────────┐ ┌─────────────────────┐
│   PROPERTIES   │ │ CONTACT_AGENTS    │ │ SCHEDULE_VIEWINGS   │
│────────────────│ │───────────────────│ │─────────────────────│
│ PK: id         │ │ PK: id            │ │ PK: id              │
│     title      │ │     subject       │ │     viewing_date    │
│     price      │ │     message       │ │     viewing_time    │
│     address    │ │     sender_name   │ │     status          │
│     city       │ │     sender_email  │ │     notes           │
│     state      │ │     sender_phone  │ │     rejection_reason│
│     bedrooms   │ │     is_read       │ │     created_at      │
│     bathrooms  │ │     created_at    │ │     confirmed_at    │
│ FK: owner_id   │ │ FK: user_id       │ │     rejected_at     │
│     (to USERS) │ │ FK: property_id   │ │     completed_at    │
│                │ │     (to PROPERTY) │ │     cancelled_at    │
└───────┬────────┘ └───────────────────┘ │ FK: user_id         │
        │                                │     (to USERS)      │
        │                                │ FK: property_id     │
        │                                │     (to PROPERTY)   │
        │ has                            └─────────────────────┘
        │
        │ *
┌───────┴─────────────┐
│ PROPERTY_IMAGES     │
│─────────────────────│
│ PK: id              │
│     image_url       │
│     caption         │
│     is_primary      │
│     display_order   │
│ FK: property_id     │
│     (to PROPERTIES) │
└─────────────────────┘

RELATIONSHIPS:
─────────────────────────────────────────────────────────────────
1. USER ──< PROPERTY (One-to-Many)
   - One user can own/list multiple properties
   - Relationship: User.properties ↔ Property.owner
   - Foreign Key: properties.owner_id → users.id
   - On Delete: SET NULL (property remains if user deleted)

2. PROPERTY ──< PROPERTY_IMAGE (One-to-Many)
   - One property can have multiple images
   - Relationship: Property.images ↔ PropertyImage.property
   - Foreign Key: property_images.property_id → properties.id
   - On Delete: CASCADE (images deleted when property deleted)

3. USER ──< CONTACT_AGENT (One-to-Many)
   - One user can send multiple contact messages
   - Relationship: User ↔ ContactAgent.user
   - Foreign Key: contact_agents.user_id → users.id
   - On Delete: CASCADE (contacts deleted when user deleted)

4. PROPERTY ──< CONTACT_AGENT (One-to-Many)
   - One property can have multiple contact inquiries
   - Relationship: Property ↔ ContactAgent.property
   - Foreign Key: contact_agents.property_id → properties.id
   - On Delete: CASCADE (contacts deleted when property deleted)

5. USER ──< SCHEDULE_VIEWING (One-to-Many)
   - One user can schedule multiple viewings
   - Relationship: User ↔ ScheduleViewing.user
   - Foreign Key: schedule_viewings.user_id → users.id
   - On Delete: CASCADE (viewings deleted when user deleted)

6. PROPERTY ──< SCHEDULE_VIEWING (One-to-Many)
   - One property can have multiple viewing schedules
   - Relationship: Property ↔ ScheduleViewing.property
   - Foreign Key: schedule_viewings.property_id → properties.id
   - On Delete: CASCADE (viewings deleted when property deleted)
─────────────────────────────────────────────────────────────────

CARDINALITY:
─────────────────────────────────────────────────────────────────
User (1) ──────< (Many) Property
User (1) ──────< (Many) ContactAgent
User (1) ──────< (Many) ScheduleViewing
Property (1) ──────< (Many) PropertyImage
Property (1) ──────< (Many) ContactAgent
Property (1) ──────< (Many) ScheduleViewing
─────────────────────────────────────────────────────────────────
*/
