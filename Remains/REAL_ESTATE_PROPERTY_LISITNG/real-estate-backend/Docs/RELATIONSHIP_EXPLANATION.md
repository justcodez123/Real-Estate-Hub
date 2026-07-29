# Real Estate Application - Entity Relationships Explained

## Overview
This document explains the three types of JPA relationships used in the real estate application:
- **One-to-Many (1:N)**
- **Many-to-One (N:1)**
- **One-to-One (1:1)**

---

## 📊 Relationship Types

### 1. ONE-TO-MANY (1:N) Relationships

#### What is One-to-Many?
One entity is associated with multiple instances of another entity. The "one" side can have many "many" sides, but each "many" side belongs to exactly one "one" side.

**Example**: One user can own multiple properties, but each property has only one owner.

---

### 2. MANY-TO-ONE (N:1) Relationships

#### What is Many-to-One?
Multiple entities belong to a single entity. It's the reverse perspective of One-to-Many.

**Example**: Many properties belong to one user (the owner).

**Note**: One-to-Many and Many-to-One are the same relationship viewed from different perspectives!

---

### 3. ONE-TO-ONE (1:1) Relationships

#### What is One-to-One?
Each entity on one side is associated with exactly one entity on the other side, and vice versa.

**Example**: One user has exactly one subscription.

---

## 🔗 Project Relationships in Detail

### **RELATIONSHIP 1: USER ↔ PROPERTY**

#### Type: **ONE-TO-MANY** / **MANY-TO-ONE**

#### Explanation:
- **One User** can own/list **Multiple Properties**
- **Many Properties** belong to **One User** (the owner)

#### Implementation in Code:

**User.java** (One side - User has many properties):
```java
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonIgnoreProperties({"owner"})
private List<Property> properties = new ArrayList<>();
```

**Property.java** (Many side - Property belongs to one user):
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "owner_id", nullable = true)
@JsonIgnoreProperties({"properties", "hibernateLazyInitializer", "handler"})
private User owner;
```

#### Database Schema:
```
USER TABLE                    PROPERTY TABLE
┌──────────────────┐         ┌──────────────────┐
│ id (PK)          │         │ id (PK)          │
│ firstName        │         │ title            │
│ lastName         │         │ price            │
│ email            │  1 ───→ │ owner_id (FK)    │ ← Many
│ ...              │   Many  │ ...              │
└──────────────────┘         └──────────────────┘

Foreign Key: properties.owner_id → users.id
Cardinality: 1:N (One user to Many properties)
```

#### Cascade Behavior:
- **CascadeType.ALL**: When user is saved/updated/deleted, properties are affected
- **orphanRemoval**: When a property is removed from the collection, it's deleted from DB

#### Example Data:
```
User: John Smith (ID: 1)
  ├─ Property 1: Villa in Mumbai (ID: 101)
  ├─ Property 2: Apartment in Delhi (ID: 102)
  └─ Property 3: Penthouse in Bangalore (ID: 103)

User: Sarah Jones (ID: 2)
  └─ Property 1: Cottage in Goa (ID: 201)
```

#### SQL Query Perspective:
```sql
-- Get all properties owned by user with ID 1
SELECT * FROM properties WHERE owner_id = 1;

-- Get the owner of property with ID 101
SELECT * FROM users WHERE id = (SELECT owner_id FROM properties WHERE id = 101);
```

---

### **RELATIONSHIP 2: PROPERTY ↔ PROPERTY_IMAGE**

#### Type: **ONE-TO-MANY** / **MANY-TO-ONE**

#### Explanation:
- **One Property** can have **Multiple Images**
- **Many PropertyImages** belong to **One Property**

#### Implementation in Code:

**Property.java** (One side - Property has many images):
```java
@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
@JsonIgnoreProperties({"property"})
private List<PropertyImage> images = new ArrayList<>();

// Helper methods
public void addImage(PropertyImage image) {
    images.add(image);
    image.setProperty(this);
}

public void removeImage(PropertyImage image) {
    images.remove(image);
    image.setProperty(null);
}
```

**PropertyImage.java** (Many side - Image belongs to one property):
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "property_id", nullable = false)
private Property property;
```

#### Database Schema:
```
PROPERTY TABLE              PROPERTY_IMAGE TABLE
┌──────────────────┐       ┌──────────────────┐
│ id (PK)          │       │ id (PK)          │
│ title            │       │ imageUrl         │
│ price            │  1 ──→ │ property_id (FK) │ ← Many
│ ...              │        │ isPrimary        │
└──────────────────┘       │ displayOrder     │
                           └──────────────────┘

Foreign Key: property_images.property_id → properties.id
Cascade: DELETE CASCADE + orphanRemoval = true
```

