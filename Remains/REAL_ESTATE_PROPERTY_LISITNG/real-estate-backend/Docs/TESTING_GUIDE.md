# Testing Guide - Contact Agent & Schedule Viewing APIs

## Overview
This guide provides step-by-step instructions to test all endpoints for the Contact Agent and Schedule Viewing features.

---

## Prerequisites

1. **Start the Application**
   ```bash
   cd "D:\CDAC Project\Atharva\Atharva\real-estate-backend"
   mvn spring-boot:run
   ```
   Application will run on: `http://localhost:8080`

2. **Test Tools** (choose one):
   - cURL (command line)
   - Postman (GUI)
   - Thunder Client (VS Code)
   - REST Client (IDE)

3. **Sample Data** (create these first via UserController and PropertyController):
   - User ID: 1 (test user)
   - User ID: 2 (property owner/agent)
   - Property ID: 1 (test property)
   - Property ID: 2 (another property)

---

## Contact Agent API Tests

### Test 1: Create Contact Inquiry
**Endpoint:** `POST /api/contact-agents`

**Request:**
```bash
curl -X POST http://localhost:8080/api/contact-agents \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "propertyId": 1,
    "subject": "Interested in Property",
    "message": "I would like more information about this property and its amenities.",
    "senderName": "John Doe",
    "senderEmail": "john@example.com",
    "senderPhone": "+1-555-0123",
    "additionalInfo": "Available for viewing on weekends after 2 PM"
  }'
```

**Expected Response:** 201 CREATED
```json
{
  "success": true,
  "message": "Contact created successfully",
  "data": {
    "id": 1,
    "user": { "id": 1, ... },
    "property": { "id": 1, ... },
    "subject": "Interested in Property",
    "senderName": "John Doe",
    "senderEmail": "john@example.com",
    "isRead": false,
    "createdAt": "2026-01-26T10:30:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 201
- Contact ID is generated
- isRead is false
- createdAt is set

---

### Test 2: Get Unread Contacts
**Endpoint:** `GET /api/contact-agents/unread`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/unread
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "isRead": false,
      ...
    }
  ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns list (may be empty)
- All items have isRead=false

---

### Test 3: Get Contacts by Property
**Endpoint:** `GET /api/contact-agents/property/{propertyId}`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/property/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "property": { "id": 1, ... },
      ...
    }
  ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns list of contacts for property 1
- All contacts belong to property 1

---

### Test 4: Get Contacts by User
**Endpoint:** `GET /api/contact-agents/user/{userId}`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/user/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns list of contacts sent by user 1

---

### Test 5: Get Contacts for Property Owner
**Endpoint:** `GET /api/contact-agents/owner/{ownerId}`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/owner/2
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns all contacts received by owner 2
- These are inquiries about their properties

---

### Test 6: Get Unread Contacts for Owner
**Endpoint:** `GET /api/contact-agents/owner/{ownerId}/unread`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/owner/2/unread
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns only unread contacts for owner 2

---

### Test 7: Get Contact by ID
**Endpoint:** `GET /api/contact-agents/{id}`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": {
    "id": 1,
    ...
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns contact details with ID 1

---

### Test 8: Mark Contact as Read
**Endpoint:** `PATCH /api/contact-agents/{id}/read`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/contact-agents/1/read
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Contact marked as read",
  "data": {
    "id": 1,
    "isRead": true,
    "respondedAt": "2026-01-26T10:35:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- isRead changes to true
- respondedAt is set to current time

---

### Test 9: Get Unread Count
**Endpoint:** `GET /api/contact-agents/owner/{ownerId}/unread-count`

**Request:**
```bash
curl http://localhost:8080/api/contact-agents/owner/2/unread-count
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": {
    "unreadCount": 2
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns unread count as integer

---

### Test 10: Delete Contact
**Endpoint:** `DELETE /api/contact-agents/{id}`

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/contact-agents/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Contact deleted successfully",
  "data": null
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Contact is removed from database
- Subsequent GET /api/contact-agents/1 returns 404

---

## Schedule Viewing API Tests

### Test 1: Schedule Viewing
**Endpoint:** `POST /api/schedule-viewings`

