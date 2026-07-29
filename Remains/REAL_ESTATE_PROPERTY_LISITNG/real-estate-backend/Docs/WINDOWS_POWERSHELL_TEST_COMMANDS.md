# Correct Test Commands for Windows PowerShell

## ⚠️ Your Error Explained

You used:
```powershell
curl GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

The issue is that PowerShell's `curl` alias uses **different syntax** than Unix/Linux curl.

---

## ✅ Correct Ways to Test

### Option 1: Use curl.exe (Traditional curl syntax)
```powershell
curl.exe -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

### Option 2: Use PowerShell's Invoke-WebRequest (Recommended for PowerShell)
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get
```

Shorter version:
```powershell
iwr -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get
```

### Option 3: Use Browser (Easiest)
Simply open in your browser:
```
http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000
```

### Option 4: Different Locations and Budgets

**Mumbai - High Budget:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=10000000" -Method Get
```

**Bangalore - Low Budget:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Bangalore&budget=2000000" -Method Get
```

**Delhi - Medium Budget:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Delhi&budget=5000000" -Method Get
```

---

## 🚀 Before Testing: Start the Application

Make sure the Spring Boot application is running first!

### From PowerShell:
```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
mvn spring-boot:run
```

Or start it from your IDE (Run button).

Wait for it to show:
```
[main] o.s.b.w.e.t.TomcatWebServer : Tomcat started on port(s): 8080
```

---

## 📊 Expected Response (If API Working)

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Luxury 3BHK Apartment",
      "price": 4500000,
      "city": "Mumbai",
      "bedrooms": 3,
      "bathrooms": 2,
      "available": true
    }
  ],
  "message": null
}
```

Or if no properties match:
```json
{
  "success": true,
  "data": [],
  "message": null
}
```

---

## ⚠️ Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Connection refused | App not running | Start with `mvn spring-boot:run` |
| Empty data [] | No matching properties | Check database has properties |
| 404 Not Found | Wrong URL | Verify URL and app is running |
| Timeout | App not responding | Check app logs for errors |

---

## 🔍 How to Check if App is Running

### Method 1: Check Port 8080
```powershell
netstat -ano | findstr :8080
```

If you see output, the app is running. If not, start it.

### Method 2: Use curl to test health
```powershell
curl.exe http://localhost:8080/actuator/health
```

Should return:
```json
{"status":"UP"}
```

---

## 💡 Tips for PowerShell Testing

### Save the response to a variable:
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10
```

### Pretty print the JSON:
```powershell
$response.Content | ConvertFrom-Json | Format-List
```

### Check only the status:
```powershell
(Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get).StatusCode
```

Should return: `200`

---

## Quick Copy-Paste Commands

### Simple test:
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get | Select-Object -ExpandProperty Content
```

### With formatted output:
```powershell
(Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get).Content | ConvertFrom-Json | ConvertTo-Json
```

---

## Postman Alternative

If you prefer a GUI:
1. Download Postman: https://www.postman.com/downloads/
2. Create new GET request
3. URL: `http://localhost:8080/api/properties/recommendations`
4. Add query parameters:
   - Key: `location` | Value: `Mumbai`
   - Key: `budget` | Value: `5000000`
5. Click Send

---

**Remember**: Always start the application first with `mvn spring-boot:run`!
