package com.real_estate.real_estate_backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	private String imageURL;
	/*Is this the main photo to show on this card*/
	private boolean isPrimary;
	
	/*The link to the parent property*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	@JsonIgnore
	
	private Property property;
	
}
