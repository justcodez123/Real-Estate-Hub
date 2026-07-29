# ✅ FIX: 400 ERROR WHEN REMOVING FAVORITES

## Problem
Getting 400 error when trying to remove a favorite

## Root Cause
Frontend `handleRemoveFavorite` function is likely sending DELETE request incorrectly

## Solution

The backend expects ONE of these two methods:

### Method 1: Remove by User + Property IDs (Recommended)
```
DELETE /api/favorites?userId=1&propertyId=5
```

### Method 2: Remove by Favorite ID
```
DELETE /api/favorites/1
```

---

## Fix Your Frontend Code

### Current (Broken) Code
Your Favorites.js:82 is probably doing something like:
```javascript
// ❌ WRONG - Missing parameters
const handleRemoveFavorite = async (favoriteId) => {
  try {
    await axios.delete(`/api/favorites/${favoriteId}`);
  } catch (error) {
    console.error('Error removing favorite:', error);
  }
};
```

### Fixed Code - Option 1 (By User + Property ID)
```javascript
// ✅ CORRECT - Using query parameters
const handleRemoveFavorite = async (userId, propertyId) => {
  try {
    const response = await axios.delete(
      'http://localhost:8080/api/favorites',
      {
        params: {
          userId: userId,
          propertyId: propertyId
        }
      }
    );
    console.log('Favorite removed:', response.data);
    // Refresh favorites list
    fetchFavorites(userId);
  } catch (error) {
    console.error('Error removing favorite:', error);
  }
};

// Usage in button click
<button onClick={() => handleRemoveFavorite(userId, propertyId)}>
  Remove Favorite
</button>
```

### Fixed Code - Option 2 (By Favorite ID)
```javascript
// ✅ CORRECT - Using path parameter
const handleRemoveFavorite = async (favoriteId) => {
  try {
    const response = await axios.delete(
      `http://localhost:8080/api/favorites/${favoriteId}`
    );
    console.log('Favorite removed:', response.data);
    // Refresh favorites list
    fetchFavorites();
  } catch (error) {
    console.error('Error removing favorite:', error);
  }
};

// Usage in button click
<button onClick={() => handleRemoveFavorite(favorite.id)}>
  Remove Favorite
</button>
```

---

## Common Mistakes Causing 400 Error

### ❌ Missing Parameters
```javascript
// WRONG - No params
await axios.delete('/api/favorites');
```

### ❌ Wrong Format
```javascript
// WRONG - Passing favoriteId but endpoint expects userId + propertyId
await axios.delete('/api/favorites', {
  data: { favoriteId: 1 }
});
```

### ❌ Missing Full URL
```javascript
// WRONG - Using relative path
await axios.delete('/api/favorites?userId=1&propertyId=5');
```

### ✅ Correct Formats
```javascript
// Correct - Full URL with query parameters
await axios.delete(
  'http://localhost:8080/api/favorites',
  { params: { userId: 1, propertyId: 5 } }
);

// Correct - Full URL with path parameter
await axios.delete(
  'http://localhost:8080/api/favorites/1'
);
```

---

## Complete Favorites Component Fix

```javascript
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Favorites = ({ userId }) => {
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFavorites();
  }, [userId]);

  const fetchFavorites = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/favorites/user/${userId}`
      );
      setFavorites(response.data.data);
    } catch (error) {
      console.error('Error fetching favorites:', error);
    } finally {
      setLoading(false);
    }
  };

  // ✅ CORRECT - Remove by userId + propertyId
  const handleRemoveFavorite = async (propertyId) => {
    try {
      await axios.delete(
        'http://localhost:8080/api/favorites',
        {
          params: {
            userId: userId,
            propertyId: propertyId
          }
        }
      );
      // Remove from UI
      setFavorites(favorites.filter(fav => fav.property.id !== propertyId));
      console.log('Favorite removed successfully');
    } catch (error) {
      console.error('Error removing favorite:', error);
      alert('Failed to remove favorite');
    }
  };

  // ✅ ALTERNATIVE - Remove by favoriteId
  const handleRemoveFavoriteById = async (favoriteId) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/favorites/${favoriteId}`
      );
      // Remove from UI
      setFavorites(favorites.filter(fav => fav.id !== favoriteId));
      console.log('Favorite removed successfully');
    } catch (error) {
      console.error('Error removing favorite:', error);
      alert('Failed to remove favorite');
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className="favorites">
      <h2>My Favorites ({favorites.length})</h2>
      {favorites.length === 0 ? (
        <p>No favorites yet</p>
      ) : (
        <div className="favorites-list">
          {favorites.map(favorite => (
            <div key={favorite.id} className="favorite-item">
              <h3>{favorite.property.title}</h3>
              <p>{favorite.property.address}</p>
              <p className="price">${favorite.property.price}</p>
              
              {/* ✅ Remove button */}
              <button
                onClick={() => handleRemoveFavorite(favorite.property.id)}
                className="remove-btn"
              >
                Remove from Favorites
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorites;
```

---

## Testing

### Using cURL (Test Backend)
```bash
# Remove favorite by userId + propertyId
curl -X DELETE "http://localhost:8080/api/favorites?userId=1&propertyId=5"

# Remove favorite by favoriteId
curl -X DELETE "http://localhost:8080/api/favorites/1"
```

### Expected Response
```json
{
  "success": true,
  "message": "Property removed from favorites",
  "data": null,
  "timestamp": "2026-01-28T12:00:00"
}
```

### Test in Frontend
1. Load Favorites page
2. Click "Remove from Favorites" button
3. Should remove from list without 400 error ✅

---

## Troubleshooting

### Still Getting 400?
1. Check userId is not null
2. Check propertyId is not null
3. Verify URL is complete (http://localhost:8080/...)
4. Check network tab in browser DevTools
5. Look at exact error message

### Check Network Tab
1. Right-click → Inspect → Network tab
2. Click remove button
3. Find DELETE request
4. Check:
   - URL: Should be `/api/favorites?userId=X&propertyId=Y`
   - Method: Should be DELETE
   - Status: Should be 200 or 204 (NOT 400)

---

## Summary

| Issue | Solution |
|-------|----------|
| 400 Error | Use correct endpoint format |
| Missing params | Add userId + propertyId as query params |
| Wrong URL | Use full URL: http://localhost:8080/... |
| Not removing | Call `fetchFavorites()` after delete |

---

**Status**: ✅ Backend WORKING | 🔴 Frontend FIX NEEDED

Apply the fixed code above to your Favorites.js and the 400 error should be resolved!
