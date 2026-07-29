package com.real_estate.real_estate_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.real_estate.real_estate_backend.models.Property;
import com.real_estate.real_estate_backend.models.PropertyImage;
import com.real_estate.real_estate_backend.repository.PropertyImageRepository;
import com.real_estate.real_estate_backend.repository.PropertyRepository;


@RestController
@RequestMapping("api/properties/{propertyId}/images")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PropertyImageController {
	
	@Autowired
	private PropertyImageRepository imageRepository;
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	// 1. GET Images (api.get(`/properties/${propertyId}/images`))
    @GetMapping
    public ResponseEntity<List<PropertyImage>> getImages(@PathVariable Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId));
    }

    // 2. ADD Image (api.post(`/properties/${propertyId}/images`, formData))
    @PostMapping
    public ResponseEntity<PropertyImage> addImage(@PathVariable Long propertyId, @RequestBody PropertyImage newImage) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) return ResponseEntity.notFound().build();

        newImage.setProperty(property);
        // If it's the first image, make it primary
        if (imageRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId).isEmpty()) {
            newImage.setPrimary(true);
        }
        
        return ResponseEntity.ok(imageRepository.save(newImage));
    }

    // 3. SET PRIMARY (api.patch(`/properties/${propertyId}/images/${imageId}/primary`))
    @PatchMapping("/{imageId}/primary")
    public ResponseEntity<Void> setPrimaryImage(@PathVariable Long propertyId, @PathVariable Long imageId) {
        List<PropertyImage> allImages = imageRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId);
        
        for (PropertyImage img : allImages) {
            // Set the target image to true, all others to false
            img.setPrimary(img.getId().equals(imageId)); 
        }
        
        imageRepository.saveAll(allImages);
        return ResponseEntity.ok().build();
    }

    // 4. DELETE Image (api.delete(`/properties/${propertyId}/images/${imageId}`))
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long propertyId, @PathVariable Long imageId) {
        if (!imageRepository.existsById(imageId)) {
            return ResponseEntity.notFound().build();
        }
        imageRepository.deleteById(imageId);
        return ResponseEntity.noContent().build();
    }
	
 // 5. UPDATE Image 
    @PutMapping("/{imageId}")
    public ResponseEntity<PropertyImage> updateImage(
            @PathVariable Long propertyId,
            @PathVariable Long imageId,
            @RequestBody PropertyImage updatedImageData) {

        // Find the image and make sure it actually belongs to this property
        PropertyImage existingImage = imageRepository.findById(imageId).orElse(null);
        if (existingImage == null || !existingImage.getProperty().getId().equals(propertyId)) {
            return ResponseEntity.notFound().build();
        }

        // Update the URL (or any other fields you add later, like captions)
        existingImage.setImageURL(updatedImageData.getImageURL());
        
        return ResponseEntity.ok(imageRepository.save(existingImage));
    }
    
 // 6. REORDER Images
    @PostMapping("/reorder")
    public ResponseEntity<Void> reorderImages(
            @PathVariable Long propertyId,
            @RequestBody Map<String, List<Long>> requestBody) {

        // Extract the array from the JSON object
        List<Long> imageIds = requestBody.get("imageIds");
        if (imageIds == null || imageIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Fetch all existing images for this property
        List<PropertyImage> existingImages = imageRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId);

        // Update the orderIndex to match the array position sent from React
        for (int i = 0; i < imageIds.size(); i++) {
            Long currentId = imageIds.get(i);
            
            for (PropertyImage img : existingImages) {
                if (img.getId().equals(currentId)) {
                    img.setOrderIndex(i); // i = 0, 1, 2, etc.
                    break;
                }
            }
        }

        // Save them all at once
        imageRepository.saveAll(existingImages);
        return ResponseEntity.ok().build();
    }
	
}
