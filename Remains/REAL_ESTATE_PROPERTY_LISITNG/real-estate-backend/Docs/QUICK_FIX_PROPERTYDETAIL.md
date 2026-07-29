# ⚡ QUICK FIX - PropertyDetail.js Error

## Error
```
propertyService.getPropertyById is not a function
```

## Fix - Add This to Your API Service

Add this to `src/services/api.js` (or wherever your API service is):

```javascript
const propertyService = {
  getPropertyById: (id) => {
    return axios.get(`http://localhost:8080/api/properties/${id}`);
  },

  getPropertyDetails: (id, userId = null) => {
    const url = userId 
      ? `http://localhost:8080/api/properties/${id}/details?userId=${userId}`
      : `http://localhost:8080/api/properties/${id}/details`;
    return axios.get(url);
  },

  getAllProperties: () => {
    return axios.get('http://localhost:8080/api/properties');
  },

  getAvailableProperties: (page = 0, size = 10) => {
    return axios.get(`http://localhost:8080/api/properties/available?page=${page}&size=${size}`);
  }
};

export default propertyService;
```

## Update PropertyDetail.js

```javascript
import propertyService from '../services/api';

useEffect(() => {
  fetchProperty();
}, [propertyId]);

const fetchProperty = async () => {
  try {
    const response = await propertyService.getPropertyById(propertyId);
    setProperty(response.data.data);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

## Done! ✅

Error fixed. PropertyDetail component will now work!
