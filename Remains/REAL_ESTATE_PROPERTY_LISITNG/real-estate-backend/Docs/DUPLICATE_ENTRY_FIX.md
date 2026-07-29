curl -X GET "http://localhost:8080/api/properties/recommendations?location=Mumbai&budget=5000000"# Duplicate Entry Fix - DataInitializer

## Problem
The application was failing to start with the error:
```
Duplicate entry 'john.smith@realestate.com' for key 'users.UK_6dotkott2kjsp8vw4d0m25fb7'
```

This occurred in the `DataInitializer` class which is responsible for populating sample data on application startup.

## Root Cause
The `DataInitializer.java` was attempting to insert the same sample users every time the application started, without checking if the data already existed in the database. This caused a constraint violation when the unique email constraint was triggered.

## Solution
Added a check at the beginning of the `initDatabase()` method to detect if sample data already exists:

```java
// Check if data already exists to avoid duplicate entries
if (userRepository.findByEmail("john.smith@realestate.com").isPresent()) {
    System.out.println("✅ Sample data already exists in database. Skipping initialization.");
    return;
}
```

## Changes Made
**File**: `src/main/java/com/realestate/config/DataInitializer.java`

- Added a guard clause that checks if the sample user "john.smith@realestate.com" already exists
- If the data exists, the initialization is skipped with a status message
- If the data doesn't exist, it proceeds with normal initialization

## Benefits
1. **Prevents Duplicate Entry Errors**: The application no longer fails on startup when sample data already exists
2. **Idempotent Initialization**: The data initializer can be run multiple times without errors
3. **Better User Experience**: No more cryptic database constraint violation errors

## Testing
To verify the fix:
1. Run the application normally - it will create sample data if none exists
2. Stop and restart the application - it will now skip initialization and report "Sample data already exists"
3. The application should start successfully in both cases

## Notes
- The check uses `findByEmail("john.smith@realestate.com")` which is guaranteed to exist if sample data was initialized
- This is the least intrusive way to check for existing data without querying multiple tables
- The solution leverages the existing `findByEmail()` method from `UserRepository`
