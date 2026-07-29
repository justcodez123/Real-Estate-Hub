# Real Estate Application - JPA Relationships Quick Reference Guide

## 📚 Three Types of Relationships in JPA

---

## 1️⃣ ONE-TO-MANY / MANY-TO-ONE Relationships

### What It Means:
- **One entity** can be associated with **multiple instances** of another entity
- **Many entities** belong to **one entity**
- These are the **same relationship** viewed from different perspectives

### In Simple Terms:
> "One parent can have many children, but each child has only one parent"

### In Real Estate Context:
```
One User owns Many Properties
→ User is the "one" side (parent)
→ Property is the "many" side (child)
```

### Code Example:

**User.java (The "One" side - Parent)**
```java
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonIgnoreProperties({"owner"})
private List<Property> properties = new ArrayList<>();
```

**Property.java (The "Many" side - Child)**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "owner_id", nullable = true)
@JsonIgnoreProperties({"properties"})
private User owner;
```

### Database Impact:
```
TABLE: users              TABLE: properties
┌──────────┐            ┌──────────────────┐
│ id (1)   │  1───────→ │ owner_id (FK) → │
│ name     │            │ Many records     │
└──────────┘            └──────────────────┘

Foreign Key location: CHILD table (properties.owner_id)
```

### Cascade Behavior:
- **CascadeType.ALL**: When user is saved/updated/deleted, properties follow
- **orphanRemoval**: When property removed from list, it's deleted from DB

### Real Examples in Your App:

#### 1. USER → PROPERTY
```
John Smith owns:
  • Villa Mumbai
  • Apartment Delhi  
  • Penthouse Bangalore
```

#### 2. PROPERTY → PROPERTY_IMAGE
```
Villa Mumbai has images:
  • villa_front.jpg
  • villa_side.jpg
  • villa_pool.jpg
  • villa_interior.jpg
```

#### 3. BUILDER_GROUP → PROPERTY
```
Shapoorji Pallonji has properties:
  • Premium Tower Mumbai
  • Grandeur Bangalore
  • Elite Delhi
```

#### 4. USER → FAVORITE
```
John Smith favorites:
  • Villa Mumbai
  • Apartment Delhi
  • Cottage Goa
```

#### 5. USER → SEARCH_HISTORY
```
John Smith searched for:
  • "apartments in mumbai"
  • "villas in delhi"
  • "3bhk properties"
```

---

## 2️⃣ ONE-TO-ONE Relationships

### What It Means:
- **Each entity** is associated with **exactly one** instance of another entity
- Works both ways: A has exactly one B, and B has exactly one A

### In Simple Terms:
> "Person has exactly one passport, and each passport belongs to exactly one person"

### In Real Estate Context:
```
One User has One Subscription
→ Each user can have only one active subscription
→ Each subscription belongs to only one user
```

### Code Example:

**User.java (One side)**
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@JsonIgnoreProperties({"user"})
private Subscription subscription;
```

**Subscription.java (Other side)**
```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false, unique = true)
@JsonIgnoreProperties({"subscription"})
private User user;
```

### Why UNIQUE Constraint?
The `unique = true` on the foreign key ensures:
```
subscriptions table:
┌─────────────────────┐
│ user_id (FK, UNIQUE)│  ← Each user_id appears only ONCE
│ planType            │  ← This forces 1:1 relationship
│ startDate           │
└─────────────────────┘

Examples:
user_id: 1 → can appear only once
user_id: 2 → can appear only once
user_id: 3 → cannot appear twice

This prevents: User 1 having 2 subscriptions
```

### Database Impact:
```
TABLE: users              TABLE: subscriptions
┌──────────┐            ┌─────────────────────┐
│ id (1)   │  1──1:1──→ │ user_id (FK,UNIQUE) │
│ name     │            │ planType            │
└──────────┘            └─────────────────────┘

Key difference: UNIQUE constraint on foreign key
```

### Real Example:
```
User: John Smith (ID: 1)
  ↓
Subscription: PREMIUM
  • Plan: PREMIUM
  • Start Date: 2026-01-01
  • End Date: 2026-02-01
  • Price: ₹999
  • Auto Renew: true

User: Sarah Jones (ID: 2)
  ↓
Subscription: FREE
  • Plan: FREE
  • Start Date: 2026-01-28
  • Price: ₹0
```

---

## 🔄 ONE-TO-MANY vs MANY-TO-ONE vs ONE-TO-ONE

### Quick Comparison Table:

| Aspect | One-to-Many | Many-to-One | One-to-One |
|--------|-------------|------------|-----------|
| **Parent Cards** | 1 | Many | 1 |
| **Child Cards** | Many | 1 | 1 |
| **Cardinality** | 1:N | N:1 | 1:1 |
| **Same Relationship?** | Yes, inverse of M:1 | Yes, inverse of 1:N | No, unique case |
| **Foreign Key Location** | Child table | Child table | Either table |
| **Unique Constraint** | No | No | YES (on FK) |
| **Collection Type** | `List<>` | Single object | Single object |
| **Example** | User has Properties | Properties have User | User has Subscription |

