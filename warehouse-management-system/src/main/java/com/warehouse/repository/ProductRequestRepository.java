package com.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.ProductRequest;

@Repository
public interface ProductRequestRepository extends JpaRepository<ProductRequest, Integer>{

	List<ProductRequest> findByStatus(String status);
	
}
