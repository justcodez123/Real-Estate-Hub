package com.real_estate.real_estate_backend.controller;

import com.real_estate.real_estate_backend.dto.ApiResponse;
import com.real_estate.real_estate_backend.models.Property;
import com.real_estate.real_estate_backend.models.PropertyImage;
import com.real_estate.real_estate_backend.repository.PropertyImageRepository;
import com.real_estate.real_estate_backend.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 1. Tells Spring: "This class handles web requests"
@RequestMapping("/api/properties") // 2. The base URL: http://localhost:8080/api/properties
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // 3. Allows React (Port 3000) to talk to 8080
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;
    
    //Injecting the Repository of Images
    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @GetMapping("/available")
    public ApiResponse getAvailableProperties() {
        // For now, we return ALL properties as "available"
        // Later we can add: propertyRepository.findByAvailableTrue();
        List<Property> properties = propertyRepository.findAll();
        return new ApiResponse(true, "Properties fetched successfully", properties);
    }

    // GET: Get all houses
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // POST: Add a new house
    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }
    
    // GET: Get one specific house by ID
    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));
    }
    
    // DELETE: Remove a house
    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
    }
    
    //Mapping for getting the images regarding to the primary Images
    @GetMapping("/{id}/images")
    public ApiResponse getPropertyImages(@PathVariable Long id) {
    	/*Check if property exists*/
    	if(!propertyRepository.existsById(id)) {
    		return new ApiResponse(false, "Property Not found", null);
    	}
    	
    	/*If found, fetch images*/
    	List<PropertyImage> images = propertyImageRepository.findByPropertyId(id);
    	
    	//Return wrapped in ur ApiResponse
    	return new ApiResponse(true, "Images Fetched ", images);
    	
    }
    
    
}