### Key Insight:
```
One-to-Many and Many-to-One are the SAME relationship!

FROM USER PERSPECTIVE:    FROM PROPERTY PERSPECTIVE:
"I have many properties"  "I belong to one user"
   ↓                           ↓
One-to-Many             Many-to-One
   ↓                           ↓
@OneToMany              @ManyToOne
@JoinColumn             @JoinColumn

Both point to SAME foreign key:
properties.owner_id → users.id
```

---

## 📊 All Relationships in Your Project

```
Relationship#  | From        | To              | Type  | JPA Annotation
---------------|-------------|-----------------|-------|------------------
1              | User        | Property        | 1:N   | @OneToMany/@ManyToOne
2              | Property    | PropertyImage   | 1:N   | @OneToMany/@ManyToOne
3              | BuilderGroup| Property        | 1:N   | @OneToMany/@ManyToOne
4              | User        | Subscription    | 1:1   | @OneToOne
5              | User        | Favorite        | 1:N   | @OneToMany/@ManyToOne
6              | User        | SearchHistory   | 1:N   | @OneToMany/@ManyToOne

Note: Relationships 5 and 6 form a Many-to-Many through junction tables
```

---

## 🎯 Key JPA Annotations Explained

### @OneToMany
```java
@OneToMany(
    mappedBy = "owner",           // Field name in child entity
    cascade = CascadeType.ALL,    // Cascade operations
    fetch = FetchType.LAZY,       // Load when needed
    orphanRemoval = true          // Delete if removed from list
)
private List<Property> properties = new ArrayList<>();
```
**Meaning**: "This entity has many of those entities"

### @ManyToOne
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "owner_id",            // Foreign key column name
    nullable = true               // Can be empty
)
private User owner;
```
**Meaning**: "Many of these belong to one of those"

### @OneToOne
```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "user_id",
    nullable = false,
    unique = true                 // CRITICAL: Enforces 1:1
)
private User user;
```
**Meaning**: "This entity has exactly one of that entity"

### @JoinColumn
```java
@JoinColumn(
    name = "owner_id",            // Column name in database
    nullable = true,              // Can have NULL values
    unique = false                // Can have duplicates
)
```
**Meaning**: Specifies foreign key column details

---

## 💡 When to Use Each

### Use One-to-Many When:
- ✅ One entity owns/contains multiple entities
- ✅ Parent-child relationships
- ✅ Users have properties, properties have images
- ✅ One-many association exists

### Use Many-to-One When:
- ✅ From the child entity perspective
- ✅ Many entities reference one parent
- ✅ Property's owner reference
- ✅ Storing the reference side

### Use One-to-One When:
- ✅ Exactly one-to-one relationship exists
- ✅ Each side has exactly one of the other
- ✅ User to Subscription relationship
- ✅ Personal information to detailed profile

---

## 🔄 Example Operations

### Create and Save with Relationships:

**One-to-Many Example:**
```java
// Create user and add properties
User user = User.builder()
    .firstName("John")
    .lastName("Smith")
    .email("john@example.com")
    .build();

// Create property
Property property = Property.builder()
    .title("Villa Mumbai")
    .price(new BigDecimal("5000000"))
    .owner(user)  // Link property to user
    .build();

// Add to collection
user.getProperties().add(property);

// Save - will save user AND property due to cascade
userRepository.save(user);
```

**One-to-One Example:**
```java
// Create user with subscription
User user = User.builder()
    .firstName("Sarah")
    .lastName("Jones")
    .email("sarah@example.com")
    .build();

Subscription subscription = Subscription.builder()
    .user(user)  // Link subscription to user
    .planType(SubscriptionType.PREMIUM)
    .startDate(LocalDate.now())
    .build();

// Link back
user.setSubscription(subscription);

// Save
userRepository.save(user);
```

---

## 🚀 Best Practices

1. **Always use FetchType.LAZY** (avoid loading unnecessary data)
   ```java
   @OneToMany(fetch = FetchType.LAZY)  // ✅ Good
   @OneToMany(fetch = FetchType.EAGER) // ❌ Avoid unless necessary
   ```

2. **Use Cascade wisely** (prevent orphaned records)
   ```java
   @OneToMany(cascade = CascadeType.ALL)  // ✅ Delete children when parent deleted
   @OneToMany(cascade = {})               // ❌ Orphaned records remain
   ```

3. **Use orphanRemoval for true one-to-many** (remove when removed from collection)
   ```java
   @OneToMany(orphanRemoval = true)  // ✅ Image deleted when removed from property
   ```

4. **Always use UNIQUE for One-to-One** (enforce relationship at DB level)
   ```java
   @OneToOne
   @JoinColumn(unique = true)  // ✅ Prevents duplicate relationships
   ```

5. **Add helper methods for bidirectional relationships**:
   ```java
   public void addImage(PropertyImage image) {
       images.add(image);
       image.setProperty(this);  // Maintain both sides
   }
   ```

---

## 📝 Summary

| Type | Definition | Example | Collection |
|------|-----------|---------|-----------|
| **One-to-Many** | 1 entity → Many entities | User → Properties | `List<Property>` |
| **Many-to-One** | Many entities → 1 entity | Properties → User | Single object |
| **One-to-One** | 1 entity ↔ 1 entity | User ↔ Subscription | Single object |

**Remember**: One-to-Many and Many-to-One are the **same relationship** from different perspectives!