#### Cascade Behavior:
- **CascadeType.ALL**: When property is saved/updated/deleted, images are affected
- **orphanRemoval = true**: When an image is removed from collection, it's deleted from DB

#### Example Data:
```
Property: Villa in Mumbai (ID: 101)
  ├─ Image 1: villa_front.jpg (isPrimary: true, order: 1)
  ├─ Image 2: villa_side.jpg (isPrimary: false, order: 2)
  ├─ Image 3: villa_pool.jpg (isPrimary: false, order: 3)
  └─ Image 4: villa_interior.jpg (isPrimary: false, order: 4)

Property: Apartment in Delhi (ID: 102)
  ├─ Image 1: apt_front.jpg (isPrimary: true, order: 1)
  └─ Image 2: apt_kitchen.jpg (isPrimary: false, order: 2)
```

#### SQL Query Perspective:
```sql
-- Get all images for property with ID 101
SELECT * FROM property_images WHERE property_id = 101 ORDER BY display_order;

-- Get the primary image of a property
SELECT * FROM property_images WHERE property_id = 101 AND is_primary = true;

-- Delete all images when property is deleted
DELETE FROM property_images WHERE property_id = 101;
```

---

### **RELATIONSHIP 3: PROPERTY ↔ BUILDER_GROUP**

#### Type: **MANY-TO-ONE** / **ONE-TO-MANY**

#### Explanation:
- **Many Properties** can belong to **One BuilderGroup**
- **One BuilderGroup** has **Multiple Properties**

#### Implementation in Code:

**BuilderGroup.java** (One side - BuilderGroup has many properties):
```java
@OneToMany(mappedBy = "builderGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Property> properties = new ArrayList<>();
```

**Property.java** (Many side - Property belongs to one builder group):
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "builder_group_id", nullable = true)
@JsonIgnoreProperties({"properties", "hibernateLazyInitializer", "handler"})
private BuilderGroup builderGroup;
```

#### Database Schema:
```
BUILDER_GROUP TABLE         PROPERTY TABLE
┌──────────────────┐       ┌──────────────────┐
│ id (PK)          │       │ id (PK)          │
│ name             │       │ title            │
│ description      │  1 ──→ │ builder_group_id │ ← Many
│ active           │        │ (FK)             │
│ ...              │       │ ...              │
└──────────────────┘       └──────────────────┘

Foreign Key: properties.builder_group_id → builder_groups.id
```

#### Example Data:
```
BuilderGroup: Shapoorji Pallonji (ID: 1)
  ├─ Property 1: Premium Tower Mumbai (ID: 501)
  ├─ Property 2: Grandeur Bangalore (ID: 502)
  └─ Property 3: Elite Delhi (ID: 503)

BuilderGroup: TCG (The Concrete Giants) (ID: 2)
  ├─ Property 1: Vision Heights Pune (ID: 601)
  └─ Property 2: Essence Gurgaon (ID: 602)
```

#### Filtering by Builder Group:
```sql
-- Get all properties from a specific builder group
SELECT * FROM properties WHERE builder_group_id = 1;

-- Get builder group details and all their properties
SELECT bg.*, p.* FROM builder_groups bg
LEFT JOIN properties p ON bg.id = p.builder_group_id
WHERE bg.id = 1;
```

---

### **RELATIONSHIP 4: USER ↔ FAVORITE**

#### Type: **ONE-TO-MANY** / **MANY-TO-ONE**

#### Explanation:
- **One User** can have **Multiple Favorite Properties**
- **Many Favorites** belong to **One User**

#### Implementation in Code:

**User.java** (One side):
```java
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonIgnoreProperties({"user"})
private List<Favorite> favorites = new ArrayList<>();
```

**Favorite.java** (Many side - Junction entity):
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "property_id", nullable = false)
private Property property;
```

#### Database Schema:
```
USER TABLE                  FAVORITE TABLE              PROPERTY TABLE
┌──────────────┐           ┌──────────────┐           ┌──────────────┐
│ id (PK)      │           │ id (PK)      │           │ id (PK)      │
│ firstName    │  1 ──→ (FK)│ user_id ──┐  │           │ title        │
│ ...          │           │ property_id ├─────────→ (FK)│ price        │
└──────────────┘           │ createdAt    │  ← N:M    │ ...          │
                           └──────────────┘           └──────────────┘

This is a Many-to-Many relationship implemented as:
- User (1) : Favorite (N) : Property (N)
```

