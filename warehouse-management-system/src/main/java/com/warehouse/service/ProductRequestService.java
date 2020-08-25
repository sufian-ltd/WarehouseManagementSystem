package com.warehouse.service;

import java.util.List;

import com.warehouse.entity.ProductRequest;
import com.warehouse.entity.User;

public interface ProductRequestService {

	public List<ProductRequest> findAll();

	public ProductRequest findById(Integer id);

	public void save(ProductRequest productRequest);

	public void deleteById(Integer id);
	
	public List<ProductRequest> findByStatus(String status);
	
	public List<ProductRequest> ownerPendingRequest(User user);
	
	public List<ProductRequest> ownerAcceptedRequest(User user);
	
	public List<ProductRequest> ownerTransaction(User user);
	
	public List<ProductRequest> userPendingRequest(User user);
	
	public List<ProductRequest> userAcceptedRequest(User user);
	
	public List<ProductRequest> userTransaction(User user);
}
