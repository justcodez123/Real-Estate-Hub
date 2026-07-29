# Maven Setup Guide - Troubleshooting "mvn" Command Not Found

## Problem
`mvn: The term 'mvn' is not recognized as the name of a cmdlet, function, script file, or operable program.`

## Solution

### Step 1: Check if Java is Installed
Open PowerShell and run:
```powershell
java -version
```

**Expected Output:**
```
java version "17.0.1" 2021-10-19 LTS
Java(TM) SE Runtime Environment (build 17.0.1+12-LTS-39)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.1+12-LTS-39, mixed mode, sharing)
```

If Java is not installed:
- Download from: https://www.oracle.com/java/technologies/downloads/#java17
- Install Java 17 (required for this project)

### Step 2: Download Maven

1. Go to: https://maven.apache.org/download.cgi
2. Download: `apache-maven-3.9.x-bin.zip` (latest version)
3. Extract to a folder, e.g., `C:\Maven\apache-maven-3.9.x`

### Step 3: Set Environment Variables

**On Windows 10/11:**

1. Press `Win + X` and select "System"
2. Click "Advanced system settings"
3. Click "Environment Variables" button
4. Under "System variables", click "New"

**Add:**
- Variable name: `MAVEN_HOME`
- Variable value: `C:\Maven\apache-maven-3.9.x` (or your Maven path)

**Edit PATH variable:**
1. Select "Path" and click "Edit"
2. Click "New"
3. Add: `%MAVEN_HOME%\bin`
4. Click OK

### Step 4: Verify Maven Installation

Open a **NEW PowerShell window** (important!) and run:
```powershell
mvn --version
```

**Expected Output:**
```
Apache Maven 3.9.x (...)
Maven home: C:\Maven\apache-maven-3.9.x
Java version: 17.0.1
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

### Step 5: Navigate to Project and Run

```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
mvn clean install
mvn spring-boot:run
```

---

## Alternative: Use Maven Wrapper (Easier)

The project has a Maven Wrapper that doesn't require Maven installation:

```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
mvnw clean install
mvnw spring-boot:run
```

**Maven Wrapper files:**
- `mvnw` (for Linux/Mac)
- `mvnw.cmd` (for Windows)

---

## Common Maven Commands

```powershell
# Clean build
mvn clean

# Compile
mvn compile

# Run tests
mvn test

# Build project
mvn install

# Run Spring Boot application
mvn spring-boot:run

# Clean and build
mvn clean install

# Build with specific Java version
mvn clean install -DskipTests
```

---

## Troubleshooting

### Issue: "JAVA_HOME is not set"
```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
mvn --version
```

### Issue: "Maven repository not accessible"
```powershell
# Use offline mode
mvn --offline clean install

# Or clear cache
mvn clean -DskipTests
```

### Issue: Port 8080 already in use
```powershell
# Use different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Issue: Out of memory
```powershell
# Increase heap size
$env:MAVEN_OPTS = "-Xmx1024m"
mvn clean install
```

---

## Step-by-Step Quick Setup

1. **Download Java 17**
   - Visit: https://www.oracle.com/java/technologies/downloads/
   - Download and install Java SE 17

2. **Download Maven**
   - Visit: https://maven.apache.org/download.cgi
   - Download binary zip file
   - Extract to `C:\Maven`

3. **Set Environment Variables**
   - Add `MAVEN_HOME` = `C:\Maven\apache-maven-3.9.x`
   - Add `%MAVEN_HOME%\bin` to PATH

4. **Verify Installation**
   ```powershell
   mvn --version
   ```

5. **Run Project**
   ```powershell
   cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
   mvn clean install
   mvn spring-boot:run
   ```

---

## Application Running Successfully

When Maven runs the application successfully, you should see:

```
[INFO] BUILD SUCCESS
[INFO] Total time: 45.123 s
[INFO] Finished at: 2026-01-26T10:30:00-05:00
[INFO] Final Memory: 45M/512M

2026-01-26 10:31:00.123  INFO 12345 --- [  restartedMain] c.r.RealEstateApplication : Started RealEstateApplication in 2.345 seconds (JVM running for 2.456)
```

Application will be available at: `http://localhost:8080`

---

## Testing the Application

Once running, test endpoints:

```powershell
# Test Contact Agent
curl http://localhost:8080/api/contact-agents/unread

# Test Schedule Viewing
curl http://localhost:8080/api/schedule-viewings/user/1

# Test Favorites (fixed)
curl http://localhost:8080/api/favorites/user/1
```

---

## Next Steps

1. ✅ Install Java 17
2. ✅ Install Maven 3.9+
3. ✅ Set environment variables
4. ✅ Verify with `mvn --version`
5. ✅ Run `mvn clean install`
6. ✅ Run `mvn spring-boot:run`
7. ✅ Test endpoints using provided cURL commands

---

## Additional Resources

- Maven Official: https://maven.apache.org/
- Java 17 Download: https://www.oracle.com/java/technologies/downloads/
- Spring Boot Maven Plugin: https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/
- Maven Wrapper: https://maven.apache.org/wrapper/

---

**Once Maven is set up, you'll be able to compile, test, and run the real estate backend application!**
