# JPA Relationships - Interview & Exam Quick Reference

## ⚡ Super Quick Answers

### Q: What are the 3 types of JPA relationships?
**A:** 
1. **One-to-Many (1:N)** - One parent has many children
2. **Many-to-One (N:1)** - Many children belong to one parent  
3. **One-to-One (1:1)** - Each entity uniquely associated

### Q: Are One-to-Many and Many-to-One the same?
**A:** YES! Same relationship, different perspective.
- One-to-Many: Parent perspective ("I have many children")
- Many-to-One: Child perspective ("I belong to one parent")

### Q: Where does the Foreign Key go?
**A:** Always on the **MANY side** (Child table)
```
User → Property: FK goes in PROPERTY table (properties.owner_id)
Property → Image: FK goes in IMAGE table (property_images.property_id)
```

### Q: What makes One-to-One different?
**A:** The **UNIQUE constraint** on the foreign key!
```java
@OneToOne
@JoinColumn(name = "user_id", unique = true)  // ← UNIQUE is key!
private User user;
```

### Q: What does `mappedBy` mean?
**A:** It tells which field on the OPPOSITE side owns the relationship.
```java
@OneToMany(mappedBy = "owner")  // owner field in Property owns this relationship
```

### Q: What's the difference between FetchType.LAZY and EAGER?
**A:** 
- **LAZY**: Load child data only when accessed (Better performance)
- **EAGER**: Load child data immediately with parent (Use sparingly)

---

## 📋 Real Estate Project Relationship Cheat Sheet

| # | Relationship | From | To | Type | Code |
|---|---|---|---|---|---|
| 1 | User owns Properties | User | Property | 1:N | `@OneToMany` / `@ManyToOne` |
| 2 | Property has Images | Property | PropertyImage | 1:N | `@OneToMany` / `@ManyToOne` |
| 3 | Builder has Properties | BuilderGroup | Property | 1:N | `@OneToMany` / `@ManyToOne` |
| 4 | User has Subscription | User | Subscription | 1:1 | `@OneToOne` (unique=true) |
| 5 | User favorites Properties | User | Favorite | 1:N | `@OneToMany` (junction) |
| 6 | User searches | User | SearchHistory | 1:N | `@OneToMany` / `@ManyToOne` |

---

## 🎯 Code Templates

### Template 1: One-to-Many

```java
// PARENT SIDE
@Entity
public class User {
    @OneToMany(
        mappedBy = "owner",              // Field name in child
        cascade = CascadeType.ALL,       // Propagate changes
        fetch = FetchType.LAZY           // Load on demand
    )
    private List<Property> properties = new ArrayList<>();
}

// CHILD SIDE
@Entity
public class Property {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "owner_id",               // Column name in DB
        nullable = true                  // Can be empty
    )
    private User owner;
}
```

### Template 2: One-to-One

```java
// OWNING SIDE (has the FK)
@Entity
public class Subscription {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true                    // CRITICAL FOR 1:1
    )
    private User user;
}

// INVERSE SIDE (mapped from other side)
@Entity
public class User {
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Subscription subscription;
}
```

### Template 3: Many-to-Many (via Junction)

```java
// PARENT SIDE 1
@Entity
public class User {
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Favorite> favorites = new ArrayList<>();
}

// JUNCTION TABLE
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "property_id"}
    )
)
public class Favorite {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;
}
```

---

## 💡 Common Interview Questions & Answers

### Q1: What is the difference between @OneToMany and @ManyToOne?
```
@OneToMany:
- Used on parent/owner side
- Represents collection of children
- @OneToMany(mappedBy = "owner")
- Collection type: List<Child>

@ManyToOne:
- Used on child side
- Represents single parent
- @ManyToOne @JoinColumn(name = "owner_id")
- Single object type: Parent
```

### Q2: Why do we use `mappedBy`?
```
It tells Spring who owns the relationship.

Example:
User has @OneToMany(mappedBy = "owner")
Property has @ManyToOne @JoinColumn(name = "owner_id")

The "owner" field in Property is the ACTUAL owner of relationship.
The @OneToMany just uses mappedBy to reference it.

Why? To avoid creating two foreign keys pointing each other.
```

### Q3: What happens with CascadeType.ALL?
```
When parent is affected, child follows:

DELETE parent → DELETE all children
SAVE parent → SAVE all children  
UPDATE parent → UPDATE all children
MERGE parent → MERGE all children
REFRESH parent → REFRESH all children
DETACH parent → DETACH all children

Careful! Can cause unexpected deletions!
```

### Q4: What is orphanRemoval?
```
When you remove a child from parent's collection,
child is deleted from database.

Example:
property.removeImage(image);  // Remove from list
imageRepository.save(...);    // Image is deleted!

Usually paired with cascade = CascadeType.ALL
```

