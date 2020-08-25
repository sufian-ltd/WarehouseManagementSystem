package com.warehouse.service;

import java.util.List;

import com.warehouse.entity.Product;

public interface ProductService {

	public List<Product> findAll();

	public Product findById(Integer id);

	public void save(Product product);

	public void deleteById(Integer id);
}