**Request:**
```bash
curl -X POST http://localhost:8080/api/schedule-viewings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "propertyId": 1,
    "viewingDate": "2026-02-15",
    "viewingTime": "14:30:00",
    "notes": "I prefer afternoon times. Please confirm."
  }'
```

**Expected Response:** 201 CREATED
```json
{
  "success": true,
  "message": "Viewing scheduled successfully",
  "data": {
    "id": 1,
    "user": { "id": 1, ... },
    "property": { "id": 1, ... },
    "viewingDate": "2026-02-15",
    "viewingTime": "14:30:00",
    "status": "PENDING",
    "notes": "I prefer afternoon times...",
    "createdAt": "2026-01-26T10:30:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 201
- Viewing ID is generated
- Status is PENDING
- All timestamps set correctly

---

### Test 2: Test Future Date Validation
**Endpoint:** `POST /api/schedule-viewings`

**Request with Past Date:**
```bash
curl -X POST http://localhost:8080/api/schedule-viewings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "propertyId": 1,
    "viewingDate": "2025-01-01",
    "viewingTime": "14:30:00"
  }'
```

**Expected Response:** 400 BAD_REQUEST
```json
{
  "success": false,
  "message": "Viewing date must be in the future",
  "data": null
}
```

✅ **Test Pass Criteria:**
- Status code is 400
- Error message mentions future date requirement
- Viewing is not created

---

### Test 3: Get User's Viewings
**Endpoint:** `GET /api/schedule-viewings/user/{userId}`

**Request:**
```bash
curl http://localhost:8080/api/schedule-viewings/user/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "user": { "id": 1, ... },
      ...
    }
  ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns viewings for user 1
- Sorted by viewingDate ascending

---

### Test 4: Get User's Viewings by Status
**Endpoint:** `GET /api/schedule-viewings/user/{userId}/status/{status}`

**Request:**
```bash
curl http://localhost:8080/api/schedule-viewings/user/1/status/PENDING
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns only PENDING viewings for user 1

---

### Test 5: Get Property's Viewings
**Endpoint:** `GET /api/schedule-viewings/property/{propertyId}`

**Request:**
```bash
curl http://localhost:8080/api/schedule-viewings/property/1
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns all viewings for property 1

---

### Test 6: Get Viewings by Date Range
**Endpoint:** `GET /api/schedule-viewings/date-range?startDate=2026-01-01&endDate=2026-12-31`

**Request:**
```bash
curl "http://localhost:8080/api/schedule-viewings/date-range?startDate=2026-01-01&endDate=2026-12-31"
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns viewings within date range
- Sorted by viewingDate

---

### Test 7: Get Owner's Viewings
**Endpoint:** `GET /api/schedule-viewings/owner/{ownerId}`

**Request:**
```bash
curl http://localhost:8080/api/schedule-viewings/owner/2
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": [ ... ]
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns viewings for properties owned by user 2

---

### Test 8: Confirm Viewing
**Endpoint:** `PUT /api/schedule-viewings/{id}/confirm`

