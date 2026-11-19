package com.real_estate.real_estate_backend.models;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Table(name = "PropertyImages")
public class PropertyImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("imageUrl")
	@JsonAlias({"imageURL", "imageUrl"})
	@Column(nullable = false, length = 1000)
	private String imageURL;
	
	/*Is this the main photo to show on this card*/
	@JsonProperty("isPrimary")
	@JsonAlias({"isPrimary", "primary"})
	private boolean isPrimary = false;
	
	/*To handle Frontend's reorderImages API*/
	@JsonProperty("displayOrder")
	@JsonAlias({"displayOrder", "orderIndex"})
	private int orderIndex = 0;
	
	/*The link to the parent property*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	@JsonIgnore
	private Property property;
	
}
