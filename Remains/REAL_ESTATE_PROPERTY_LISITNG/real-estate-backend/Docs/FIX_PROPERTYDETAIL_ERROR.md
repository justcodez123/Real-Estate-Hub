# 🔧 FIX: PropertyDetail.js - propertyService.getPropertyById is not a function

## Issue
```
Error: propertyService.getPropertyById is not a function
```

## Root Cause
The frontend API service doesn't export `getPropertyById` function, or it's named differently.

## Backend Endpoints Available

✅ `GET /api/properties/{id}` - Returns Property object
✅ `GET /api/properties/{id}/details` - Returns PropertyResponse with images and favorites

## Solution

### Option 1: Update API Service (Recommended)

Your `api.js` or `apiService.js` file needs these functions:

```javascript
// Add to your API service file (e.g., services/api.js)

const propertyService = {
  // Get basic property info
  getPropertyById: (id) => {
    return axios.get(`http://localhost:8080/api/properties/${id}`);
  },

  // Get detailed property info with images and favorites
  getPropertyDetails: (id, userId = null) => {
    const url = userId 
      ? `http://localhost:8080/api/properties/${id}/details?userId=${userId}`
      : `http://localhost:8080/api/properties/${id}/details`;
    return axios.get(url);
  },

  // Get all properties
  getAllProperties: () => {
    return axios.get('http://localhost:8080/api/properties');
  },

  // Get available properties with pagination
  getAvailableProperties: (page = 0, size = 10) => {
    return axios.get(`http://localhost:8080/api/properties/available?page=${page}&size=${size}`);
  },

  // Create property
  createProperty: (propertyData) => {
    return axios.post('http://localhost:8080/api/properties', propertyData);
  },

  // Update property
  updateProperty: (id, propertyData) => {
    return axios.put(`http://localhost:8080/api/properties/${id}`, propertyData);
  },

  // Delete property
  deleteProperty: (id) => {
    return axios.delete(`http://localhost:8080/api/properties/${id}`);
  },

  // Get property images
  getPropertyImages: (propertyId) => {
    return axios.get(`http://localhost:8080/api/properties/${propertyId}/images`);
  },

  // Add image to property
  addPropertyImage: (propertyId, imageUrl, caption = '') => {
    return axios.post(`http://localhost:8080/api/properties/${propertyId}/images`, {
      imageUrl,
      caption,
      isPrimary: false
    });
  }
};

export default propertyService;
```

### Option 2: Update PropertyDetail.js Component

If you're exporting it differently, update PropertyDetail.js:

```javascript
import propertyService from '../services/api'; // Adjust path

useEffect(() => {
  fetchProperty();
}, [propertyId]);

const fetchProperty = async () => {
  try {
    setLoading(true);
    
    // Use the correct method name
    const response = await propertyService.getPropertyDetails(propertyId);
    // OR if you want basic info:
    // const response = await propertyService.getPropertyById(propertyId);
    
    setProperty(response.data.data);
  } catch (error) {
    console.error('Error fetching property:', error);
    setError('Failed to load property');
  } finally {
    setLoading(false);
  }
};
```

---

## Complete API Service Template

Create or update `src/services/api.js`:

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Property Service
const propertyService = {
  getPropertyById: (id) => api.get(`/properties/${id}`),
  getPropertyDetails: (id, userId) => {
    const params = userId ? `?userId=${userId}` : '';
    return api.get(`/properties/${id}/details${params}`);
  },
  getAllProperties: () => api.get('/properties'),
  getAvailableProperties: (page = 0, size = 10) => 
    api.get(`/properties/available?page=${page}&size=${size}`),
  createProperty: (data) => api.post('/properties', data),
  updateProperty: (id, data) => api.put(`/properties/${id}`, data),
  deleteProperty: (id) => api.delete(`/properties/${id}`),
  getPropertiesByCity: (city) => api.get(`/properties/city/${city}`),
  getPropertiesByOwner: (ownerId, page = 0, size = 10) => 
    api.get(`/properties/owner/${ownerId}/paged?page=${page}&size=${size}`),
  searchProperties: (query) => api.post('/properties/search', query),
  
  // Images
  getPropertyImages: (propertyId) => api.get(`/properties/${propertyId}/images`),
  addPropertyImage: (propertyId, imageUrl, caption = '') => 
    api.post(`/properties/${propertyId}/images`, { imageUrl, caption }),
  deletePropertyImage: (propertyId, imageId) => 
    api.delete(`/properties/${propertyId}/images/${imageId}`),
  
  // Bulk images
  addImagesToEmptyProperties: () => 
    api.post('/properties/images/bulk/add-images-to-empty-properties'),
  addImagesToAllProperties: () => 
    api.post('/properties/images/bulk/add-images-to-all-properties'),
  addImagesToProperty: (propertyId, count) => 
    api.post(`/properties/${propertyId}/bulk-add-images?numberOfImages=${count}`)
};

export default propertyService;
```

---

## Steps to Fix

1. **Check your API service file** (likely `src/services/api.js`)

2. **Add the `getPropertyById` function** if missing (code above)

3. **Update PropertyDetail.js** to use the correct import:
   ```javascript
   import propertyService from '../services/api';
   ```

4. **Update the fetch call** in PropertyDetail.js:
   ```javascript
   const response = await propertyService.getPropertyById(propertyId);
   ```

5. **Test the component**

---

## Verification

After fixing, the component should:
- ✅ Load property data from API
- ✅ Display property details
- ✅ Show images
- ✅ No "not a function" errors

---

## Common Mistakes to Avoid

❌ Wrong import path
✅ Verify relative path is correct

❌ Function name mismatch
✅ Use `getPropertyById` or `getPropertyDetails` (one that exists)

❌ Missing axios in API service
✅ Make sure axios is imported

❌ Wrong API endpoint
✅ Endpoint should be `/api/properties/{id}`

---

## If Still Not Working

1. Check your actual API service file path
2. Verify the function is exported correctly
3. Check browser console for exact error
4. Verify axios is installed: `npm install axios`

---

**Status**: ✅ READY TO IMPLEMENT

Use the template above and update your API service file!
