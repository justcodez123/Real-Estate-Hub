# 🎯 YOUR EC2 REACT FIX - QUICK CARD

## Your EC2 Details
```
Domain: ec2-13-220-57-64.compute-1.amazonaws.com
Frontend: http://ec2-13-220-57-64.compute-1.amazonaws.com:3000
Backend: http://ec2-13-220-57-64.compute-1.amazonaws.com:8080
```

---

## ✅ BACKEND - DONE
WebConfig.java updated with CORS for your EC2 domain

---

## 📝 REACT - 4 STEPS

### 1️⃣ Create `.env` file (React root)
```
REACT_APP_API_URL=http://ec2-13-220-57-64.compute-1.amazonaws.com:8080
```

### 2️⃣ Create `src/services/api.js`
Use the service code from: `EC2_REACT_COMPLETE_FIX_YOUR_DOMAIN.md`

### 3️⃣ Update React Components
Replace all `localhost:8080` calls with:
```javascript
import { propertyService } from '../services/api';
propertyService.getAvailableProperties()
```

### 4️⃣ Rebuild & Deploy
```bash
npm install
npm run build
npm start
```

---

## 🧪 Test
```
Browser: http://ec2-13-220-57-64.compute-1.amazonaws.com:3000
F12 → Console: Should show ✅ Properties loaded
F12 → Network: Should show calls to ec2-13-220-57-64... (not localhost)
```

---

## 📖 Full Guide
See: `EC2_REACT_COMPLETE_FIX_YOUR_DOMAIN.md`

