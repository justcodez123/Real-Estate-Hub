# Recommend API Testing Guide

## Endpoint Details

**Method:** GET  
**Path:** `/api/properties/recommendations`  
**Base URL:** `http://localhost:8080`  
**Full URL:** `http://localhost:8080/api/properties/recommendations`

## Required Parameters

| Parameter | Type   | Required | Example |
|-----------|--------|----------|---------|
| location  | String | Yes      | "Mumbai" |
| budget    | double | Yes      | 5000000 |

## Test Commands

### Using cURL (PowerShell)

```powershell
# Test 1: Get recommendations for Mumbai with budget of 5 million
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" `
  -H "Content-Type: application/json"

# Test 2: Get recommendations for Bangalore with budget of 2 million
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Bangalore&budget=2000000" `
  -H "Content-Type: application/json"

# Test 3: Get recommendations for Delhi with budget of 10 million
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Delhi&budget=10000000" `
  -H "Content-Type: application/json"
```

### Using Postman

1. **Create a new GET request**
2. **Enter URL:**
   ```
   http://localhost:8080/api/properties/recommendations
   ```
3. **Add Parameters (Query):**
   - Key: `location` | Value: `Mumbai`
   - Key: `budget` | Value: `5000000`
4. **Click Send**

### Using Python

```python
import requests

url = "http://localhost:8080/api/properties/recommendations"
params = {
    "location": "Mumbai",
    "budget": 5000000
}

response = requests.get(url, params=params)
print(response.json())
```

### Using JavaScript/Fetch API

```javascript
const location = "Mumbai";
const budget = 5000000;

fetch(`http://localhost:8080/api/properties/recommendations?location=${location}&budget=${budget}`)
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

## Expected Response (Success)

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Luxury 3BHK Apartment",
      "description": "Beautiful apartment in prime location",
      "price": 4500000,
      "address": "123 Main Street",
      "city": "Mumbai",
      "state": "Maharashtra",
      "zipCode": "400001",
      "propertyType": "APARTMENT",
      "listingType": "RENT",
      "bedrooms": 3,
      "bathrooms": 2,
      "squareFeet": 1500,
      "yearBuilt": 2020,
      "available": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-20T14:45:00",
      "owner": {
        "id": 1,
        "fullName": "John Doe",
        "email": "john@example.com",
        "phone": "+91-9876543210",
        "company": "Real Estate Co.",
        "profileImageUrl": "https://example.com/profile.jpg"
      },
      "imageUrls": ["https://example.com/image1.jpg"],
      "favoriteCount": 5,
      "isFavorited": false
    }
  ],
  "message": null
}
```

## Expected Response (No Recommendations Found)

```json
{
  "success": true,
  "data": [],
  "message": null
}
```

## Expected Response (Error)

```json
{
  "success": false,
  "data": null,
  "message": "Error calling .NET recommendation service: Connection refused"
}
```

## Prerequisites for Testing

1. **Application must be running:**
   ```
   The Spring Boot application must be running on port 8080
   ```

2. **.NET Recommendation Service must be running:**
   ```
   The .NET service must be running on http://localhost:5000/api/recommend
   ```

3. **Database must have properties:**
   ```
   Ensure the database has properties with the specified locations
   ```

## Troubleshooting

### Issue: 404 Not Found
- **Cause:** Application not running or endpoint path is incorrect
- **Solution:** Ensure the application is running on port 8080

### Issue: Empty response []
- **Cause:** No properties match the criteria, or .NET service returned empty
- **Solution:** Check if properties exist in the database with the specified location

### Issue: Connection refused to .NET service
- **Cause:** .NET recommendation service is not running
- **Solution:** Start the .NET service on `http://localhost:5000`

### Issue: Unsupported character encoding error
- **Cause:** Database connection issue with utf8mb4 encoding
- **Solution:** Check CHARACTER_ENCODING_FIX.md in the project root

## Implementation Details

### Request Flow
1. Client sends GET request with location and budget parameters
2. PropertyController receives the request
3. PropertyRepository retrieves all properties from database
4. DotNetRecommendationClient calls the .NET API
5. .NET service filters properties and returns recommendations
6. Response is wrapped in ApiResponse and returned to client

### Key Files
- **Controller:** `PropertyController.java` - Handles HTTP requests
- **Service:** `DotNetRecommendationClient.java` - Calls .NET API
- **Config:** `RestTemplateConfig.java` - Configures RestTemplate bean
- **Repository:** `PropertyRepository.java` - Database queries

## Response Wrapping

All responses are wrapped in an `ApiResponse` object:
```json
{
  "success": boolean,
  "data": <payload>,
  "message": string or null
}
```

## Rate Limiting

Currently, there are no rate limits on the recommendation API. If implementing rate limiting:
- Use Spring Security with @RateLimiter annotation
- Or implement a custom interceptor

## API Security

Currently, the endpoint is open to all. For production:
- Add authentication/authorization
- Use @PreAuthorize or @Secured annotations
- Implement API key validation
