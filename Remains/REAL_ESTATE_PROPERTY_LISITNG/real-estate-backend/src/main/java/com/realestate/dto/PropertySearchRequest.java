package com.realestate.dto;

import com.realestate.model.ListingType;
import com.realestate.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearchRequest {
    
    private String keyword;
    private String city;
    private String state;
    private String zipCode;
    
    private PropertyType propertyType;
    private ListingType listingType;
    
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    private Integer minBedrooms;
    private Integer maxBedrooms;
    
    private Integer minBathrooms;
    private Integer maxBathrooms;
    
    private BigDecimal minSquareFeet;
    private BigDecimal maxSquareFeet;
    
    private Integer minYearBuilt;
    private Integer maxYearBuilt;
    
    private Boolean available;
    
    // Sorting
    private String sortBy = "createdAt";  // price, createdAt, squareFeet, bedrooms
    private String sortDirection = "DESC"; // ASC or DESC
    
    // Pagination
    private Integer page = 0;
    private Integer size = 10;
}
