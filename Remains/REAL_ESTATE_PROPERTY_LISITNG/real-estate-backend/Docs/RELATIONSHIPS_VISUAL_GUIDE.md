# Visual Entity Relationship Reference - All 6 Relationships

## 1️⃣ USER ↔ PROPERTY (One-to-Many)

```
CONCEPT:
One user can own multiple properties
Each property has exactly one owner

CODE STRUCTURE:
┌─────────────────────────────────────┐
│         USER (Parent)               │
├─────────────────────────────────────┤
│ @OneToMany(mappedBy="owner")        │
│ List<Property> properties           │
└─────────────────────────────────────┘
                │
              1 to N
                │
┌─────────────────────────────────────┐
│      PROPERTY (Child)               │
├─────────────────────────────────────┤
│ @ManyToOne                          │
│ @JoinColumn(name="owner_id")        │
│ User owner                          │
└─────────────────────────────────────┘

DATABASE:
USERS                    PROPERTIES
┌────┐                   ┌────┬────────────┐
│ id │ ◄────────────── │ id │ owner_id   │
│ 1  │                │ 1  │ 1          │
│ 2  │                │ 2  │ 1          │
│ 3  │                │ 3  │ 2          │
└────┘                │ 4  │ 3          │
                      └────┴────────────┘

REAL DATA:
John Smith (User ID: 1)
  ├─ Villa Mumbai (Property ID: 1, owner_id: 1)
  ├─ Apartment Delhi (Property ID: 2, owner_id: 1)
  └─ Penthouse Bangalore (Property ID: 3, owner_id: 1)

Sarah Jones (User ID: 2)
  └─ Cottage Goa (Property ID: 4, owner_id: 2)
```

---

## 2️⃣ PROPERTY ↔ PROPERTY_IMAGE (One-to-Many)

```
CONCEPT:
One property can have multiple images
Each image belongs to exactly one property

CODE STRUCTURE:
┌──────────────────────────────────┐
│     PROPERTY (Parent)            │
├──────────────────────────────────┤
│ @OneToMany(mappedBy="property")  │
│ orphanRemoval=true               │
│ List<PropertyImage> images       │
└──────────────────────────────────┘
                │
              1 to N
                │
┌──────────────────────────────────┐
│   PROPERTY_IMAGE (Child)         │
├──────────────────────────────────┤
│ @ManyToOne                       │
│ @JoinColumn(name="property_id")  │
│ Property property                │
└──────────────────────────────────┘

DATABASE:
PROPERTIES              PROPERTY_IMAGES
┌────┐                  ┌────┬─────────┐
│ id │ ◄─────────────── │ id │ prop_id │
│    │                  │ 1  │ 1       │
│ 1  │    1 ──→ N       │ 2  │ 1       │
│    │                  │ 3  │ 1       │
│ 2  │                  │ 4  │ 1       │
└────┘                  │ 5  │ 2       │
                        │ 6  │ 2       │
                        └────┴─────────┘

REAL DATA:
Property: Villa Mumbai (ID: 1)
  ├─ villa_front.jpg (Image ID: 1, prop_id: 1, isPrimary: true)
  ├─ villa_side.jpg (Image ID: 2, prop_id: 1)
  ├─ villa_pool.jpg (Image ID: 3, prop_id: 1)
  └─ villa_interior.jpg (Image ID: 4, prop_id: 1)

Property: Apartment Delhi (ID: 2)
  ├─ apt_front.jpg (Image ID: 5, prop_id: 2, isPrimary: true)
  └─ apt_kitchen.jpg (Image ID: 6, prop_id: 2)

KEY FEATURE: orphanRemoval = true
Delete image from collection → Image deleted from DB automatically
```

---

## 3️⃣ BUILDER_GROUP ↔ PROPERTY (One-to-Many)

```
CONCEPT:
One builder group develops multiple properties
Each property is built by one builder group

CODE STRUCTURE:
┌──────────────────────────────┐
│  BUILDER_GROUP (Parent)      │
├──────────────────────────────┤
│ @OneToMany(mappedBy=...)     │
│ List<Property> properties    │
└──────────────────────────────┘
                │
              1 to N
                │
┌──────────────────────────────┐
│    PROPERTY (Child)          │
├──────────────────────────────┤
│ @ManyToOne                   │
│ @JoinColumn(name=...)        │
│ BuilderGroup builderGroup    │
└──────────────────────────────┘

DATABASE:
BUILDER_GROUPS          PROPERTIES
┌────┐                  ┌────┬─────┐
│ id │ ◄──────────────── │ id │ bg_ │
│ 1  │     1 ──→ N       │ 1  │ 1   │
│ 2  │                  │ 2  │ 1   │
│ 3  │                  │ 3  │ 2   │
└────┘                  │ 4  │ 2   │
                        │ 5  │ 3   │
                        └────┴─────┘

REAL DATA:
Shapoorji Pallonji (Builder Group ID: 1)
  ├─ Premium Tower Mumbai (Property ID: 1, bg_id: 1)
  ├─ Grandeur Bangalore (Property ID: 2, bg_id: 1)
  └─ Elite Delhi (Property ID: 3, bg_id: 1)

TCG (Builder Group ID: 2)
  ├─ Vision Heights Pune (Property ID: 4, bg_id: 2)
  └─ Essence Gurgaon (Property ID: 5, bg_id: 2)
```

