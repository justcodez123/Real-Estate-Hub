package com.real_estate.real_estate_backend.repository;

import com.real_estate.real_estate_backend.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // This interface is currently empty, but it inherits powerful methods:
    // .save(Property p)   -> INSERT/UPDATE
    // .findAll()          -> SELECT *
    // .findById(Long id)  -> SELECT WHERE ID=...
    // .deleteById(Long id)-> DELETE
}