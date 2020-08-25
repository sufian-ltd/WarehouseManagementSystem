package com.warehouse.service;

import java.util.List;

import com.warehouse.entity.Review;

public interface ReviewService {

	public List<Review> findAll();

	public Review findById(Integer id);

	public void save(Review review);

	public void deleteById(Integer id);
}
