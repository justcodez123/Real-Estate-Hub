# 🚨 AWS DEPLOYMENT - CORS ERROR FIXED

## The Problem

Your AWS S3 frontend (`http://realestate-frontend.s3-website-us-east-1.amazonaws.com`) was trying to connect to `localhost:8080`, which causes:

```
Access to XMLHttpRequest at 'http://localhost:8080/api/properties/available' 
from origin 'http://realestate-frontend.s3-website-us-east-1.amazonaws.com' 
has been blocked by CORS policy: The request client is not a secure context 
and the resource is in more-private address space `loopback`.
```

**Why?** AWS S3 (public internet) cannot access `localhost` (your local machine) - it's a security restriction.

---

## ✅ Solution Applied

### 1. Created `.env.production` File
- Uses your AWS backend IP: `http://13.220.57.64:8080/api`
- Automatically used when building for production
- Keeps `.env` for local development

### 2. What You Need to Do NOW

#### Option A: Deploy Backend to AWS (Recommended for Production)

Your backend must be accessible from the internet. Follow these steps:

**Step 1: Deploy Backend to AWS EC2**

```bash
# On your AWS EC2 instance (or create one):
# 1. Upload your backend JAR file
# 2. Install Java 17+
# 3. Run the backend:
java -jar real-estate-backend.jar
```

**Step 2: Update Backend CORS Configuration**

Your backend needs to allow requests from your S3 URL. Add/update this in your Spring Boot:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins(
                        "http://localhost:3000",
                        "http://realestate-frontend.s3-website-us-east-1.amazonaws.com",
                        "http://13.220.57.64:8080"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

**Step 3: Rebuild and Redeploy Frontend**

```powershell
# Build for production (uses .env.production)
npm run build

# Deploy to S3
aws s3 sync build/ s3://YOUR_BUCKET_NAME --delete

# Or if using AWS CLI with specific bucket:
aws s3 sync build/ s3://realestate-frontend --delete
```

---

#### Option B: Test Locally (Quick Test)

If you want to test the AWS frontend with your local backend temporarily:

**Step 1: Update `.env.production`**
```env
REACT_APP_API_URL=http://localhost:8080/api
```

**Step 2: Run frontend locally**
```powershell
npm start
```

This will work locally, but NOT from AWS S3.

---

## 🎯 Recommended AWS Architecture

```
┌─────────────────────────────────────────────────┐
│                                                 │
│  User's Browser                                 │
│                                                 │
└──────────────┬──────────────────────────────────┘
               │
               ├─────────────────────────────────┐
               │                                 │
               v                                 v
┌──────────────────────────┐    ┌──────────────────────────┐
│                          │    │                          │
│  AWS S3 Static Website   │    │   AWS EC2 Instance       │
│  (Frontend - React)      │    │   (Backend - Spring)     │
│                          │    │                          │
│  URL: http://...s3...    │───>│  URL: http://IP:8080     │
│                          │    │                          │
└──────────────────────────┘    └──────────────┬───────────┘
                                               │
                                               v
                                ┌──────────────────────────┐
                                │                          │
                                │   AWS RDS MySQL          │
                                │   (Database)             │
                                │                          │
                                └──────────────────────────┘
```

---

## 📋 Complete AWS Deployment Checklist

### Backend Deployment (EC2)

- [ ] Create EC2 instance (Ubuntu/Amazon Linux)
- [ ] Install Java 17+
- [ ] Upload backend JAR file
- [ ] Configure security group (allow port 8080)
- [ ] Start backend: `java -jar backend.jar`
- [ ] Test: `curl http://YOUR_EC2_IP:8080/api/properties/available`

### Database Setup (RDS)

- [ ] Create RDS MySQL instance
- [ ] Update backend `application.properties` with RDS endpoint
- [ ] Configure security group (allow 3306 from EC2)
- [ ] Test connection from EC2

### Frontend Deployment (S3)

- [ ] Update `.env.production` with backend EC2 IP
- [ ] Build: `npm run build`
- [ ] Upload to S3: `aws s3 sync build/ s3://bucket-name`
- [ ] Enable static website hosting on S3
- [ ] Make bucket public or use CloudFront

### CORS Configuration

- [ ] Update backend to allow S3 origin
- [ ] Test CORS from browser console
- [ ] Verify network requests succeed

---

## 🔧 Quick Commands

### Build and Deploy Frontend
```powershell
# Build for production
npm run build

# Deploy to S3 (replace with your bucket name)
aws s3 sync build/ s3://realestate-frontend --delete --acl public-read
```

### Deploy Backend to EC2
```bash
# SSH to EC2
ssh -i your-key.pem ec2-user@13.220.57.64

# Upload JAR
scp -i your-key.pem target/backend.jar ec2-user@13.220.57.64:~/

# Run backend
nohup java -jar backend.jar > app.log 2>&1 &
```

### Test Backend
```bash
curl http://13.220.57.64:8080/api/properties/available
```

---

## 🐛 Troubleshooting

### "Backend still not accessible from S3"

1. **Check EC2 Security Group:**
   - Inbound rule: TCP 8080 from 0.0.0.0/0 (or your IP)

2. **Check Backend CORS:**
   - Must allow S3 URL in `allowedOrigins`

3. **Check Backend is Running:**
   ```bash
   ps aux | grep java
   ```

### "CORS error persists"

Update backend controller:
```java
@CrossOrigin(origins = "*") // Allow all (for testing)
@RestController
@RequestMapping("/api")
public class PropertyController {
    // ... your endpoints
}
```

### "Can't access EC2 backend"

```bash
# Check if backend is running
netstat -tulpn | grep 8080

# Check firewall
sudo ufw status

# View logs
tail -f app.log
```

---

## 💡 Production Best Practices

1. **Use HTTPS:**
   - Get SSL certificate (AWS Certificate Manager)
   - Use CloudFront for S3
   - Use Application Load Balancer for EC2

2. **Use Environment-Specific URLs:**
   - `.env.development` → localhost
   - `.env.production` → AWS IPs/domains

3. **Secure CORS:**
   - Don't use `*` in production
   - Specify exact allowed origins

4. **Use Domain Names:**
   - Register domain (Route 53)
   - frontend.yourdomain.com → S3/CloudFront
   - api.yourdomain.com → EC2/ALB

---

## 🚀 Next Steps

1. **Deploy backend to AWS EC2** (IP: 13.220.57.64 or new instance)
2. **Update backend CORS** to allow S3 origin
3. **Rebuild frontend:** `npm run build`
4. **Deploy to S3:** `aws s3 sync build/ s3://bucket-name`
5. **Test:** Open S3 URL and check if properties load

---

## 📞 Need Help?

Read these guides:
- `AWS_DEPLOYMENT_GUIDE.md` (if exists)
- `AWS_QUICK_START.md` (if exists)

---

**Current Status:**
- ✅ `.env.production` created with AWS backend IP
- ✅ Local `.env` keeps localhost for development
- ⚠️ Backend must be deployed to AWS and CORS configured
- ⚠️ Frontend must be rebuilt and redeployed to S3

**The error will be fixed once backend is accessible from AWS and CORS is properly configured!**
