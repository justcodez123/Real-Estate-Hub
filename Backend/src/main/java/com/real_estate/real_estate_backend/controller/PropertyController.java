package com.real_estate.real_estate_backend.controller;

import com.real_estate.real_estate_backend.controller.enums.PropertyStatus;
import com.real_estate.real_estate_backend.dto.ApiResponse;
import com.real_estate.real_estate_backend.models.Property;
import com.real_estate.real_estate_backend.models.PropertyImage;
import com.real_estate.real_estate_backend.repository.PropertyImageRepository;
import com.real_estate.real_estate_backend.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;


    // --- 1. GET ALL (Paged) ---
    // GET /api/properties             → page 0, size 10 (defaults)
    // GET /api/properties?page=1&size=5
    @GetMapping                                                   // ✅ Only ONE @GetMapping now
    public Page<Property> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return propertyRepository.findAll(pageable);
    }


    // --- 2. GET AVAILABLE (Paged & Sorted) ---
    // GET /api/properties/available?page=0&size=10&sortBy=price&direction=ASC
    @GetMapping("/available")
    public Page<Property> getAvailableProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,   // ✅ 'price' exists in Property
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return propertyRepository.findByStatus(PropertyStatus.AVAILABLE, pageable);
    }


    // --- 3. GET BY ID ---
    // GET /api/properties/1
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // --- 4. GET IMAGES FOR A PROPERTY ---
    // GET /api/properties/1/images
    @GetMapping("/{id}/images")
    public ApiResponse getPropertyImages(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) {
            return new ApiResponse(false, "Property not found", null);
        }
        List<PropertyImage> images = propertyImageRepository.findByPropertyIdOrderByOrderIndexAsc(id);
        return new ApiResponse(true, "Images fetched successfully", images);
    }


    // --- 5. GET BY CITY ---
    // GET /api/properties/city/Pune
    @GetMapping("/city/{city}")
    public List<Property> getPropertiesByCity(@PathVariable String city) {
        return propertyRepository.findByCityIgnoreCase(city);
    }


    // --- 6. CREATE PROPERTY ---
    // POST /api/properties
    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        if (property.getStatus() == null) {
            property.setStatus(PropertyStatus.AVAILABLE);
        }
        Property saved = propertyRepository.save(property);
        return ResponseEntity.status(201).body(saved);   // ✅ Returns 201 Created, not 200
    }


    // --- 7. DELETE PROPERTY ---
    // DELETE /api/properties/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        propertyRepository.deleteById(id);
        return ResponseEntity.noContent().build();        // 204 No Content
    }
}