#### Example Data:
```
User: John Smith (ID: 1)
  ├─ Favorite: Villa in Mumbai (Property ID: 101)
  ├─ Favorite: Apartment in Delhi (Property ID: 102)
  └─ Favorite: Cottage in Goa (Property ID: 103)

User: Sarah Jones (ID: 2)
  ├─ Favorite: Penthouse in Bangalore (Property ID: 104)
  └─ Favorite: Villa in Mumbai (Property ID: 101)
```

---

### **RELATIONSHIP 5: USER ↔ SUBSCRIPTION**

#### Type: **ONE-TO-ONE**

#### Explanation:
- **One User** has **Exactly One Subscription**
- **One Subscription** belongs to **Exactly One User**

#### Implementation in Code:

**User.java** (One side - User owns subscription):
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@JsonIgnoreProperties({"user"})
private Subscription subscription;
```

**Subscription.java** (Other side - Subscription belongs to user):
```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false, unique = true)
@JsonIgnoreProperties({"favorites", "properties", "searchHistories", "subscription", "hibernateLazyInitializer", "handler"})
private User user;
```

#### Database Schema:
```
USER TABLE                  SUBSCRIPTION TABLE
┌──────────────────┐       ┌──────────────────┐
│ id (PK)          │       │ id (PK)          │
│ firstName        │  1:1  │ user_id (FK)     │
│ lastName         │ ←───→ │ planType         │
│ email            │       │ startDate        │
│ ...              │       │ endDate          │
└──────────────────┘       │ price            │
                           │ autoRenew        │
                           └──────────────────┘

Foreign Key: subscriptions.user_id → users.id (UNIQUE constraint)
Uniqueness ensures 1:1 relationship
```

#### Why UNIQUE constraint?
The `UNIQUE` constraint on `user_id` ensures:
- Each user can have only ONE subscription
- Each subscription belongs to only ONE user
- This enforces the 1:1 relationship at the database level

#### Example Data:
```
User: John Smith (ID: 1)
  └─ Subscription: PREMIUM
      ├─ Plan Type: PREMIUM
      ├─ Start Date: 2026-01-01
      ├─ End Date: 2026-02-01
      └─ Auto Renew: true

User: Sarah Jones (ID: 2)
  └─ Subscription: FREE
      ├─ Plan Type: FREE
      ├─ Start Date: 2026-01-28
      ├─ End Date: null (no expiry for free plan)
      └─ Auto Renew: false
```

#### SQL Query Perspective:
```sql
-- Get a user's subscription
SELECT s.* FROM subscriptions s
JOIN users u ON s.user_id = u.id
WHERE u.id = 1;

-- Get user with subscription details
SELECT u.*, s.* FROM users u
LEFT JOIN subscriptions s ON u.id = s.user_id
WHERE u.id = 1;
```

---

### **RELATIONSHIP 6: USER ↔ SEARCH_HISTORY**

#### Type: **ONE-TO-MANY** / **MANY-TO-ONE**

#### Explanation:
- **One User** can have **Multiple Search Histories**
- **Many SearchHistories** belong to **One User**

#### Implementation in Code:

**User.java** (One side):
```java
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonIgnoreProperties({"user"})
private List<SearchHistory> searchHistories = new ArrayList<>();
```

#### Example Data:
```
User: John Smith (ID: 1)
  ├─ Search: "apartments in mumbai" (Timestamp: 2026-01-28 10:00)
  ├─ Search: "villas in delhi" (Timestamp: 2026-01-28 11:30)
  └─ Search: "3bhk properties" (Timestamp: 2026-01-28 12:45)
