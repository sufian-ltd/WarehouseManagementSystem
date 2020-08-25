package com.warehouse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse.entity.Review;
import com.warehouse.repository.ReviewRepository;
import com.warehouse.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public List<Review> findAll() {
		return reviewRepository.findAll();
	}

	@Override
	public Review findById(Integer id) {
		return reviewRepository.getOne(id);
	}

	@Override
	public void save(Review review) {
		reviewRepository.save(review);
	}

	@Override
	public void deleteById(Integer id) {
		reviewRepository.deleteById(id);
	}

}
