# ⚡ INSTANT REFERENCE - ADD IMAGES

## One Command Solution

### Copy & Paste This Command:
```bash
curl -X POST http://localhost:8080/api/properties/images/bulk/add-images-to-empty-properties
```

### That's All You Need!

---

## Expected Output:
```json
{
  "success": true,
  "message": "Successfully added images to X properties",
  "data": null
}
```

---

## What Happens:
1. ✅ Finds all properties WITHOUT images
2. ✅ Assigns 3-5 images to each
3. ✅ Saves to database
4. ✅ All persisted ✅

---

## Time Required:
- Start app: 30 seconds
- Run command: 10 seconds
- Verify: 1 minute
- **Total: 2 minutes**

---

## Verify It Worked:

### Option 1: Database Query
```sql
SELECT COUNT(*) FROM property_images;
```

### Option 2: API Call
```
GET http://localhost:8080/api/properties/1/details
```
(Should show images in response)

---

## Three Endpoints Available:

| Command | Use Case |
|---------|----------|
| `add-images-to-empty-properties` | Safe - fill missing only |
| `add-images-to-all-properties` | Refresh - replace all |
| `{propertyId}/bulk-add-images` | Single - one property |

---

## Images Included:
✅ Living room
✅ Bedroom
✅ Kitchen
✅ Bathroom
✅ Dining room
✅ Exterior
✅ Pool
✅ Office
✅ Apartment
✅ More...

---

## That's It!
All remaining properties now have images. ✅

See full guide: BULK_ADD_IMAGES_COMPLETE_GUIDE.md