---

## 4️⃣ USER ↔ SUBSCRIPTION (One-to-One) ⭐ SPECIAL

```
CONCEPT:
One user has exactly ONE subscription
One subscription belongs to exactly ONE user
↓
UNIQUE constraint on foreign key enforces this!

CODE STRUCTURE:
┌──────────────────────────────┐
│    USER (Inverse Side)       │
├──────────────────────────────┤
│ @OneToOne(mappedBy="user")   │
│ Subscription subscription    │
└──────────────────────────────┘
                ↕
            1 to 1
            (UNIQUE!)
                ↕
┌──────────────────────────────┐
│  SUBSCRIPTION (Owning Side)  │
├──────────────────────────────┤
│ @OneToOne                    │
│ @JoinColumn(               │
│   name="user_id",          │
│   unique=true ← KEY!       │
│ )                          │
│ User user                  │
└──────────────────────────────┘

DATABASE:
USERS                SUBSCRIPTIONS
┌────┐              ┌────┬────────────┐
│ id │ ◄──1:1──── │ id │ user_id    │
│ 1  │    UNIQUE  │ 1  │ 1 (UQ)     │
│ 2  │    FK      │ 2  │ 2 (UQ)     │
│ 3  │            │ 3  │ 3 (UQ)     │
│ 4  │            │    │            │
└────┘            └────┴────────────┘

WHY UNIQUE CONSTRAINT?

❌ Without UNIQUE (allows duplicates):
┌────┬────────┐
│ id │ user_id│
├────┼────────┤
│ 1  │ 1      │  ← User 1
│ 2  │ 1      │  ← User 1 AGAIN (Not allowed!)
│ 3  │ 2      │
└────┴────────┘

✅ With UNIQUE (prevents duplicates):
┌────┬────────┐
│ id │ user_id│  UNIQUE constraint prevents
├────┼────────┤  user_id = 1 appearing twice
│ 1  │ 1      │
│ 2  │ 2      │
│ 3  │ 3      │
└────┴────────┘

REAL DATA:
User: John Smith (ID: 1)
  ↕ 1:1 (UNIQUE)
Subscription: PREMIUM (ID: 1, user_id: 1, unique)
  • Plan Type: PREMIUM
  • Price: ₹999
  • Auto Renew: true

User: Sarah Jones (ID: 2)
  ↕ 1:1 (UNIQUE)
Subscription: FREE (ID: 2, user_id: 2, unique)
  • Plan Type: FREE
  • Price: ₹0
  • Auto Renew: false
```

---

## 5️⃣ USER → FAVORITE (Many-to-Many via Junction Table)

```
CONCEPT:
One user can favorite MANY properties
One property can be favorited by MANY users
↓
Many-to-Many relationship via FAVORITE junction table

CODE STRUCTURE:
┌─────────────────────────────┐
│      USER (Side A)          │
├─────────────────────────────┤
│ @OneToMany(mappedBy="user") │
│ List<Favorite> favorites    │
└─────────────────────────────┘
            │
           1:N
            │
┌─────────────────────────────┐
│   FAVORITE (Junction)       │
├─────────────────────────────┤
│ @ManyToOne → User           │
│ @ManyToOne → Property       │
│ @UniqueConstraint           │
└─────────────────────────────┘
            │
           N:1
            │
┌─────────────────────────────┐
│    PROPERTY (Side B)        │
├─────────────────────────────┤
│ @ManyToOne (implicit)       │
└─────────────────────────────┘

DATABASE:
USERS           FAVORITES              PROPERTIES
┌────┐          ┌────┬──────┬─────┐   ┌────┐
│ id │◄────────│ id │usr_id│prop_│───│ id │
│ 1  │  1:N    │ 1  │ 1    │ 101 │   │101 │
│ 2  │         │ 2  │ 1    │ 102 │   │102 │
│ 3  │         │ 3  │ 1    │ 103 │   │103 │
└────┘         │ 4  │ 2    │ 101 │   │104 │
               │ 5  │ 2    │ 104 │   └────┘
               └────┴──────┴─────┘

UNIQUE(user_id, property_id) prevents:
User 1 from favoriting Property 101 twice

REAL DATA:
User 1: John Smith
  ├─ Favorited Property 101 (Villa Mumbai) → Favorite ID: 1
  ├─ Favorited Property 102 (Apartment Delhi) → Favorite ID: 2
  └─ Favorited Property 103 (Cottage Goa) → Favorite ID: 3

User 2: Sarah Jones
  ├─ Favorited Property 101 (Villa Mumbai) → Favorite ID: 4 ← Same as John!
  └─ Favorited Property 104 (Penthouse) → Favorite ID: 5

Property 101 favorited by: Users 1, 2 (2 people)
```

