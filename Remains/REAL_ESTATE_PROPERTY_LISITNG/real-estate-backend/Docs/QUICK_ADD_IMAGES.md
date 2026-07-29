# 🚀 QUICK START - ADD IMAGES TO PROPERTIES

## One Command to Add Images

### Using cURL (Easiest)
```bash
curl -X POST http://localhost:8080/api/properties/images/bulk/add-images-to-empty-properties
```

**Expected Output:**
```json
{"success":true,"message":"Successfully added images to 5 properties","data":null}
```

---

## That's It! ✅

Your properties now have images:
✅ 10 different high-quality images
✅ 3-5 images per property
✅ Persisted in database
✅ Ready to display in frontend

---

## Three Options Available

### Option 1: Add to Empty Properties Only
```
POST /api/properties/images/bulk/add-images-to-empty-properties
```
Adds images ONLY to properties without images

### Option 2: Add to ALL Properties  
```
POST /api/properties/images/bulk/add-images-to-all-properties
```
Replaces images on all properties (careful!)

### Option 3: Add to Specific Property
```
POST /api/properties/{propertyId}/bulk-add-images?numberOfImages=3
```
Example: `POST /api/properties/1/bulk-add-images?numberOfImages=3`

---

## Verify It Worked

Check database:
```sql
SELECT COUNT(*) FROM property_images;
```

View in API:
```
GET http://localhost:8080/api/properties/1/details
```

---

## Images Added

✅ Living room
✅ Modern house exterior
✅ Bedroom  
✅ Kitchen
✅ Bathroom
✅ Dining room
✅ Property exterior
✅ Pool area
✅ Modern apartment
✅ Office space

---

**Time**: 1 minute  
**Result**: All properties with images  
**Status**: ✅ DONE
