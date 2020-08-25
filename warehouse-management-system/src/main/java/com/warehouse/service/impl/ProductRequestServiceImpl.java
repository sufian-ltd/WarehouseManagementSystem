package com.warehouse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse.constant.WarehouseConstant;
import com.warehouse.entity.ProductRequest;
import com.warehouse.entity.User;
import com.warehouse.repository.ProductRequestRepository;
import com.warehouse.service.ProductRequestService;

@Service
public class ProductRequestServiceImpl implements ProductRequestService {

	@Autowired
	private ProductRequestRepository productRequestRepository;

	@Autowired
	private ProductRequestService productRequestService;

	@Override
	public List<ProductRequest> findAll() {
		return productRequestRepository.findAll();
	}

	@Override
	public ProductRequest findById(Integer id) {
		return productRequestRepository.getOne(id);
	}

	@Override
	public void save(ProductRequest productRequest) {
		productRequestRepository.save(productRequest);
	}

	@Override
	public void deleteById(Integer id) {
		productRequestRepository.deleteById(id);
	}

	@Override
	public List<ProductRequest> findByStatus(String status) {
		return productRequestRepository.findByStatus(status);
	}

	@Override
	public List<ProductRequest> ownerPendingRequest(User user) {
		List<ProductRequest> productRequests = productRequestService.findByStatus(WarehouseConstant.PENDING_STATUS);
		productRequests.removeIf(e -> !e.getProduct().getStorage().getUser().getId().equals(user.getId()));
		return productRequests;
	}

	@Override
	public List<ProductRequest> ownerAcceptedRequest(User user) {
		List<ProductRequest> productRequests = productRequestService.findByStatus(WarehouseConstant.ACCPETED_STATUS);
		productRequests.removeIf(e -> !e.getProduct().getStorage().getUser().getId().equals(user.getId()));
		return productRequests;
	}

	@Override
	public List<ProductRequest> ownerTransaction(User user) {
		List<ProductRequest> productRequests = productRequestService.findByStatus(WarehouseConstant.CHECKOUT_STATUS);
		productRequests.removeIf(e -> !e.getProduct().getStorage().getUser().getId().equals(user.getId()));
		return productRequests;
	}

	@Override
	public List<ProductRequest> userPendingRequest(User user) {
		List<ProductRequest> productRequests = user.getProductRequests();
		productRequests.removeIf(e -> e.getStatus().equals(WarehouseConstant.CHECKOUT_STATUS)
				|| e.getStatus().equals(WarehouseConstant.ACCPETED_STATUS));
		return productRequests;
	}

	@Override
	public List<ProductRequest> userAcceptedRequest(User user) {
		List<ProductRequest> productRequests = user.getProductRequests();
		productRequests.removeIf(e -> e.getStatus().equals(WarehouseConstant.CHECKOUT_STATUS)
				|| e.getStatus().equals(WarehouseConstant.PENDING_STATUS));
		return productRequests;
	}

	@Override
	public List<ProductRequest> userTransaction(User user) {
		List<ProductRequest> productRequests = user.getProductRequests();
		productRequests.removeIf(e -> e.getStatus().equals(WarehouseConstant.PENDING_STATUS)
				|| e.getStatus().equals(WarehouseConstant.ACCPETED_STATUS));
		return productRequests;
	}

}