---

## 6️⃣ USER ↔ SEARCH_HISTORY (One-to-Many)

```
CONCEPT:
One user performs multiple searches
Each search belongs to exactly one user

CODE STRUCTURE:
┌─────────────────────────────────┐
│      USER (Parent)              │
├─────────────────────────────────┤
│ @OneToMany(mappedBy="user")     │
│ List<SearchHistory> searches    │
└─────────────────────────────────┘
                │
              1 to N
                │
┌─────────────────────────────────┐
│   SEARCH_HISTORY (Child)        │
├─────────────────────────────────┤
│ @ManyToOne                      │
│ @JoinColumn(name="user_id")     │
│ User user                       │
└─────────────────────────────────┘

DATABASE:
USERS                SEARCH_HISTORIES
┌────┐              ┌────┬────────┐
│ id │◄───────────│ id │ user_id│
│ 1  │    1:N     │ 1  │ 1      │
│ 2  │            │ 2  │ 1      │
│ 3  │            │ 3  │ 1      │
└────┘            │ 4  │ 2      │
                  │ 5  │ 2      │
                  └────┴────────┘

REAL DATA:
User: John Smith (ID: 1)
  ├─ Search: "apartments in mumbai" (Search ID: 1, user_id: 1)
  ├─ Search: "villas in delhi" (Search ID: 2, user_id: 1)
  └─ Search: "3bhk properties" (Search ID: 3, user_id: 1)

User: Sarah Jones (ID: 2)
  ├─ Search: "luxury apartments" (Search ID: 4, user_id: 2)
  └─ Search: "beachfront properties" (Search ID: 5, user_id: 2)
```

---

## 📊 All 6 Relationships at a Glance

```
┌────────────────────────────────────────────────────────────┐
│              YOUR PROJECT'S 6 RELATIONSHIPS                │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  1. USER → PROPERTY (1:N)                                │
│     └─ One user owns many properties                     │
│                                                          │
│  2. PROPERTY → PROPERTY_IMAGE (1:N)                      │
│     └─ One property has many images                      │
│                                                          │
│  3. BUILDER_GROUP → PROPERTY (1:N)                       │
│     └─ One builder group has many properties             │
│                                                          │
│  4. USER ↔ SUBSCRIPTION (1:1) ⭐                         │
│     └─ One user has one subscription (UNIQUE FK)         │
│                                                          │
│  5. USER → FAVORITE (1:N via junction)                   │
│     └─ Many-to-Many: Users favorite Properties           │
│                                                          │
│  6. USER → SEARCH_HISTORY (1:N)                          │
│     └─ One user performs many searches                   │
│                                                          │
└────────────────────────────────────────────────────────────┘

COMPLETE DATA MODEL:

                    ┌──────────┐
                    │  USERS   │
                    └──────────┘
                    /    │    \    \
                   /     │     \    \
                1:N    1:N    1:N   1:1
               /       |      \     \
        ┌──────────┐  │   ┌──────────┐  ┌──────────────┐
        │PROPERTIES├──┤   │SEARCH    │  │SUBSCRIPTIONS │
        │          │  │   │HISTORY   │  │              │
        └──────────┘  │   └──────────┘  └──────────────┘
              │       │
             1:N     1:N via
              │      junction
              │       │
       ┌──────────┐  ┌────────────┐
       │PROPERTY  │  │ FAVORITES  │ ← N:M relationship
       │IMAGES    │  └────────────┘
       └──────────┘

        ┌──────────────┐
        │BUILDER_GROUPS│
        └──────────────┘
              │
             1:N
              │
        PROPERTIES
```

---

## 🎯 Key Points Summary

| # | Relationship | Type | Parent Has | Child Has | FK | Special |
|---|---|---|---|---|---|---|
| 1 | USER→PROP | 1:N | List | Single | prop.owner_id | — |
| 2 | PROP→IMAGE | 1:N | List | Single | image.prop_id | orphanRemoval |
| 3 | BG→PROP | 1:N | List | Single | prop.bg_id | — |
| 4 | USER↔SUBS | 1:1 | Single | Single | subs.user_id | **UNIQUE** |
| 5 | USER→FAV | 1:N | List | Single(junction) | fav.user_id | N:M via junction |
| 6 | USER→SEARCH | 1:N | List | Single | search.user_id | — |

