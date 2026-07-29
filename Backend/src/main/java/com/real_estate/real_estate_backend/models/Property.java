package com.real_estate.real_estate_backend.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.real_estate.real_estate_backend.controller.enums.ListingType;
import com.real_estate.real_estate_backend.controller.enums.PropertyStatus;
import com.real_estate.real_estate_backend.controller.enums.PropertyType;

@Entity
@Table(name = "properties")
@Data   // ✅ Generates ALL getters, setters, toString, equals, hashCode automatically
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    private String address;
    private String city;       
    private String state;
    private String pincode;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyImage> images = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "builder_group_id")
    private BuilderGroup builderGroup;

    // ✅ No getters/setters needed — @Data handles everything!
}