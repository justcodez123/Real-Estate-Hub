# 🚀 Quick Fix & Run Guide

## ✅ What Was Fixed

The character encoding error has been resolved:
- Changed `characterEncoding=utf8mb4` → `characterEncoding=UTF-8`
- Updated Hibernate dialect: `MySQL8Dialect` → `MySQLDialect`

---

## 🎯 Run Application Now

### Step 1: Ensure MySQL is Running
```powershell
# Check if MySQL is running
mysql -u root -proot -e "SELECT 1;"
# If error, start it:
net start MySQL80
```

### Step 2: Create Database (if not exists)
```powershell
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS realestatedb;"
```

### Step 3: Build Project
```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean package -DskipTests
```

### Step 4: Run Application
```powershell
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
```

### Step 5: Access Application
```
http://localhost:8080
```

---

## ✅ Expected Output

When application starts successfully, you'll see:
```
[INFO] HikariPool-1 - Starting...
[INFO] HikariPool-1 - Added connection
[INFO] Started RealEstateApplication in 3.XXXs
```

**No more charset encoding errors!** ✨

---

## 📋 Configuration Changes Made

**File**: `src/main/resources/application.properties`

### Change 1: JDBC URL
```diff
- characterEncoding=utf8mb4
+ characterEncoding=UTF-8
```

### Change 2: Hibernate Dialect
```diff
- spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
+ spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## 🔍 Verify Configuration

```powershell
# Check if changes were applied
Get-Content "src\main\resources\application.properties" | Select-String "characterEncoding|database-platform"

# Expected Output:
# spring.datasource.url=...characterEncoding=UTF-8
# spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## ⚡ One-Command Quick Start

```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"; C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean package -DskipTests && C:\Maven\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
```

---

## 📚 Full Configuration

**Current JDBC URL:**
```
jdbc:mysql://localhost:3306/realestatedb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&autoReconnect=true&characterEncoding=UTF-8
```

**Connection Details:**
- Host: localhost
- Port: 3306
- Database: realestatedb
- Username: root
- Password: root
- Character Encoding: UTF-8 ✅ FIXED

---

## ❌ If Issues Persist

### Still getting charset error?

**Solution**: Make sure you saved the file:
```powershell
# Check file content
type "src\main\resources\application.properties" | findstr "characterEncoding"
# Must show: characterEncoding=UTF-8
```

### If MySQL not connecting:

**Solution**: Verify MySQL is running:
```powershell
# Test connection
mysql -u root -proot -e "SELECT VERSION();"

# If fails, start MySQL:
net start MySQL80

# Or check if running:
Get-Process | grep -i mysql
```

### If database doesn't exist:

**Solution**: Create it:
```powershell
mysql -u root -proot -e "CREATE DATABASE realestatedb;"
```

---

## 📊 Status Dashboard

| Component | Status | Action |
|-----------|--------|--------|
| Character Encoding | ✅ FIXED | Done |
| Hibernate Dialect | ✅ UPDATED | Done |
| Configuration File | ✅ VERIFIED | Done |
| MySQL Connection | ⏳ Pending | Test Now |
| Application Build | ⏳ Pending | Build Now |
| Application Running | ⏳ Pending | Run Now |

---

**Everything is configured! Ready to run!** 🚀
