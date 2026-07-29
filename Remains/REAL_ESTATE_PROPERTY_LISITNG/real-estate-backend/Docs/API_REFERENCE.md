# API Reference Guide - Contact Agent & Schedule Viewing

## Base URL
```
http://localhost:8080/api
```

---

## Contact Agent API

### 1. Create Contact Inquiry
**Endpoint:** `POST /contact-agents`

**Request Body:**
```json
{
  "userId": 1,
  "propertyId": 5,
  "subject": "Interested in Property",
  "message": "I am very interested in this property. Can we schedule a viewing?",
  "senderName": "John Doe",
  "senderEmail": "john@example.com",
  "senderPhone": "+1-555-0123",
  "additionalInfo": "Available weekends after 2 PM"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Contact created successfully",
  "data": {
    "id": 1,
    "user": { ... },
    "property": { ... },
    "subject": "Interested in Property",
    "message": "I am very interested...",
    "senderName": "John Doe",
    "senderEmail": "john@example.com",
    "senderPhone": "+1-555-0123",
    "additionalInfo": "Available weekends after 2 PM",
    "isRead": false,
    "createdAt": "2026-01-26T10:30:00"
  }
}
```

---

### 2. Get Contact by ID
**Endpoint:** `GET /contact-agents/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": { ... }
}
```

---

### 3. Get All Contacts for a Property
**Endpoint:** `GET /contact-agents/property/{propertyId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 4. Get All Contacts Sent by a User
**Endpoint:** `GET /contact-agents/user/{userId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 5. Get All Contacts Received by Property Owner
**Endpoint:** `GET /contact-agents/owner/{ownerId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 6. Get All Unread Contacts
**Endpoint:** `GET /contact-agents/unread`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 7. Get Unread Contacts for Owner
**Endpoint:** `GET /contact-agents/owner/{ownerId}/unread`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 8. Mark Contact as Read
**Endpoint:** `PATCH /contact-agents/{id}/read`

**Response (200 OK):**
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

---

### 9. Delete Contact
**Endpoint:** `DELETE /contact-agents/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Contact deleted successfully",
  "data": null
}
```

---

### 10. Get Unread Count for Owner
**Endpoint:** `GET /contact-agents/owner/{ownerId}/unread-count`

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "unreadCount": 5
  }
}
```

---

## Schedule Viewing API

### 1. Schedule New Viewing
**Endpoint:** `POST /schedule-viewings`

**Request Body:**
```json
{
  "userId": 1,
  "propertyId": 5,
  "viewingDate": "2026-02-15",
  "viewingTime": "14:30:00",
  "notes": "Please confirm availability. I prefer afternoon times."
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Viewing scheduled successfully",
  "data": {
    "id": 1,
    "user": { ... },
    "property": { ... },
    "viewingDate": "2026-02-15",
    "viewingTime": "14:30:00",
    "status": "PENDING",
    "notes": "Please confirm availability...",
    "createdAt": "2026-01-26T10:30:00"
  }
}
```

---

### 2. Get Viewing by ID
**Endpoint:** `GET /schedule-viewings/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": { ... }
}
```

---

### 3. Get User's Viewings
**Endpoint:** `GET /schedule-viewings/user/{userId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 4. Get User's Viewings by Status
**Endpoint:** `GET /schedule-viewings/user/{userId}/status/{status}`

**Status values:** `PENDING`, `CONFIRMED`, `REJECTED`, `COMPLETED`, `CANCELLED`

**Example:** `GET /schedule-viewings/user/1/status/PENDING`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 5. Get Property's Viewings
**Endpoint:** `GET /schedule-viewings/property/{propertyId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 6. Get Property's Viewings by Status
**Endpoint:** `GET /schedule-viewings/property/{propertyId}/status/{status}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 7. Get Owner's Received Viewings
**Endpoint:** `GET /schedule-viewings/owner/{ownerId}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 8. Get Owner's Viewings by Status
**Endpoint:** `GET /schedule-viewings/owner/{ownerId}/status/{status}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 9. Get Viewings in Date Range
**Endpoint:** `GET /schedule-viewings/date-range?startDate=2026-01-01&endDate=2026-12-31`

**Query Parameters:**
- `startDate` (required): YYYY-MM-DD format
- `endDate` (required): YYYY-MM-DD format

**Response (200 OK):**
```json
{
  "success": true,
  "data": [ ... ]
}
```

---

### 10. Confirm Viewing
**Endpoint:** `PUT /schedule-viewings/{id}/confirm`

**Response (200 OK):**
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

---

### 11. Reject Viewing
**Endpoint:** `PUT /schedule-viewings/{id}/reject?rejectionReason=Property+already+sold`

**Query Parameters:**
- `rejectionReason` (optional): Reason for rejection

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Viewing rejected successfully",
  "data": {
    "id": 1,
    "status": "REJECTED",
    "rejectionReason": "Property already sold",
    "rejectedAt": "2026-01-26T10:35:00"
  }
}
```

---

### 12. Mark Viewing as Completed
**Endpoint:** `PUT /schedule-viewings/{id}/complete`

**Response (200 OK):**
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

---

### 13. Cancel Viewing
**Endpoint:** `PUT /schedule-viewings/{id}/cancel`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Viewing cancelled successfully",
  "data": {
    "id": 1,
    "status": "CANCELLED",
    "cancelledAt": "2026-01-26T10:35:00"
  }
}
```

---

### 14. Delete Viewing
**Endpoint:** `DELETE /schedule-viewings/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Viewing deleted successfully",
  "data": null
}
```

---

### 15. Get Confirmed Viewing Count for Property
**Endpoint:** `GET /schedule-viewings/property/{propertyId}/confirmed-count`

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "confirmedCount": 3
  }
}
```

---

## Error Response Examples

### 400 Bad Request - Validation Error
```json
{
  "success": false,
  "message": "Viewing date must be in the future",
  "data": null
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Property not found with id: '999'",
  "data": null
}
```

### 400 Bad Request - Invalid State Transition
```json
{
  "success": false,
  "message": "Only pending viewings can be confirmed",
  "data": null
}
```

---

## Status Transition Rules

### Viewing Status Workflow
```
PENDING ──confirm──> CONFIRMED ──complete──> COMPLETED
   │                    │
   │                    └──cancel──> CANCELLED
   │
   └──reject──> REJECTED
```

### Rules
- Can only **confirm** a PENDING viewing
- Can only **reject** a PENDING viewing
- Can only **complete** a CONFIRMED viewing
- Can **cancel** from PENDING or CONFIRMED state
- Cannot cancel REJECTED or COMPLETED viewings

---

## CORS Policy
Both APIs allow requests from `http://localhost:3000` (frontend development server).

---

## Authentication
Currently, these APIs do not require authentication. User ID and Owner ID are passed as parameters. In production, implement JWT or OAuth2 authentication.

---

## Rate Limiting
No rate limiting currently implemented. Consider adding for production use.

---

## Pagination
Pagination is not implemented. For large datasets, consider adding pagination support.

---

## Sorting & Filtering
Currently, results are sorted by:
- Contact Agent: `createdAt DESC` (newest first)
- Schedule Viewing: `viewingDate ASC` (earliest first)

Future enhancement: Add `sort` and `filter` query parameters.
