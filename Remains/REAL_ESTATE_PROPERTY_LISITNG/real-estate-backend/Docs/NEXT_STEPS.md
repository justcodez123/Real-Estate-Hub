# Next Steps: Adding New Features

## Completed ✅
1. **Fixed all compilation errors** - All Lombok annotations added
2. **Database connectivity** - MySQL is properly configured
3. **User registration and authentication** - Already implemented

## TODO: New Features to Implement

### 1. **Add to Favorites** 
**Status**: ⏳ Ready for Implementation
- Model exists: `Favorite.java`
- Controller needs: `FavoriteController.java`
- Service needs: `FavoriteService.java`
- Requirements:
  - Add/Remove property from favorites
  - List user's favorite properties
  - Check if property is favorite

### 2. **User Registration Form** 
**Status**: ✅ Already Implemented
- Registration endpoint: `POST /api/auth/register`
- DTOs: `RegistrationRequest`, `UserResponse`
- Types supported: BUYER, AGENT

### 3. **Agent Registration**
**Status**: ⚠️ Partial - Needs Enhancement
- Registration endpoint exists: `POST /api/auth/register`
- License number validation: Implemented
- Additional needed:
  - Agent-specific profile fields
  - License verification endpoint (optional)
  - Agent listing/search endpoint

### 4. **Agent Login**
**Status**: ✅ Already Implemented
- Login endpoint: `POST /api/auth/login`
- Role-based access control ready

### 5. **Schedule Viewing**
**Status**: ⏳ Ready for Implementation
- Model needed: `PropertyViewing.java`
- Controller needed: `PropertyViewingController.java`
- Service needed: `PropertyViewingService.java`
- Requirements:
  - Create viewing appointment
  - List viewings for property/user
  - Cancel viewing
  - Get available time slots

### 6. **Add Images to Property**
**Status**: ⚠️ Partial - Needs Completion
- Model exists: `PropertyImage.java`
- Database table: `property_images`
- Needs:
  - Upload image endpoint (POST)
  - Delete image endpoint (DELETE)
  - Reorder images functionality
  - Set primary image functionality
  - File storage service (S3/Local)

### 7. **Builder Groups**
**Status**: ⚠️ Model exists, Needs Implementation
- Model needed: `BuilderGroup.java`
- Examples: TCG, Shapoorji Pallonji
- Controller needed: `BuilderGroupController.java`
- Functionality:
  - CRUD operations for builder groups
  - Filter properties by builder group
  - Get all properties under a builder

## Implementation Priority

1. **High Priority** (Core functionality):
   - Fix subscription persistence issue
   - Complete Builder Groups implementation
   - Add Images to Property functionality

2. **Medium Priority**:
   - Schedule Viewing system
   - Add to Favorites

3. **Nice to Have**:
   - Enhanced Agent features
   - Additional filters

## Database Schema Updates Needed

```sql
-- Already exists
ALTER TABLE properties ADD COLUMN builder_group_id BIGINT;
ALTER TABLE properties ADD FOREIGN KEY (builder_group_id) REFERENCES builder_groups(id);

-- May need for viewing
CREATE TABLE property_viewings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    property_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    viewing_date DATE NOT NULL,
    viewing_time TIME NOT NULL,
    notes VARCHAR(500),
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Current Issues to Resolve

### 1. **Subscription Not Persisting**
- Issue: `subscription_type` column cannot be null
- Solution: Ensure default value is set before saving
- File: `UserService.java` line 64

### 2. **CORS Issues**
- Add @CrossOrigin to all controllers
- Currently fixed in: `SubscriptionController.java`
- Needs to be added to all other controllers

### 3. **Image Upload**
- Need to implement file upload service
- Consider: Local storage or AWS S3
- Need multipart/form-data handling

### 4. **Currency Conversion (USD to INR)**
- Add currency conversion service
- Exchange rate API integration
- Store prices in both currencies or conversion factor

## Quick Start Commands

```bash
# Compile project
mvn clean compile

# Run tests
mvn test

# Build JAR
mvn clean package

# Run application
java -jar target/real-estate-backend-1.0.0.jar

# Or through Maven
mvn spring-boot:run
```

## Testing API Endpoints

All endpoints require proper CORS headers and should be tested with:
- Postman
- cURL
- Frontend application (localhost:3001)

## Next Session Action Items

1. ✅ Compilation issues - FIXED
2. ⏳ Run application and verify no errors
3. ⏳ Test existing endpoints
4. ⏳ Implement missing features in priority order
