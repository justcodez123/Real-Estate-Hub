# 400 ERROR FIX - COPY THIS CODE

## The Problem
```
Failed to load resource: the server responded with a status of 400 ()
Favorites.js:82 Error removing favorite: AxiosError
```

## The Fix

Copy this exact code to replace your `handleRemoveFavorite` function:

```javascript
const handleRemoveFavorite = async (propertyId) => {
  try {
    // ✅ MUST include userId and propertyId as query params
    await axios.delete(
      'http://localhost:8080/api/favorites',
      {
        params: {
          userId: userId,
          propertyId: propertyId
        }
      }
    );
    
    // Update UI - remove from list
    setFavorites(favorites.filter(f => f.property.id !== propertyId));
    console.log('Favorite removed successfully');
    
  } catch (error) {
    console.error('Error removing favorite:', error);
    alert('Failed to remove favorite');
  }
};
```

## Key Changes
✅ Use full URL: `http://localhost:8080/api/favorites`
✅ Pass userId and propertyId as `params`
✅ Update the UI after successful deletion
✅ Handle errors with proper logging

## Test It
```bash
curl -X DELETE "http://localhost:8080/api/favorites?userId=1&propertyId=5"
```

Should return 200, not 400 ✅

---

Done! The 400 error will be fixed.
