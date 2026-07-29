# 🤍 FAVORITES DEFAULT STATE - QUICK GUIDE

## The Problem
Properties showing as "Favorited ❤️" by default instead of "Not Favorited 🤍"

## The Solution
Frontend must check favorite status and display correct state

---

## What You Need to Do (Frontend)

### 1. Create FavoriteButton Component
```jsx
// On load: Check if favorited
// By default: isFavorited = false ✅
// Display: 🤍 "Add to Favorites" (not favorited by default)

// On click: Toggle favorite
// Display: ❤️ "Favorited" (only if user clicked)
```

### 2. Call These Endpoints

**Check Status** (on component mount):
```
GET /api/favorites/check?userId=1&propertyId=1
Response: {"isFavorited": false}  // Default
```

**Toggle Favorite** (on button click):
```
POST /api/favorites/toggle?userId=1&propertyId=1
Response: {"isFavorited": true}   // Added
   OR    {"isFavorited": false}   // Removed
```

### 3. Update UI Based on State
```javascript
if (isFavorited) {
  display: "❤️ Favorited"     // Filled heart
} else {
  display: "🤍 Add to Favorites" // Unfilled heart
}
```

---

## Expected Behavior

**Page Load**
```
→ Check endpoint
→ Backend returns: false
→ Display: 🤍 "Add to Favorites"
```

**User Clicks**
```
→ Toggle endpoint
→ Backend adds favorite
→ Display: ❤️ "Favorited"
```

**Page Refresh**
```
→ Check endpoint
→ Backend returns: true (saved in DB)
→ Display: ❤️ "Favorited" ✅
```

**User Clicks Again**
```
→ Toggle endpoint
→ Backend removes favorite
→ Display: 🤍 "Add to Favorites"
```

---

## Backend Status
✅ Already working correctly
✅ Returns false by default
✅ Persists to database
✅ Check endpoint ready
✅ Toggle endpoint ready

## Frontend Status
🔄 Needs implementation
  → Add favorite button component
  → Call check endpoint on mount
  → Display default state (not favorited)
  → Handle toggle on click

---

## Code Template (React)

```jsx
const [isFavorited, setIsFavorited] = useState(false); // DEFAULT: NOT favorited

useEffect(() => {
  // Check status on load
  checkFavoriteStatus();
}, []);

const checkFavoriteStatus = async () => {
  const res = await axios.get('/api/favorites/check', {
    params: { userId, propertyId }
  });
  // Set based on backend response
  setIsFavorited(res.data.data.isFavorited);
};

const toggleFavorite = async () => {
  const res = await axios.post('/api/favorites/toggle', {}, {
    params: { userId, propertyId }
  });
  // Toggle based on response (null means removed)
  setIsFavorited(res.data.data !== null);
};

// Display
<button onClick={toggleFavorite}>
  {isFavorited ? '❤️ Favorited' : '🤍 Add to Favorites'}
</button>
```

---

## Summary

✅ Backend: Returns `false` by default (NOT favorited)
✅ Endpoint: `/api/favorites/check` tells us current status
✅ Default: Show 🤍 "Add to Favorites" when isFavorited = false
✅ On Click: Toggle and update state
✅ Persist: Data stays in database on refresh

---

**See**: FAVORITES_FRONTEND_IMPLEMENTATION.md for complete examples
