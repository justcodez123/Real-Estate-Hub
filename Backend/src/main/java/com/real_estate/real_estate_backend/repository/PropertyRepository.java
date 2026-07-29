package com.real_estate.real_estate_backend.repository;

import com.real_estate.real_estate_backend.controller.enums.ListingType;
import com.real_estate.real_estate_backend.controller.enums.PropertyStatus;
import com.real_estate.real_estate_backend.controller.enums.PropertyType;
import com.real_estate.real_estate_backend.models.Property;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // This interface is currently empty, but it inherits powerful methods:
    // .save(Property p)   -> INSERT/UPDATE
    // .findAll()          -> SELECT *
    // .findById(Long id)  -> SELECT WHERE ID=...
    // .deleteById(Long id)-> DELETE
	
	//1. Pagination : Getting Available Properties
	Page<Property> findByStatus(PropertyStatus status, Pageable pageable);
		
	// 2. Filters (Standard Lists)
    List<Property> findByCityIgnoreCase(String city);
    List<Property> findByPropertyType(PropertyType type);
    List<Property> findByListingType(ListingType listingType);
    
    // 3. Price Range (Between)
    List<Property> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 4. Builder Group
    List<Property> findByBuilderGroupId(Long builderGroupId);
	
}