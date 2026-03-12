package com.real_estate.real_estate_backend.models;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.real_estate.real_estate_backend.controller.enums.ListingType;
import com.real_estate.real_estate_backend.controller.enums.PropertyType;

@Entity // 1. Tells Hibernate: "Make a table out of this class"
@Data
@Table(name = "properties") // 2. Name the table "properties"
public class Property {

    @Id // 3. This is the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto Import (1, 2, 3...)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    /* Enumerated DataTypes, Saving PropertyType and listingType  */
    private PropertyType propertyType;
    private ListingType listingType;
    private String address;

    /* Simple status to check, is property available */
    private boolean available = true;

    @Column(nullable = false)
    private BigDecimal price; // 5. Always use BigDecimal for money, not Double!

    @Column(name = "image_url")
    private String imageUrl; // 6. This will store your S3 URL
    
    /*One to Many Relationship, for addressing the primary Image of the property*/
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<PropertyImage> images = new ArrayList<>();
    

    // ---------------------------------------------------
    // Getters and Setters (Essential for Spring to work)
    // ---------------------------------------------------
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

