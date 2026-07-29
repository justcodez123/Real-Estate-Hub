# Quick Start - Test Recommend API

## 🚀 Fast Track Testing

### Prerequisites
- ✅ Spring Boot application running on port 8080
- ✅ .NET service running on http://localhost:5000
- ✅ Database with properties

### 1. Start Application (if not running)
```bash
# Navigate to project directory
cd D:\CDAC Project\Atharva\Atharva\real-estate-backend

# Run with Maven
mvn spring-boot:run

# Or run directly via IDE (Run → Run 'RealEstateApplication')
```

### 2. Test API with Simple cURL

**PowerShell (Windows):**
```powershell
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

**Command Prompt (Windows):**
```cmd
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

**Linux/Mac:**
```bash
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

### 3. Check Browser
Open in your browser:
```
http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000
```

### 4. Expected Response (JSON)

#### Success (Properties Found)
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "3BHK Apartment",
      "price": 4500000,
      "city": "Mumbai",
      "bedrooms": 3,
      "bathrooms": 2,
      ...
    }
  ],
  "message": null
}
```

#### Success (No Properties)
```json
{
  "success": true,
  "data": [],
  "message": null
}
```

#### Error (Service Down)
```json
{
  "success": true,
  "data": [],
  "message": null
}
```
*Note: Returns empty list if .NET service is unavailable*

### 5. Check Logs

In the application console, you should see:

```
INFO  c.r.s.DotNetRecommendationClient - Calling .NET recommendation service for location: Mumbai with budget: 5000000.0
INFO  c.r.s.DotNetRecommendationClient - Successfully retrieved 3 recommendations
```

### 6. Test Different Locations

```powershell
# Bangalore
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Bangalore&budget=2000000"

# Delhi
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Delhi&budget=10000000"

# Hyderabad
curl -X GET "http://localhost:8080/api/properties/recommendations?location=Hyderabad&budget=3000000"
```

## 🔍 Testing Checklist

- [ ] Application starts without errors
- [ ] Can access http://localhost:8080 in browser
- [ ] API responds to recommendations request
- [ ] Response contains valid JSON
- [ ] Response wrapped in ApiResponse format
- [ ] HTTP status is 200 OK
- [ ] Logs show service calls
- [ ] Returns data when properties exist
- [ ] Returns empty array when no match
- [ ] Handles .NET service unavailable gracefully

## ⚠️ Troubleshooting

| Issue | Solution |
|-------|----------|
| 404 Not Found | App not running or wrong URL |
| Empty response | No matching properties or .NET service down |
| Connection error | .NET service not running on port 5000 |
| Slow response | Large database - many properties being processed |
| JSON decode error | Response format incorrect (check logs) |

## 📝 Test Scenarios

### Scenario 1: Valid Request
```
GET /api/properties/recommendations?location=Mumbai&budget=5000000
Expected: List of properties in Mumbai within budget
```

### Scenario 2: Large Budget
```
GET /api/properties/recommendations?location=Mumbai&budget=100000000
Expected: More properties (broader range)
```

### Scenario 3: Small Budget
```
GET /api/properties/recommendations?location=Mumbai&budget=1000000
Expected: Fewer properties (limited options)
```

### Scenario 4: Invalid Location
```
GET /api/properties/recommendations?location=InvalidCity&budget=5000000
Expected: Empty list (no properties with that location)
```

### Scenario 5: .NET Service Down
```
Stop .NET service, then:
GET /api/properties/recommendations?location=Mumbai&budget=5000000
Expected: Empty list (graceful fallback)
```

## 🎯 Success Indicators

✅ Application starts on port 8080  
✅ API endpoint returns HTTP 200  
✅ Response is valid JSON  
✅ Response follows ApiResponse format  
✅ Logs show "Calling .NET recommendation service"  
✅ Results match search criteria  
✅ Errors handled gracefully  

## 📊 API Metrics

| Metric | Value |
|--------|-------|
| Method | GET |
| Base Path | /api/properties |
| Endpoint | /recommendations |
| Full Path | /api/properties/recommendations |
| Protocol | HTTP/HTTPS |
| Content-Type | application/json |
| Response Format | ApiResponse<List<Property>> |

## 🔗 Related Documentation

- **Full Testing Guide**: See `RECOMMEND_API_TESTING.md`
- **Implementation Details**: See `RECOMMEND_API_IMPLEMENTATION.md`
- **Verification Report**: See `RECOMMEND_API_VERIFICATION_REPORT.md`
- **Code Changes**: See `CHANGES_SUMMARY.md`

## 💡 Tips

1. **Use Postman** for easier testing with UI
2. **Monitor logs** to debug issues
3. **Check .NET service** if getting empty results
4. **Verify database** has properties with test locations
5. **Use browser DevTools** (F12) to inspect responses

---

**Ready to test? Go to step 1 above!** 🚀