**Request:**
```bash
curl -X PUT http://localhost:8080/api/schedule-viewings/1/confirm
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Viewing confirmed successfully",
  "data": {
    "id": 1,
    "status": "CONFIRMED",
    "confirmedAt": "2026-01-26T10:35:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Status changes to CONFIRMED
- confirmedAt is set

---

### Test 9: Confirm Non-PENDING Viewing (Should Fail)
**Endpoint:** `PUT /api/schedule-viewings/{id}/confirm`

**Request:** (after confirming viewing 1)
```bash
curl -X PUT http://localhost:8080/api/schedule-viewings/1/confirm
```

**Expected Response:** 400 BAD_REQUEST
```json
{
  "success": false,
  "message": "Only pending viewings can be confirmed",
  "data": null
}
```

✅ **Test Pass Criteria:**
- Status code is 400
- Error message about viewing status
- Viewing status unchanged

---

### Test 10: Reject Viewing
**Endpoint:** `PUT /api/schedule-viewings/{id}/reject?rejectionReason=Property+sold`

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/schedule-viewings/2/reject?rejectionReason=Property+already+sold"
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Viewing rejected successfully",
  "data": {
    "id": 2,
    "status": "REJECTED",
    "rejectionReason": "Property already sold",
    "rejectedAt": "2026-01-26T10:35:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Status changes to REJECTED
- rejectionReason is saved
- rejectedAt is set

---

### Test 11: Complete Viewing
**Endpoint:** `PUT /api/schedule-viewings/{id}/complete`

**Request:** (after confirming viewing 1)
```bash
curl -X PUT http://localhost:8080/api/schedule-viewings/1/complete
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Viewing marked as completed",
  "data": {
    "id": 1,
    "status": "COMPLETED",
    "completedAt": "2026-01-26T15:00:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Status changes to COMPLETED
- completedAt is set

---

### Test 12: Cancel Viewing
**Endpoint:** `PUT /api/schedule-viewings/{id}/cancel`

**Request:** (create new PENDING viewing first)
```bash
curl -X PUT http://localhost:8080/api/schedule-viewings/3/cancel
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Viewing cancelled successfully",
  "data": {
    "id": 3,
    "status": "CANCELLED",
    "cancelledAt": "2026-01-26T10:40:00"
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Status changes to CANCELLED
- cancelledAt is set

---

### Test 13: Delete Viewing
**Endpoint:** `DELETE /api/schedule-viewings/{id}`

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/schedule-viewings/3
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "message": "Viewing deleted successfully",
  "data": null
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Viewing is removed from database

---

### Test 14: Get Confirmed Viewing Count
**Endpoint:** `GET /api/schedule-viewings/property/{propertyId}/confirmed-count`

**Request:**
```bash
curl http://localhost:8080/api/schedule-viewings/property/1/confirmed-count
```

**Expected Response:** 200 OK
```json
{
  "success": true,
  "data": {
    "confirmedCount": 2
  }
}
```

✅ **Test Pass Criteria:**
- Status code is 200
- Returns count of CONFIRMED viewings

---

## Error Cases to Test

### Invalid Resource IDs
```bash
# Should return 404
curl http://localhost:8080/api/contact-agents/9999
curl http://localhost:8080/api/schedule-viewings/9999
```

### Invalid Request Body
```bash
# Missing required fields
curl -X POST http://localhost:8080/api/contact-agents \
  -H "Content-Type: application/json" \
  -d '{ "userId": 1 }'
# Expected: 400 BAD_REQUEST with validation errors
```

### Invalid Email Format
```bash
curl -X POST http://localhost:8080/api/contact-agents \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "propertyId": 1,
    "subject": "Test",
    "message": "Test",
    "senderName": "John",
    "senderEmail": "invalid-email",
    "senderPhone": "555-1234"
  }'
# Expected: 400 BAD_REQUEST - invalid email format
```

---

## Postman Collection Import

Create a Postman collection file (`Real-Estate-API.postman_collection.json`) and import it:

```json
{
  "info": {
    "name": "Real Estate API",
    "description": "Contact Agent & Schedule Viewing APIs"
  },
  "item": [
    {
      "name": "Contact Agent",
      "item": [
        {
          "name": "Create Contact",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/contact-agents",
            "body": { ... }
          }
        }
        // ... more endpoints
      ]
    },
    {
      "name": "Schedule Viewing",
      "item": [ ... ]
    }
  ]
}
```

---

## Checklist

Use this checklist to verify all features work:

### Contact Agent
- [ ] Create contact (valid data)
- [ ] Create contact (invalid user ID)
- [ ] Create contact (invalid property ID)
- [ ] Create contact (invalid email)
- [ ] Get unread contacts
- [ ] Get contacts by property
- [ ] Get contacts by user
- [ ] Get contacts for owner
- [ ] Get unread for owner
- [ ] Mark contact as read
- [ ] Get by ID
- [ ] Delete contact
- [ ] Get unread count

### Schedule Viewing
- [ ] Schedule viewing (valid future date)
- [ ] Schedule viewing (past date - should fail)
- [ ] Get user's viewings
- [ ] Get user's viewings by status
- [ ] Get property's viewings
- [ ] Get property's viewings by status
- [ ] Get viewings by date range
- [ ] Get owner's viewings
- [ ] Get owner's viewings by status
- [ ] Confirm PENDING viewing
- [ ] Confirm non-PENDING viewing (should fail)
- [ ] Reject PENDING viewing
- [ ] Complete CONFIRMED viewing
- [ ] Cancel viewing
- [ ] Delete viewing
- [ ] Get confirmed count

All tests passing ✅ = Implementation complete!
