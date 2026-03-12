package com.real_estate.real_estate_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.real_estate.real_estate_backend.models.PropertyImage;

/*A Interface that talks to the PropertyImages Table.*/
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long>{
	/*Find the images belonging to the specific property Id*/
	List<PropertyImage> findByPropertyId(Long PropertyId);
}
