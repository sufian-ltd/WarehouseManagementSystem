package com.warehouse.service;

import java.util.List;

import com.warehouse.entity.Storage;

public interface StorageService {

	public List<Storage> findAll();

	public Storage findById(Integer id);

	public void save(Storage storage);

	public void deleteById(Integer id);

	public List<Storage> search(String key1,String key2);
	
	List<Storage> findByIsActive(Integer isActive);
	
}
