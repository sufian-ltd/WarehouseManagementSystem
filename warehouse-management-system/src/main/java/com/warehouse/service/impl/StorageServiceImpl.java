package com.warehouse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse.entity.Storage;
import com.warehouse.repository.StorageRepository;
import com.warehouse.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Override
	public List<Storage> findAll() {
		return storageRepository.findAll();
	}

	@Override
	public Storage findById(Integer id) {
		return storageRepository.getOne(id);
	}

	@Override
	public void save(Storage storage) {
		storageRepository.save(storage);
	}

	@Override
	public void deleteById(Integer id) {
		storageRepository.deleteById(id);
	}

	@Override
	public List<Storage> search(String key1,String key2) {
		return storageRepository.findByNameOrLocation(key1,key2);
	}

	@Override
	public List<Storage> findByIsActive(Integer isActive) {
		return storageRepository.findByIsActive(isActive);
	}

}
