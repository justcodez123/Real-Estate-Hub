# Fix Summary - Request Mapping Conflicts

## Issue
**Error:** "Command not found exception" when accessing certain API endpoints

**Root Cause:** Spring's path variable matcher was incorrectly routing requests. When a URL like `/api/contact-agents/property/1` was requested, the generic `@GetMapping("/{id}")` endpoint was being matched instead of the specific `@GetMapping("/property/{propertyId}")` endpoint.

This occurs because Spring evaluates URL patterns in the order they are defined, and the generic `{id}` path matches any single path segment.

## Solution Applied

### Problem Pattern
```
❌ INCORRECT ORDER (causes routing conflicts)
@GetMapping("/{id}")                        // Matches FIRST - matches /property/1 as id=property/1
@GetMapping("/property/{propertyId}")       // Never reached
@GetMapping("/user/{userId}")               // Never reached
@GetMapping("/owner/{ownerId}")             // Never reached
```

### Fixed Pattern
```
✅ CORRECT ORDER (specific paths first)
@PostMapping                                // Create
@GetMapping("/unread")                      // Specific static paths
@GetMapping("/date-range")                  // Specific static paths
@GetMapping("/property/{propertyId}")       // Multi-segment paths
@GetMapping("/property/{propertyId}/...")   // Longer paths
@GetMapping("/user/{userId}")               // Multi-segment paths
@GetMapping("/owner/{ownerId}")             // Multi-segment paths
@GetMapping("/{id}")                        // Generic single-segment LAST
@PutMapping("/{id}/...")                    // Operations on generic id
@DeleteMapping("/{id}")                     // Delete generic id
```

## Files Fixed

### 1. ContactAgentController.java
**Changes Made:**
- ✅ Removed unused imports (`ContactAgentResponse`, `LocalDateTime`)
- ✅ Reordered endpoint mappings:
  - POST (Create) - first
  - Specific endpoints (/unread, /property/{}, /user/{}, /owner/{}) - middle
  - Generic endpoints (/{id}, /{id}/read) - last
  - DELETE /{id} - last

**New Endpoint Order:**
1. POST /api/contact-agents
2. GET /api/contact-agents/unread
3. GET /api/contact-agents/property/{propertyId}
4. GET /api/contact-agents/user/{userId}
5. GET /api/contact-agents/owner/{ownerId}
6. GET /api/contact-agents/owner/{ownerId}/unread
7. GET /api/contact-agents/owner/{ownerId}/unread-count
8. GET /api/contact-agents/{id}
9. PATCH /api/contact-agents/{id}/read
10. DELETE /api/contact-agents/{id}

### 2. ScheduleViewingController.java
**Changes Made:**
- ✅ Reordered endpoint mappings:
  - POST (Create) - first
  - Specific paths (/date-range, /property/{}, /user/{}, /owner/{}) - middle
  - Generic endpoints (/{id}, /{id}/confirm, /{id}/reject, etc.) - last

**New Endpoint Order:**
1. POST /api/schedule-viewings
2. GET /api/schedule-viewings/date-range (query params)
3. GET /api/schedule-viewings/user/{userId}
4. GET /api/schedule-viewings/user/{userId}/status/{status}
5. GET /api/schedule-viewings/property/{propertyId}
6. GET /api/schedule-viewings/property/{propertyId}/status/{status}
7. GET /api/schedule-viewings/property/{propertyId}/confirmed-count
8. GET /api/schedule-viewings/owner/{ownerId}
9. GET /api/schedule-viewings/owner/{ownerId}/status/{status}
10. GET /api/schedule-viewings/{id}
11. PUT /api/schedule-viewings/{id}/confirm
12. PUT /api/schedule-viewings/{id}/reject
13. PUT /api/schedule-viewings/{id}/complete
14. PUT /api/schedule-viewings/{id}/cancel
15. DELETE /api/schedule-viewings/{id}

## Testing the Fix

### Before Fix (Would Fail)
```
GET /api/contact-agents/property/5
→ Would try to treat "property/5" as a single id parameter
→ Returns 404 or error
```

### After Fix (Works Correctly)
```
GET /api/contact-agents/property/5
→ Matches @GetMapping("/property/{propertyId}")
→ propertyId = 5
→ Returns list of contacts for property 5
✅ Success
```

## Best Practices Applied

1. **URL Pattern Ordering Rule:** In Spring REST controllers, always define URL patterns in this order:
   - POST operations (create)
   - Static paths (e.g., /unread, /date-range)
   - Multi-segment paths with path variables
   - Generic parameterized paths (/{id}, /{id}/action)
   - DELETE operations

2. **Path Variable Naming:** Use descriptive names:
   - `{id}` - for primary resource identifier
   - `{propertyId}` - when accessing from property context
   - `{userId}` - when accessing from user context
   - `{ownerId}` - when accessing from owner context

3. **Comments in Code:** Added comments like:
   ```java
   // Specific paths must come before /{id} to avoid routing conflicts
   // Generic {id} path comes last
   ```

## Spring Framework Behavior

Spring's RequestMappingHandlerMapping uses the following algorithm:
1. Checks for exact literal match first
2. Evaluates patterns in declaration order
3. First matching pattern wins
4. `{variable}` can match any path segment

**Example of matching process:**
```
Request: GET /api/contact-agents/property/5

Evaluates in order:
1. POST ❌ (wrong HTTP method)
2. GET /unread ❌ (doesn't match "property/5")
3. GET /property/{propertyId} ✅ MATCH FOUND
   → propertyId = 5
   → Executes getContactsByProperty(5)
```

If `/{id}` came first:
```
1. POST ❌ (wrong HTTP method)
2. GET /{id} ✅ MATCH FOUND (but WRONG!)
   → id = "property"
   → Tries to find contact with id="property"
   → Returns 404 or error
```

## Verification

All endpoints should now work correctly:

✅ ContactAgent endpoints:
- POST /api/contact-agents - Create
- GET /api/contact-agents/unread - Get unread
- GET /api/contact-agents/property/{propertyId} - By property
- GET /api/contact-agents/user/{userId} - By user
- GET /api/contact-agents/owner/{ownerId} - By owner
- GET /api/contact-agents/{id} - Get by ID
- PATCH /api/contact-agents/{id}/read - Mark as read
- DELETE /api/contact-agents/{id} - Delete

✅ ScheduleViewing endpoints:
- POST /api/schedule-viewings - Schedule
- GET /api/schedule-viewings/date-range?startDate=&endDate= - Date range
- GET /api/schedule-viewings/user/{userId} - User's viewings
- GET /api/schedule-viewings/property/{propertyId} - Property's viewings
- GET /api/schedule-viewings/owner/{ownerId} - Owner's viewings
- GET /api/schedule-viewings/{id} - Get by ID
- PUT /api/schedule-viewings/{id}/confirm - Confirm
- PUT /api/schedule-viewings/{id}/reject - Reject
- PUT /api/schedule-viewings/{id}/complete - Complete
- PUT /api/schedule-viewings/{id}/cancel - Cancel
- DELETE /api/schedule-viewings/{id} - Delete

---

## Additional Resources

- Spring MVC Path Pattern Matching: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping.html
- REST API Best Practices: https://restfulapi.net/
- Spring Boot RESTful API: https://spring.io/guides/gs/rest-service/
