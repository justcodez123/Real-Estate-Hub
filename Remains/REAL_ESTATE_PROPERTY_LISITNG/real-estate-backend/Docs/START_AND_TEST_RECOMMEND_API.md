# Step-by-Step: Start App & Test Recommend API

## 🚀 Step 1: Start the Spring Boot Application

### Option A: Using Maven (PowerShell)
```powershell
# Navigate to project directory
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"

# Start the application
mvn clean spring-boot:run
```

### Option B: Using IDE
1. Open the project in IntelliJ IDEA or Eclipse
2. Click **Run** button (or press Shift+F10)
3. Wait for "Tomcat started on port(s): 8080"

### Option C: Using pre-built JAR (if available)
```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend\target"
java -jar real-estate-backend-1.0.0.jar
```

---

## ⏳ Wait for Application to Start

The application is ready when you see:
```
[main] o.s.b.w.e.t.TomcatWebServer : Tomcat started on port(s): 8080
[main] o.s.b.a.ApplicationReadyEvent : Spring Boot application started
```

This typically takes 10-30 seconds depending on your system.

---

## ✅ Step 2: Verify Application is Running

Open a **NEW PowerShell window** (keep the first one running with the app):

```powershell
# Test if app is running
curl.exe http://localhost:8080/actuator/health
```

Expected response:
```
{"status":"UP"}
```

---

## 📡 Step 3: Test the Recommend API

In your PowerShell window, run:

### **Best Method for PowerShell:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" -Method Get | Select-Object -ExpandProperty Content | ConvertFrom-Json | ConvertTo-Json
```

### **Or Simpler:**
```powershell
iwr "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000" | Select-Object -ExpandProperty Content
```

### **Or Use curl.exe:**
```powershell
curl.exe -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"
```

### **Or Just Use Browser:**
```
http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000
```

---

## 📊 Step 4: Interpret the Response

### ✅ Success Response (Properties Found)
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "3BHK Luxury Apartment",
      "price": 4500000,
      "city": "Mumbai",
      "bedrooms": 3,
      "bathrooms": 2,
      "squareFeet": 1500,
      "available": true,
      "owner": {
        "id": 1,
        "fullName": "John Doe",
        "email": "john@example.com"
      }
    }
  ],
  "message": null
}
```

**Status Code**: 200 OK  
**Meaning**: API working perfectly!

### ✅ Success Response (No Properties Match)
```json
{
  "success": true,
  "data": [],
  "message": null
}
```

**Status Code**: 200 OK  
**Meaning**: API working, but no properties match the criteria

### ❌ Error Response (Service Unavailable)
```
Connection refused to localhost:8080
```

**Meaning**: Application not running - go back to Step 1

---

## 🧪 Step 5: Test with Different Locations

Try these test commands:

### **Bangalore (Low Budget)**
```powershell
iwr "http://localhost:8080/api/properties/recommendations?location=Bangalore&budget=2000000" | Select-Object -ExpandProperty Content
```

### **Delhi (High Budget)**
```powershell
iwr "http://localhost:8080/api/properties/recommendations?location=Delhi&budget=10000000" | Select-Object -ExpandProperty Content
```

### **Hyderabad (Medium Budget)**
```powershell
iwr "http://localhost:8080/api/properties/recommendations?location=Hyderabad&budget=5000000" | Select-Object -ExpandProperty Content
```

---

## 🔍 Step 6: Check Application Logs

Look at the application console window (where it's running). You should see:

```
INFO  c.r.s.DotNetRecommendationClient - Calling .NET recommendation service for location: Mumbai with budget: 5000000.0
INFO  c.r.s.DotNetRecommendationClient - Successfully retrieved 3 recommendations
```

Or if .NET service is down:
```
ERROR c.r.s.DotNetRecommendationClient - Error calling .NET recommendation service: Connection refused
```

---

## ⚡ Quick Test Summary

| Step | Command | What to Look For |
|------|---------|------------------|
| 1 | `mvn clean spring-boot:run` | "Tomcat started on port 8080" |
| 2 | `curl.exe http://localhost:8080/actuator/health` | `{"status":"UP"}` |
| 3 | `iwr "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"` | JSON response with data |
| 4 | Check console logs | "Calling .NET recommendation service" |

---

## ✅ Success Criteria

- [x] Application starts without errors
- [x] Health endpoint returns UP
- [x] API returns HTTP 200 status
- [x] Response is valid JSON
- [x] Response contains ApiResponse structure
- [x] Logs show service calls
- [x] No timeout errors

---

## ⚠️ Troubleshooting

### Issue: "Connection refused"
```
Cause: Application not running
Solution: Execute mvn clean spring-boot:run and wait for Tomcat to start
```

### Issue: Empty data array []
```
Cause: No matching properties in database
Solution: Check if database has properties with that location
```

### Issue: PowerShell shows error about positional parameters
```
Cause: Using wrong curl syntax
Solution: Use: iwr "url" OR curl.exe -X GET "url"
```

### Issue: Response takes long time
```
Cause: Large database being processed
Solution: Normal for first request, subsequent requests will be faster
```

### Issue: {"success":false}
```
Cause: Internal server error
Solution: Check application logs for error details
```

---

## 📱 Using Postman Instead

If command line is too complex:

1. **Download Postman**: https://www.postman.com/downloads/
2. **Create new request**:
   - Method: GET
   - URL: `http://localhost:8080/api/properties/recommendations`
3. **Add query parameters**:
   - Key: `location` | Value: `Mumbai`
   - Key: `budget` | Value: `5000000`
4. **Click Send**
5. **View response** in the Response tab

---

## 🎯 Next Steps

1. Run the test commands above
2. Verify you get responses
3. Check the logs in the application window
4. Test with different locations/budgets
5. If any issues, check troubleshooting section

---

**Ready? Start with: `mvn clean spring-boot:run`** 🚀