```

---

## 📋 Complete Relationship Summary Table

| Relationship | From | To | Type | Cardinality | Foreign Key Location |
|---|---|---|---|---|---|
| User owns Properties | User | Property | One-to-Many | 1:N | `properties.owner_id` |
| Property belongs to User | Property | User | Many-to-One | N:1 | `properties.owner_id` |
| Property has Images | Property | PropertyImage | One-to-Many | 1:N | `property_images.property_id` |
| Image belongs to Property | PropertyImage | Property | Many-to-One | N:1 | `property_images.property_id` |
| BuilderGroup has Properties | BuilderGroup | Property | One-to-Many | 1:N | `properties.builder_group_id` |
| Property belongs to BuilderGroup | Property | BuilderGroup | Many-to-One | N:1 | `properties.builder_group_id` |
| User has Subscription | User | Subscription | One-to-One | 1:1 | `subscriptions.user_id` (UNIQUE) |
| Subscription belongs to User | Subscription | User | One-to-One | 1:1 | `subscriptions.user_id` (UNIQUE) |
| User has Favorites | User | Favorite | One-to-Many | 1:N | `favorites.user_id` |
| User has SearchHistories | User | SearchHistory | One-to-Many | 1:N | `search_histories.user_id` |

---

## 🎯 Key JPA Annotations Explained

### @OneToMany
```java
@OneToMany(
    mappedBy = "owner",           // Property field name in child entity
    cascade = CascadeType.ALL,    // Cascade operations to child entities
    fetch = FetchType.LAZY,       // Load child entities lazily
    orphanRemoval = true          // Delete child if removed from collection
)
private List<Property> properties;
```

### @ManyToOne
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "owner_id",            // Database column name for foreign key
    nullable = true               // Allow null values
)
private User owner;
```

### @OneToOne
```java
@OneToOne(
    mappedBy = "user",            // Field in other entity
    cascade = CascadeType.ALL,    // Cascade operations
    fetch = FetchType.LAZY        // Lazy loading
)
@JoinColumn(
    name = "user_id",             // Database column name
    nullable = false,             // Must have a value
    unique = true                 // Ensures 1:1 relationship
)
private Subscription subscription;
```

### @JoinColumn
```java
@JoinColumn(
    name = "owner_id",            // Column name in database
    nullable = true,              // Can be null
    unique = false                // Can have duplicates
)
```

---

## 💾 Cascade Options Explained

| Cascade Type | Behavior |
|---|---|
| **CascadeType.ALL** | Cascade ALL operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH) |
| **CascadeType.PERSIST** | Save child when parent is saved |
| **CascadeType.MERGE** | Update child when parent is updated |
| **CascadeType.REMOVE** | Delete child when parent is deleted |
| **CascadeType.REFRESH** | Refresh child when parent is refreshed |

---

## 🔄 Fetch Strategies Explained

| Fetch Type | Behavior | When to Use |
|---|---|---|
| **FetchType.LAZY** | Load associated entities only when accessed | For large collections, performance optimization |
| **FetchType.EAGER** | Load associated entities immediately | For small entities that are always needed |

---

## 📝 Example Operations

### Create a Property with Multiple Images:
```java
Property property = Property.builder()
    .title("Beautiful Villa")
    .price(new BigDecimal("5000000"))
    .owner(user)
    .builderGroup(builderGroup)
    .build();

// Add images
PropertyImage image1 = PropertyImage.builder()
    .imageUrl("villa1.jpg")
    .isPrimary(true)
    .displayOrder(1)
    .build();

property.addImage(image1);  // This sets the property reference in image

propertyRepository.save(property);  // Saves property AND all images due to cascade
```

### Create User with Subscription:
```java
User user = User.builder()
    .firstName("John")
    .lastName("Smith")
    .email("john@example.com")
    .build();

Subscription subscription = Subscription.builder()
    .user(user)
    .planType(SubscriptionType.PREMIUM)
    .startDate(LocalDate.now())
    .price(new BigDecimal("999"))
    .build();

userRepository.save(user);  // Saves user AND subscription due to cascade
```

### Add Favorite:
```java
Favorite favorite = Favorite.builder()
    .user(user)
    .property(property)
    .notes("Great property for future investment")
    .build();

favoriteRepository.save(favorite);
```

---

## ✅ Summary

### One-to-Many/Many-to-One:
- **One entity** has **collection** of another entity
- **Multiple entities** each **belong to** one entity
- **Implemented with**: `@OneToMany` on parent, `@ManyToOne` on child
- **Foreign Key**: In the child table

### One-to-One:
- **Each entity** is **uniquely associated** with the other
- **Implemented with**: `@OneToOne` on both sides
- **Foreign Key**: On either side (typically where `mappedBy` is NOT used)
- **Constraint**: `UNIQUE` on foreign key to ensure 1:1

### Real Estate App Context:
- Users manage multiple properties (1:N)
- Properties have multiple images (1:N)
- Users have one subscription (1:1)
- Users can favorite multiple properties (N:M via Favorite junction entity)
- Properties belong to builder groups (N:1)

