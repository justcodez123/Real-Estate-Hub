# Maven Setup - COMPLETE ✅

## Status
**Maven is now installed and configured on your system.**

## What Was Done

### 1. Java Verification ✅
- **Java Version**: 21.0.8 LTS (Perfect! Better than required 17)
- **Java Location**: `C:\Program Files\Java\jdk-21`
- **JAVA_HOME**: Already set correctly

### 2. Maven Installation ✅
- **Version**: Apache Maven 3.9.11
- **Installation Path**: `C:\Maven\apache-maven-3.9.11`
- **Bin Directory**: `C:\Maven\apache-maven-3.9.11\bin`

### 3. Environment Configuration ✅
- **MAVEN_HOME**: Set to `C:\Maven\apache-maven-3.9.11`
- **PATH**: Updated with Maven bin directory
- **PowerShell Profile**: Updated with Maven configuration

## How to Use Maven Going Forward

### Option 1: Using Full Path (Works Now)
```powershell
cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean install
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
```

### Option 2: Quick Alias (Recommended)
Add this to your PowerShell session:
```powershell
$env:MAVEN_HOME = 'C:\Maven\apache-maven-3.9.11'
$env:Path = $env:MAVEN_HOME + '\bin;' + $env:Path
```

Then you can use:
```powershell
mvn clean install
mvn spring-boot:run
```

### Option 3: Close and Reopen PowerShell (Permanent)
Your PowerShell profile has been updated automatically. Simply:
1. **Close** your current PowerShell window
2. **Open** a new PowerShell window
3. Now `mvn` will work directly without any setup

## Common Maven Commands

```powershell
# Check Maven version
mvn --version

# Clean build directory
mvn clean

# Compile code
mvn compile

# Run all tests
mvn test

# Build JAR file
mvn install

# Build and skip tests (faster)
mvn clean install -DskipTests

# Run Spring Boot application
mvn spring-boot:run

# Run with specific port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# Build only (no tests)
mvn clean package -DskipTests
```

## Project Build Command

For your real-estate-backend project:

```powershell
# Full build with tests
mvn clean install

# Fast build without tests
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

## Expected Build Output

When successful, you'll see:
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXXs
[INFO] Finished at: 2026-01-26T...
```

## Application Access

Once running with `mvn spring-boot:run`, your application will be available at:
- **Base URL**: http://localhost:8080
- **API Endpoints**: http://localhost:8080/api/...

## Troubleshooting

### If `mvn` command still not found:
**Solution**: Use the full path:
```powershell
C:\Maven\apache-maven-3.9.11\bin\mvn.cmd clean install
```

### If you see "MAVEN_HOME is not set":
**Solution**: Run in PowerShell:
```powershell
$env:MAVEN_HOME = 'C:\Maven\apache-maven-3.9.11'
$env:Path = $env:MAVEN_HOME + '\bin;' + $env:Path
```

### If build is slow on first run:
- This is normal! Maven is downloading dependencies from the internet
- First build takes longer (several minutes)
- Subsequent builds are faster

### If you need to increase heap memory:
```powershell
$env:MAVEN_OPTS = "-Xmx1024m"
mvn clean install
```

## Verification Checklist

- [x] Java 21 installed
- [x] Maven 3.9.11 installed to C:\Maven
- [x] MAVEN_HOME environment variable set
- [x] Maven bin added to PATH
- [x] PowerShell profile updated
- [x] Maven verified working

## Next Steps

1. **Open a new PowerShell window** (important!)
2. **Navigate to project**:
   ```powershell
   cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
   ```

3. **Run the build**:
   ```powershell
   mvn clean install -DskipTests
   ```

4. **Start the application**:
   ```powershell
   mvn spring-boot:run
   ```

5. **Test the API**:
   ```powershell
   curl http://localhost:8080/api/...
   ```

## Support Information

- **Maven Documentation**: https://maven.apache.org/
- **Maven Central Repository**: https://repo.maven.apache.org/
- **Java 21 Documentation**: https://docs.oracle.com/en/java/javase/21/
- **Spring Boot Maven Plugin**: https://docs.spring.io/spring-boot/docs/current/maven-plugin/

---

## Configuration Summary

| Item | Value |
|------|-------|
| Java Version | 21.0.8 LTS |
| Maven Version | 3.9.11 |
| Maven Home | C:\Maven\apache-maven-3.9.11 |
| Maven Bin | C:\Maven\apache-maven-3.9.11\bin |
| Project Path | D:\CDAC Project\Atharva\Atharva\real-estate-backend |
| Application Port | 8080 (default) |

---

**You're all set! Maven is ready to build your project.** 🎉

**IMPORTANT**: Close and reopen your PowerShell window for environment changes to take full effect!
