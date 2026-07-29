# MySQL Setup & Application Run - Quick Guide

## 🚀 Quick Start (Copy-Paste Ready)

### 1️⃣ Start MySQL Server
```powershell
# Windows - If using MySQL service
net start MySQL80

# Or verify MySQL is running
mysql -u root -proot -e "SELECT VERSION();"
```

### 2️⃣ Create Database
```powershell
# Run this in PowerShell
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS realestatedb;"
```

### 3️⃣ Build Project
```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean install -DskipTests
```

### 4️⃣ Run Application
```powershell
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
```

### 5️⃣ Test API (In Another Terminal)
```powershell
curl http://localhost:8080/api/users
```

---

## 📋 Current Configuration

| Setting | Value |
|---------|-------|
| **Database** | MySQL 8.0+ |
| **Host** | localhost:3306 |
| **Database Name** | realestatedb |
| **Username** | root |
| **Password** | root |
| **Server Port** | 8080 |
| **JDBC Driver** | com.mysql.cj.jdbc.Driver |
| **Hibernate Dialect** | MySQL8Dialect |
| **DDL Mode** | update (preserves data) |

---

## 🔧 Common Issues & Fixes

### ❌ "Connection refused" at port 3306
```powershell
# Solution: Start MySQL
net start MySQL80
```

### ❌ "Access denied for user 'root'"
```powershell
# Solution: Verify MySQL password
mysql -u root -proot

# If wrong password, update in:
# src/main/resources/application.properties
# spring.datasource.password=your_actual_password
```

### ❌ "Unknown database 'realestatedb'"
```powershell
# Solution: Create database
mysql -u root -proot -e "CREATE DATABASE realestatedb;"
```

### ❌ "mvn" command not found
```powershell
# Solution: Use full path
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd --version

# Or restart PowerShell (environment variables updated)
```

---

## ✅ Verification Checklist

Run these commands to verify everything is set up:

```powershell
# 1. Check Java
java -version
# Expected: openjdk version "21.0.8" or similar

# 2. Check Maven
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd --version
# Expected: Apache Maven 3.9.11

# 3. Check MySQL
mysql -u root -proot -e "SELECT VERSION();"
# Expected: MySQL version 8.x or higher

# 4. Check Database
mysql -u root -proot -e "SHOW DATABASES;" | grep realestatedb
# Expected: realestatedb listed

# 5. Check Build
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean compile -DskipTests
# Expected: BUILD SUCCESS
```

---

## 📁 Important Directories

```
D:\CDAC Project\Atharva\Atharva\real-estate-backend\
├── src/main/resources/
│   └── application.properties          ← MySQL config here
├── target/
│   └── real-estate-backend-1.0.0.jar   ← Built JAR file
└── pom.xml                             ← Project dependencies
```

---

## 🌐 API Access

Once running:
- **Base URL**: http://localhost:8080
- **Swagger UI** (if configured): http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/health (if available)

Example API call:
```powershell
# Get all users
curl http://localhost:8080/api/users

# Get specific user
curl http://localhost:8080/api/users/1

# Create new user
curl -X POST http://localhost:8080/api/users `
  -H "Content-Type: application/json" `
  -d '{"name":"John","email":"john@example.com"}'
```

---

## 📚 Helpful Links

- **MySQL Documentation**: https://dev.mysql.com/doc/
- **Spring Boot Guide**: https://spring.io/guides/gs/spring-boot/
- **Hibernate Documentation**: https://hibernate.org/orm/
- **Maven Guide**: https://maven.apache.org/guides/

---

## 🔄 Development Workflow

```
1. Edit Code
   ↓
2. mvn clean compile
   (Verify syntax)
   ↓
3. mvn spring-boot:run
   (Test locally)
   ↓
4. mvn clean install
   (Full build with tests - when ready)
   ↓
5. Deploy target/real-estate-backend-1.0.0.jar
```

---

## 💾 Backup & Restore Database

```sql
-- Export database
mysqldump -u root -proot realestatedb > backup.sql

-- Import database
mysql -u root -proot realestatedb < backup.sql
```

---

## 📊 Useful MySQL Commands

```sql
-- Connect to database
mysql -u root -proot realestatedb

-- List all tables
SHOW TABLES;

-- Describe a table
DESCRIBE users;

-- View all users
SELECT * FROM users;

-- Count records
SELECT COUNT(*) FROM users;

-- Check database size
SELECT 
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb
FROM information_schema.tables
WHERE table_schema = 'realestatedb';
```

---

## ⚙️ Performance Tips

1. **Enable Query Cache** (MySQL 5.7):
   ```sql
   SET GLOBAL query_cache_size = 268435456;  -- 256MB
   SET GLOBAL query_cache_type = 1;
   ```

2. **Connection Pool Tuning** (in application.properties):
   ```properties
   spring.datasource.hikari.maximum-pool-size=10
   spring.datasource.hikari.minimum-idle=5
   spring.datasource.hikari.connection-timeout=20000
   ```

3. **Enable SQL Performance Logging**:
   ```properties
   spring.jpa.properties.hibernate.generate_statistics=true
   logging.level.org.hibernate.stat=DEBUG
   ```

---

## 🎯 Status

✅ **Configuration**: MySQL enabled  
✅ **Build**: Successful (4.303 seconds)  
✅ **Dependencies**: All present  
✅ **Java**: 21.0.8 installed  
✅ **Maven**: 3.9.11 installed  

**Ready to deploy!** 🚀

---

**Last Updated**: January 26, 2026  
**Configuration Version**: 1.0.0
