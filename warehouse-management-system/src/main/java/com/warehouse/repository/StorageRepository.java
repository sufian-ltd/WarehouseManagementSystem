package com.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {
	
	List<Storage> findByIsActive(Integer isActive);
	List<Storage> findByNameOrLocation(String key1,String key2);
}
