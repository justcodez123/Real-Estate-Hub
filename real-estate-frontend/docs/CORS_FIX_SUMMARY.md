# CORS Error - Complete Solution

**Error**: `Access to XMLHttpRequest... has been blocked by CORS policy`

**Status**: Backend configuration issue

**Solution**: Add CORS configuration to backend

---

## Problem Explanation

Frontend at `http://localhost:3001` is making API requests to backend at `http://localhost:8080`. The browser is blocking the responses because the backend is not sending the required CORS headers.

---

## Solution Steps

### STEP 1: Create New File

**Location**: `src/main/java/com/realestate/config/CorsConfig.java`

**Contents**:

```java
package com.realestate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

### STEP 2: Rebuild Backend

Open PowerShell and run:

```bash
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
mvn clean install
```

### STEP 3: Restart Backend

```bash
mvn spring-boot:run
```

### STEP 4: Test

- Go to http://localhost:3001
- Click on any page that makes API calls
- CORS errors should be gone! ✅

---

## What This Does

The `CorsConfig.java` file tells the backend to:

✅ Accept requests from localhost (any port)  
✅ Allow GET, POST, PUT, DELETE, PATCH, OPTIONS methods  
✅ Allow all headers  
✅ Include CORS headers in responses  
✅ Fix all CORS errors  

---

## Verification

Open Browser → Developer Tools → Network Tab

Look at any API request response headers:

```
Access-Control-Allow-Origin: http://localhost:3001  ✅
```

If present, CORS is working!

---

## Result

After this fix:

✅ All API requests work  
✅ No CORS errors  
✅ Data loads on all pages  
✅ Subscriptions work  
✅ User Management works  
✅ Everything functional  

---

**That's all that's needed to fix the CORS issue!** 🚀