### Q5: How to enforce One-to-One relationship?
```
Use UNIQUE constraint on Foreign Key:

@OneToOne
@JoinColumn(name = "user_id", unique = true)
         ↑
    This is CRITICAL!

What it does:
- subscriptions.user_id must be UNIQUE
- Prevents user_id = 1 appearing twice
- Enforces 1:1 at database level
```

### Q6: Lazy vs Eager Loading?
```
LAZY (Default, Better):
- Load child only when accessed
- user.getProperties()  ← Triggers query here
- Better performance
- Prevents N+1 problem

EAGER (Avoid):
- Load child immediately with parent
- SELECT users, properties at same time
- More data loaded upfront
- Can be inefficient
```

### Q7: What's a junction table?
```
For Many-to-Many relationships:

User (many) ↔ Property (many)

Create junction table:
FAVORITE table:
┌─────────┬──────────────┐
│ user_id │ property_id  │
├─────────┼──────────────┤
│ 1       │ 101          │
│ 1       │ 102          │
│ 2       │ 101          │
│ 2       │ 103          │
└─────────┴──────────────┘

Result:
- User 1 favorited properties 101, 102
- User 2 favorited properties 101, 103
- Property 101 favorited by users 1, 2
```

### Q8: How to fetch relationships?
```
Lazy Loading:
User user = userRepository.findById(1L).get();
List<Property> props = user.getProperties();  // Query here

Eager Loading (configured in entity):
@OneToMany(fetch = FetchType.EAGER)

EntityGraph (dynamic):
@EntityGraph(attributePaths = {"properties"})
User findWithProperties(Long id);
```

---

## 🔍 Troubleshooting Common Issues

### Issue 1: LazyInitializationException
```
Error: "failed to lazily initialize a collection"

Cause: Accessing lazy relationship after session closed

Solutions:
- Use @Transactional on method
- Set fetch = FetchType.EAGER (not recommended)
- Use EntityGraph to specify loading
- Manually load in query: left join fetch
```

### Issue 2: Stack Overflow (Infinite Recursion)
```
Error: "Infinite recursion in JSON serialization"

Cause: User has properties, Property has owner (User)
       JSON tries to serialize both forever

Solutions:
- Add @JsonIgnoreProperties({"owner"}) on User
- Add @JsonIgnoreProperties({"properties"}) on Property
- Use DTOs instead of entities
```

### Issue 3: Constraint Violation
```
Error: "Unique constraint violation"

If One-to-One without unique = true:

@OneToOne
@JoinColumn(name = "user_id")  // ← Missing unique = true!
private User user;

Fix:
@OneToOne
@JoinColumn(name = "user_id", unique = true)
```

### Issue 4: Orphaned Records
```
Error: "Records still exist but not referenced"

Cause: Removed from collection but didn't delete

Solution:
@OneToMany(orphanRemoval = true)  // Add this!
```

---

## 📊 Quick Comparison Matrix

```
Aspect              1:N/N:1        1:1           N:M
─────────────────────────────────────────────────────
Parent Collection   List<Child>    Single        List<Junction>
Child Type          Single         Single        Junction entity
Foreign Key         Child table    Either        Junction table
Unique FK           No             YES           No (composite)
mappedBy            Yes            Yes           Yes (both sides)
Cascade             Usually        Maybe         Usually
orphanRemoval       Maybe          No            Via junction
Example             User→Property  User→Subs     User↔Props
```

---

## ✅ Pre-Interview Checklist

- [ ] Know 3 relationship types by name
- [ ] Understand One-to-Many IS Many-to-One (different view)
- [ ] Foreign key always on MANY side
- [ ] One-to-One needs UNIQUE constraint
- [ ] `mappedBy` points to owning field
- [ ] FetchType.LAZY vs EAGER differences
- [ ] Cascade vs Orphan Removal
- [ ] Can code each template from memory
- [ ] Understand your project's 6 relationships
- [ ] Know common errors and solutions

---

## 🎓 Final Quick Reference

```
Q: How many types of relationships?
A: 3 (1:N, N:1, 1:1)

Q: Are 1:N and N:1 different?
A: No, same relationship, different sides

Q: Where is FK?
A: MANY side (child table)

Q: What makes 1:1 special?
A: UNIQUE constraint on FK

Q: Best practice fetch type?
A: LAZY (load on demand)

Q: What does orphanRemoval do?
A: Delete child when removed from collection

Q: Your project has how many relationships?
A: 6 (User→Property, Property→Image, BuilderGroup→Property,
      User↔Subscription, User→Favorite, User→SearchHistory)

Q: Which is 1:1?
A: User↔Subscription (with unique = true)
```

