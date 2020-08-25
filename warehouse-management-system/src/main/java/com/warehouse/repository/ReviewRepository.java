package com.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

}
